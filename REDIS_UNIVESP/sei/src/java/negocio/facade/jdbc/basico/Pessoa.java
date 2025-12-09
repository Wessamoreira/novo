package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.richfaces.event.FileUploadEvent;
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

import controle.arquitetura.DataModelo;
import controle.arquitetura.MensagensRetornoErroEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaProfessorDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtualizacaoCadastralVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.CurriculumPessoaVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModalidadeBolsaEnum;
import negocio.comuns.basico.enumeradores.SituacaoMilitarEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.SexoEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.financeiro.BuscaCandidatoVagaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.academico.DisciplinasInteresse;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import negocio.facade.jdbc.administrativo.FormacaoExtraCurricular;
import negocio.facade.jdbc.administrativo.PessoaPreInscricaoCurso;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.basico.PessoaInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.AlunoAutoAtendimentoRSVO;

/**
 * Classe de persistï¿½ncia que encapsula todas as operaï¿½ï¿½es de manipulaï¿½ï¿½o dos
 * dados da classe <code>PessoaVO</code>. Responsável por implementar operaï¿½ï¿½es
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PessoaVO</code>. Encapsula toda a interaï¿½ï¿½o com o banco de dados.
 *
 * @see PessoaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
@SuppressWarnings({ "static-access", "unchecked", "serial", "rawtypes" })
public class Pessoa extends ControleAcesso implements PessoaInterfaceFacade {

	protected static String idEntidade;

	public Pessoa() throws Exception {
		super();
		setIdEntidade("Pessoa");
	}

	/**
	 * Operaï¿½ï¿½o Responsável por retornar um novo objeto da classe
	 * <code>PessoaVO</code>.
	 */
	public PessoaVO novo() throws Exception {
		Pessoa.incluir(getIdEntidade());
		PessoaVO obj = new PessoaVO();
		return obj;
	}

	public void validarIncluir(UsuarioVO usuario) throws Exception {
		Pessoa.incluir(getIdEntidade(), true, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void validarCPF(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String cpf = obj.getCPF();
		Iterator i = this.consultarPorCPF(cpf, "", false, nivelMontarDados, usuario).iterator();
		if (i.hasNext()) {
			if (obj.getAluno().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um aluno cadastrado com esse CPF.");
			} else if (obj.getProfessor().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um professor cadastrado com esse CPF.");
			} else if (obj.getMembroComunidade().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um membro da comunidade cadastrado com esse CPF.");
			} else if (obj.getCandidato().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um candidato cadastrado com esse CPF.");
			} else if (obj.getFuncionario().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um funcionário cadastrado com esse CPF.");
			}
		}
	}

	public void validarCPFUnico(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PessoaVO pessoaValidarCPF = this.consultarPorCPFUnico(obj.getCPF(), obj.getCodigo(), "", false, nivelMontarDados, usuario);
		if (pessoaValidarCPF.getCodigo() != 0 && !obj.getGerarNumeroCPF()) {
			if (obj.getAluno().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um aluno cadastrado com esse CPF.");
			} else if (obj.getProfessor().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um professor cadastrado com esse CPF.");
			} else if (obj.getMembroComunidade().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um membro da comunidade cadastrado com esse CPF.");
			} else if (obj.getCandidato().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um candidato cadastrado com esse CPF.");
			} else if (obj.getFuncionario().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um funcionário cadastrado com esse CPF.");
			}
		}
		if (Uteis.isAtributoPreenchido(obj.getFiliacaoVOs())) {
			if (obj.getFiliacaoVOs().stream().anyMatch(f -> Uteis.retirarMascaraCPF(f.getCPF()).equals(Uteis.retirarMascaraCPF(obj.getCPF()))) || obj.getFiliacaoVOs().stream().anyMatch(f -> Uteis.retirarMascaraCPF(f.getPais().getCPF()).equals(Uteis.retirarMascaraCPF(obj.getCPF())))) {
				throw new Exception("O CPF da filiação não pode ser igual ao da pessoa.");
			}
		}
	}

	public void validarRG(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String rg = obj.getRG();
		Iterator i = this.consultarPorRG(rg, "", false, nivelMontarDados, usuario).iterator();
		if (i.hasNext()) {
			if (obj.getAluno().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um aluno cadastrado com esse RG.");
			} else if (obj.getProfessor().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um professor cadastrado com esse RG.");
			} else if (obj.getMembroComunidade().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um membro da comunidade cadastrado com esse RG.");
			} else if (obj.getCandidato().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um candidato cadastrado com esse RG.");
			} else if (obj.getFuncionario().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um funcionário cadastrado com esse RG.");
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		incluir(obj, true, usuario, configuracaoGeralSistema, editadoPorAluno);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluirPessoaProspectMatriculaCRM(PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		incluirSemValidarDados(obj, false, usuario, configuracaoGeralSistema, editadoPorAluno, true);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarPessoaProspectMatriculaCRM(final PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
			obj.setNome(obj.getNome().toUpperCase());
		}
		final String sql = "UPDATE Pessoa set nome=?, telefoneComer=?, telefoneRes=?, telefoneRecado=?, celular=?, email=?, dataNasc=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, cidade=?, email2=?, nacionalidade=?, naturalidade=?, corraca=?, orgaoemissor=?, sexo=?, necessidadesEspeciais=?, rg=?, dataEmissaoRG=? , aluno = ?, nomeBatismo=? "
					+ " WHERE ((codigo = ?))" ;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
					sqlAlterar.setString(2, obj.getTelefoneComer());
					sqlAlterar.setString(3, obj.getTelefoneRes());
					sqlAlterar.setString(4, obj.getTelefoneRecado());
					sqlAlterar.setString(5, obj.getCelular());
					sqlAlterar.setString(6, obj.getEmail().trim());
					sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataNasc()));
					sqlAlterar.setString(8, obj.getEndereco());
					sqlAlterar.setString(9, obj.getSetor());
					sqlAlterar.setString(10, obj.getNumero());
					if(Uteis.validarMascaraCEP(obj.getCEP())) {
						sqlAlterar.setString(11, obj.getCEP());
					} else {
						sqlAlterar.setString(11, Uteis.formataMascaraCEP(obj.getCEP()));
					}
					sqlAlterar.setString(12, obj.getComplemento());
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(13, obj.getCidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(13, 0);
					}
					sqlAlterar.setString(14, obj.getEmail2().trim());

					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(15, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(15, 0);
					}
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(16, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					sqlAlterar.setString(17, obj.getCorRaca());
					sqlAlterar.setString(18, obj.getOrgaoEmissor());

					sqlAlterar.setString(19, obj.getSexo());
					sqlAlterar.setString(20, obj.getNecessidadesEspeciais());
					sqlAlterar.setString(21, obj.getRG());
					sqlAlterar.setDate(22, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
					sqlAlterar.setBoolean(23, obj.getAluno());
					sqlAlterar.setString(24, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
					sqlAlterar.setInt(25, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), true, usuario);

			getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
			if (!obj.getFiliacaoVOs().isEmpty()) {
				getFacadeFactory().getFiliacaoFacade().alterarFiliacaos(obj, obj.getFiliacaoVOs(), true, configuracaoGeralSistema, usuario);
			}
			getFacadeFactory().getUsuarioFacade().alterarNomeUsuario(obj.getCodigo(), obj.getNome(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemValidarDados(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, Boolean incluirFiliacao) throws Exception {

		try {

			validarCPFUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (obj.getArquivoImagem().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			}
			if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
				obj.setNome(obj.getNome().toUpperCase());
			}
			if (!obj.getPossuiFilho()) {
				obj.setQtdFilhos(null);
			}
			final String sql = "INSERT INTO Pessoa( nome, endereco, setor, numero, CEP, complemento, "
					+ "cidade, sexo, estadoCivil, telefoneComer, telefoneRes, " // 1-11
					+ "telefoneRecado, celular, email, dataNasc, naturalidade, "
					+ "nacionalidade, CPF, RG, certificadoMilitar, dataEmissaoRG, "//12-21
					+ "estadoEmissaoRG, orgaoEmissor, tituloEleitoral, necessidadesEspeciais, " 
					+ "funcionario, professor, aluno, candidato, membroComunidade, " 
					+ "paginaPessoal, valorCssTopoLogo, valorCssBackground, valorCssMenu, perfilEconomico, atuaComoDocente, " 
					+ "ativo, idalunoinep, passaporte, corraca, deficiencia, nomeFiador, enderecoFiador, telefoneFiador, cpfFiador, tipoNecessidadesEspeciais, " 
					+ "celularFiador, arquivoImagem, " 
					+ "email2,reponsavelUltimaAlteracao, dataUltimaAlteracao, coordenador, " 
					+ "ingles, espanhol, frances, inglesNivel, espanholNivel, francesNivel, outrosIdiomas, outrosIdiomasNivel, " 
					+ "windows, word, excel, access, powerPoint, internet, sap, corelDraw, autoCad, photoshop, microsiga, outrosSoftwares, qtdFilhos, participabancocurriculum, informacoesverdadeiras, divulgarmeusdados, "
					+ "certidaoNascimento, informacoesAdicionais, gerenciaPreInscricao, curriculoAtualizado,  pispasep, complementofiador, numeroendfiador, setorfiador, cepFiador, cidadeFiador ,  " 
					+ "possuiAcessoVisaoPais, codprospect, isentarTaxaBoleto, gravida, canhoto, portadorNecessidadeEspecial, situacaoMilitar, nomeBatismo, sabatista, banco, agencia, contacorrente, universidadeparceira, modalidadebolsa, valorbolsa, " 
					+ "tipoAssinaturaDocumentoEnum,tempoEstendidoProva, transtornosNeurodivergentes) " 
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? , " 
					+ " ? ,? ,? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " 
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"
					+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome().trim()));
					sqlInserir.setString(2, obj.getEndereco());
					sqlInserir.setString(3, obj.getSetor());
					sqlInserir.setString(4, obj.getNumero());
					sqlInserir.setString(5, obj.getCEP());
					sqlInserir.setString(6, obj.getComplemento());
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getSexo().equals("NENHUM")) {
						sqlInserir.setString(8, "");
					} else {
						sqlInserir.setString(8, obj.getSexo());
					}
					sqlInserir.setString(9, obj.getEstadoCivil());
					sqlInserir.setString(10, obj.getTelefoneComer());
					sqlInserir.setString(11, obj.getTelefoneRes());
					sqlInserir.setString(12, obj.getTelefoneRecado());
					sqlInserir.setString(13, obj.getCelular());
					sqlInserir.setString(14, obj.getEmail().trim());
					sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataNasc()));
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(16, 0);
					}
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(17, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(17, 0);
					}
					sqlInserir.setString(18, obj.getCPF());
					sqlInserir.setString(19, obj.getRG());
					sqlInserir.setString(20, obj.getCertificadoMilitar());
					sqlInserir.setDate(21, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
					sqlInserir.setString(22, obj.getEstadoEmissaoRG());
					sqlInserir.setString(23, obj.getOrgaoEmissor());
					sqlInserir.setString(24, obj.getTituloEleitoral());
					sqlInserir.setString(25, obj.getNecessidadesEspeciais());
					sqlInserir.setBoolean(26, obj.getFuncionario().booleanValue());
					sqlInserir.setBoolean(27, obj.getProfessor().booleanValue());
					sqlInserir.setBoolean(28, obj.getAluno().booleanValue());
					sqlInserir.setBoolean(29, obj.getCandidato().booleanValue());
					sqlInserir.setBoolean(30, obj.getMembroComunidade().booleanValue());
					sqlInserir.setString(31, obj.getPaginaPessoal());
					sqlInserir.setString(32, obj.getValorCssTopoLogo());
					sqlInserir.setString(33, obj.getValorCssBackground());
					sqlInserir.setString(34, obj.getValorCssMenu());

					if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
						sqlInserir.setInt(35, obj.getPerfilEconomico().getCodigo().intValue());
					} else {
						sqlInserir.setNull(35, 0);
					}
					sqlInserir.setString(36, obj.getAtuaComoDocente());
					sqlInserir.setBoolean(37, obj.getAtivo().booleanValue());

					if (!obj.getIdAlunoInep().equals("")) {
						sqlInserir.setString(38, obj.getIdAlunoInep());
					} else {
						sqlInserir.setNull(38, 0);
					}
					sqlInserir.setString(39, obj.getPassaporte());
					sqlInserir.setString(40, obj.getCorRaca());
					sqlInserir.setString(41, obj.getDeficiencia());
					sqlInserir.setString(42, obj.getNomeFiador());
					sqlInserir.setString(43, obj.getEnderecoFiador());
					sqlInserir.setString(44, obj.getTelefoneFiador());
					sqlInserir.setString(45, obj.getCpfFiador());
					sqlInserir.setString(46, obj.getTipoNecessidadesEspeciais());
					sqlInserir.setString(47, obj.getCelularFiador());
					if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
						sqlInserir.setInt(48, obj.getArquivoImagem().getCodigo().intValue());
					} else {
						sqlInserir.setNull(48, 0);
					}
					// sqlInserir.setString(49, obj.getNomeEmpresa());
					// sqlInserir.setString(50, obj.getEnderecoEmpresa());
					// sqlInserir.setString(51, obj.getCargoPessoaEmpresa());
					// sqlInserir.setString(52, obj.getCepEmpresa());
					// sqlInserir.setString(53, obj.getComplementoEmpresa());
					// if (obj.getCidadeEmpresa().getCodigo() != 0) {
					// sqlInserir.setInt(54,
					// obj.getCidadeEmpresa().getCodigo().intValue());
					// } else {
					// sqlInserir.setNull(54, 0);
					// }
					// sqlInserir.setString(55, obj.getSetorEmpresa());
					sqlInserir.setString(49, obj.getEmail2().trim());

					if (usuario != null && usuario.getCodigo() != 0) {
						sqlInserir.setInt(50, usuario.getCodigo().intValue());
					} else {
						sqlInserir.setNull(50, 0);
					}
					sqlInserir.setTimestamp(51, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setBoolean(52, obj.getCoordenador());

					sqlInserir.setBoolean(53, obj.getIngles());
					sqlInserir.setBoolean(54, obj.getEspanhol());
					sqlInserir.setBoolean(55, obj.getFrances());
					sqlInserir.setString(56, obj.getInglesNivel());
					sqlInserir.setString(57, obj.getEspanholNivel());
					sqlInserir.setString(58, obj.getFrancesNivel());
					sqlInserir.setString(59, obj.getOutrosIdiomas());
					sqlInserir.setString(60, obj.getOutrosIdiomasNivel());

					sqlInserir.setBoolean(61, obj.getWindows());
					sqlInserir.setBoolean(62, obj.getWord());
					sqlInserir.setBoolean(63, obj.getExcel());
					sqlInserir.setBoolean(64, obj.getAccess());
					sqlInserir.setBoolean(65, obj.getPowerPoint());
					sqlInserir.setBoolean(66, obj.getInternet());
					sqlInserir.setBoolean(67, obj.getSap());
					sqlInserir.setBoolean(68, obj.getCorelDraw());
					sqlInserir.setBoolean(69, obj.getAutoCad());
					sqlInserir.setBoolean(70, obj.getPhotoshop());
					sqlInserir.setBoolean(71, obj.getMicrosiga());
					sqlInserir.setString(72, obj.getOutrosSoftwares());
					sqlInserir.setInt(73, obj.getQtdFilhos());
					sqlInserir.setBoolean(74, obj.getParticipaBancoCurriculum().booleanValue());
					sqlInserir.setBoolean(75, obj.getInformacoesVerdadeiras().booleanValue());
					sqlInserir.setBoolean(76, obj.getDivulgarMeusDados().booleanValue());
					sqlInserir.setString(77, obj.getCertidaoNascimento());
					sqlInserir.setString(78, obj.getInformacoesAdicionais());
					sqlInserir.setBoolean(79, obj.getGerenciaPreInscricao());
					sqlInserir.setBoolean(80, obj.getCurriculoAtualizado());
					sqlInserir.setString(81, obj.getPispasep());
					sqlInserir.setString(82, obj.getComplementoFiador());
					sqlInserir.setString(83, obj.getNumeroEndFiador());
					sqlInserir.setString(84, obj.getSetorFiador());
					sqlInserir.setString(85, obj.getCepFiador());
					if (obj.getCidadeFiador().getCodigo().intValue() != 0) {
						sqlInserir.setInt(86, obj.getCidadeFiador().getCodigo().intValue());
					} else {
						sqlInserir.setNull(86, 0);
					}
					sqlInserir.setBoolean(87, obj.getPossuiAcessoVisaoPais());
					sqlInserir.setInt(88, obj.getCodProspect());
					sqlInserir.setBoolean(89, obj.getIsentarTaxaBoleto());
					sqlInserir.setBoolean(90, obj.getGravida());
					sqlInserir.setBoolean(91, obj.getCanhoto());
					sqlInserir.setBoolean(92, obj.getPortadorNecessidadeEspecial());
					sqlInserir.setString(93, obj.getSituacaoMilitar().name());
					sqlInserir.setString(94, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo().trim()));
					sqlInserir.setBoolean(95, obj.getSabatista());
					sqlInserir.setString(96, obj.getBanco());
					sqlInserir.setString(97, obj.getAgencia());
					sqlInserir.setString(98, obj.getContaCorrente());
					sqlInserir.setString(99, obj.getUniversidadeParceira());					
					sqlInserir.setString(100, obj.getModalidadeBolsa().name());
					sqlInserir.setDouble(101, obj.getValorBolsa());
					sqlInserir.setString(102, obj.getTipoAssinaturaDocumentoEnum().name());
					sqlInserir.setBoolean(103, obj.getTempoEstendidoProva());
					sqlInserir.setString(104, obj.getTranstornosNeurodivergentes());
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

			obj.setNovoObj(Boolean.FALSE);

			if (!editadoPorAluno) {
				getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getFormacaoAcademicaFacade().incluirFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);

//				getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade(this.getIdEntidade());
//				getFacadeFactory().getFormacaoExtraCurricularFacade().incluirFormacaoExtraCurricular(obj, obj.getFormacaoExtraCurricularVOs(), verificarAcesso);

				getFacadeFactory().getDadosComerciaisFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDadosComerciaisFacade().incluirDadosComerciais(obj, obj.getDadosComerciaisVOs(), verificarAcesso);

//				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade(this.getIdEntidade());
//				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().incluirAreaProfissionalInteresseContratacao(obj.getCodigo(), obj.getAreaProfissionalInteresseContratacaoVOs(), usuario);
			}
			if (obj.getProfessor().equals(Boolean.TRUE)) {
				getFacadeFactory().getDocumetacaoPessoaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDocumetacaoPessoaFacade().incluirDocumetacaoPessoas(obj, obj.getCodigo(), obj.getDocumetacaoPessoaVOs(), usuario, configuracaoGeralSistema);

				getFacadeFactory().getDisciplinasInteresseFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDisciplinasInteresseFacade().incluirDisciplinasInteresses(obj.getCodigo(), obj.getDisciplinasInteresseVOs(), usuario);

				getFacadeFactory().getHorarioProfessorFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getHorarioProfessorFacade().incluirHorarioProfessor(obj.getCodigo(), obj.getHorarioProfessorVOs(), null, null, usuario);
			}

			if (obj.getGerenciaPreInscricao().equals(Boolean.TRUE)) {
				getFacadeFactory().getPessoaPreInscricaoCursoFacade().incluirPessoaPreInscricaoCursos(obj.getCodigo(), obj.getPessoaPreInscricaoCursoVOs());
			}
			if(incluirFiliacao) {
				getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getFiliacaoFacade().incluirFiliacaos(obj, obj.getFiliacaoVOs(), false, configuracaoGeralSistema, usuario);
			}
			

//			Autorizado pelo Rodrigo na importação da planilha Excel
//			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
			
			if (obj.getGerarNumeroCPF()) {
				executarGeracaoNumeroCPF(obj);
			}
		} catch (Exception e) {
			if(e.getMessage().contains("fn_pessoa_validarunicidadeemail()")) {
		         String	mensagemTratada =" Não é permitido/possível cadastrar um email que já é utilizado por outra pessoa. ("+obj.getEmail()+")  ";				
				e = new  Exception(mensagemTratada);
			}	
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		incluir(obj, verificarAcesso, usuario, configuracaoGeralSistema, editadoPorAluno, true, false, false, editadoPorAluno);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato, boolean validaCamposEnadeCenso, boolean validarEndereco) throws Exception {

		try {
			Pessoa.incluir(getIdEntidade(), verificarAcesso, usuario);
			boolean validarCPF = false;
			if (deveValidarCPF) {
				validarCPF = getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, usuario);
			}

			if (validarCandidato) {
				PessoaVO.validarDadosCandidato(obj,deveValidarCPF, configuracaoGeralSistema.getConfiguracaoCandidatoProcessoSeletivoVO());
			}else {
				PessoaVO.validarDados(obj, validarCPF, validaCamposEnadeCenso, validarEndereco);
			}

			validarCPFUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			validarRegistroAcademicoUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (obj.getArquivoImagem().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			}
			if (!obj.getPossuiFilho()) {
				obj.setQtdFilhos(null);
			}
			if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
				obj.setNome(obj.getNome().toUpperCase());
			}
			final String sql = "INSERT INTO Pessoa( nome, endereco, setor, numero, CEP, complemento, "
					+ "cidade, sexo, estadoCivil, telefoneComer, telefoneRes, " // 1-11
					+ "telefoneRecado, celular, email, dataNasc, naturalidade, "
					+ "nacionalidade, CPF, RG, certificadoMilitar, dataEmissaoRG, "//
					+ "estadoEmissaoRG, orgaoEmissor, tituloEleitoral, necessidadesEspeciais, " 
					+ "funcionario, professor, aluno, candidato, membroComunidade, " 
					+ "paginaPessoal, valorCssTopoLogo, valorCssBackground, valorCssMenu, perfilEconomico, atuaComoDocente, " 
					+ "ativo, idalunoinep, passaporte, corraca, deficiencia, nomeFiador, enderecoFiador, telefoneFiador, cpfFiador, tipoNecessidadesEspeciais, " 
					+ "celularFiador, arquivoImagem, " + "email2,reponsavelUltimaAlteracao, dataUltimaAlteracao, coordenador, " 
					+ "ingles, espanhol, frances, inglesNivel, espanholNivel, francesNivel, outrosIdiomas, outrosIdiomasNivel, " 
					+ "windows, word, excel, access, powerPoint, internet, sap, corelDraw, autoCad, photoshop, microsiga, outrosSoftwares, qtdFilhos, participabancocurriculum, informacoesverdadeiras, divulgarmeusdados, "
					+ "certidaoNascimento, informacoesAdicionais, gerenciaPreInscricao, curriculoAtualizado,  pispasep, complementofiador, numeroendfiador, setorfiador, cepFiador, cidadeFiador ,  " 
					+ "possuiAcessoVisaoPais, codprospect, isentarTaxaBoleto, gravida, canhoto, portadorNecessidadeEspecial, " 
					+ "dataExpedicaoTituloEleitoral, zonaEleitoral, dataExpedicaoCertificadoMilitar, orgaoExpedidorCertificadoMilitar, situacaoMilitar, nomeCenso, grauParentesco, permiteenviarremessa, "
					+ "senhacertificadoparadocumento, estadoCivilFiador, rgFiador, profissaoFiador, paisFiador, dataNascimentoFiador, secaoZonaEleitoral, tipomidiacaptacao, nomeBatismo, banco, agencia, "
					+ "contaCorrente ,registroAcademico, sabatista, universidadeparceira, modalidadebolsa, valorbolsa, " 
					+ "tipoAssinaturaDocumentoEnum, tempoEstendidoProva, renovacaoAutomatica, transtornosNeurodivergentes ) " 
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? , " 
					+ "? ,? ,? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? ,? , " 
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?) "
					+ "returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNome()).trim());
					sqlInserir.setString(2, obj.getEndereco());
					sqlInserir.setString(3, obj.getSetor());
					sqlInserir.setString(4, obj.getNumero());
					if(Uteis.validarMascaraCEP(obj.getCEP())) {
						sqlInserir.setString(5, obj.getCEP());
					} else {
						sqlInserir.setString(5, Uteis.formataMascaraCEP(obj.getCEP()));
					}
					sqlInserir.setString(6, obj.getComplemento());
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getSexo());
					sqlInserir.setString(9, obj.getEstadoCivil());
					sqlInserir.setString(10, obj.getTelefoneComer());
					sqlInserir.setString(11, obj.getTelefoneRes());
					sqlInserir.setString(12, obj.getTelefoneRecado());
					sqlInserir.setString(13, obj.getCelular());
					sqlInserir.setString(14, obj.getEmail().trim());
					sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataNasc()));
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(16, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(16, 0);
					}
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(17, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(17, 0);
					}
					sqlInserir.setString(18, obj.getCPF());
					sqlInserir.setString(19, obj.getRG());
					sqlInserir.setString(20, obj.getCertificadoMilitar());
					sqlInserir.setDate(21, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
					sqlInserir.setString(22, obj.getEstadoEmissaoRG());
					sqlInserir.setString(23, obj.getOrgaoEmissor());
					sqlInserir.setString(24, obj.getTituloEleitoral());
					sqlInserir.setString(25, obj.getNecessidadesEspeciais());
					sqlInserir.setBoolean(26, obj.getFuncionario().booleanValue());
					sqlInserir.setBoolean(27, obj.getProfessor().booleanValue());
					sqlInserir.setBoolean(28, obj.getAluno().booleanValue());
					sqlInserir.setBoolean(29, obj.getCandidato().booleanValue());
					sqlInserir.setBoolean(30, obj.getMembroComunidade().booleanValue());
					sqlInserir.setString(31, obj.getPaginaPessoal());
					sqlInserir.setString(32, obj.getValorCssTopoLogo());
					sqlInserir.setString(33, obj.getValorCssBackground());
					sqlInserir.setString(34, obj.getValorCssMenu());

					if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
						sqlInserir.setInt(35, obj.getPerfilEconomico().getCodigo().intValue());
					} else {
						sqlInserir.setNull(35, 0);
					}
					sqlInserir.setString(36, obj.getAtuaComoDocente());
					sqlInserir.setBoolean(37, obj.getAtivo().booleanValue());

					if (!obj.getIdAlunoInep().equals("")) {
						sqlInserir.setString(38, obj.getIdAlunoInep());
					} else {
						sqlInserir.setNull(38, 0);
					}
					sqlInserir.setString(39, obj.getPassaporte());
					sqlInserir.setString(40, obj.getCorRaca());
					sqlInserir.setString(41, obj.getDeficiencia());
					sqlInserir.setString(42, obj.getNomeFiador());
					sqlInserir.setString(43, obj.getEnderecoFiador());
					sqlInserir.setString(44, obj.getTelefoneFiador());
					sqlInserir.setString(45, obj.getCpfFiador());
					sqlInserir.setString(46, obj.getTipoNecessidadesEspeciais());
					sqlInserir.setString(47, obj.getCelularFiador());
					if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
						sqlInserir.setInt(48, obj.getArquivoImagem().getCodigo().intValue());
					} else {
						sqlInserir.setNull(48, 0);
					}

					sqlInserir.setString(49, obj.getEmail2().trim());

					if (usuario != null && usuario.getCodigo() != 0) {
						sqlInserir.setInt(50, usuario.getCodigo().intValue());
					} else {
						sqlInserir.setNull(50, 0);
					}
					sqlInserir.setTimestamp(51, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setBoolean(52, obj.getCoordenador());

					sqlInserir.setBoolean(53, obj.getIngles());
					sqlInserir.setBoolean(54, obj.getEspanhol());
					sqlInserir.setBoolean(55, obj.getFrances());
					sqlInserir.setString(56, obj.getInglesNivel());
					sqlInserir.setString(57, obj.getEspanholNivel());
					sqlInserir.setString(58, obj.getFrancesNivel());
					sqlInserir.setString(59, obj.getOutrosIdiomas());
					sqlInserir.setString(60, obj.getOutrosIdiomasNivel());

					sqlInserir.setBoolean(61, obj.getWindows());
					sqlInserir.setBoolean(62, obj.getWord());
					sqlInserir.setBoolean(63, obj.getExcel());
					sqlInserir.setBoolean(64, obj.getAccess());
					sqlInserir.setBoolean(65, obj.getPowerPoint());
					sqlInserir.setBoolean(66, obj.getInternet());
					sqlInserir.setBoolean(67, obj.getSap());
					sqlInserir.setBoolean(68, obj.getCorelDraw());
					sqlInserir.setBoolean(69, obj.getAutoCad());
					sqlInserir.setBoolean(70, obj.getPhotoshop());
					sqlInserir.setBoolean(71, obj.getMicrosiga());
					sqlInserir.setString(72, obj.getOutrosSoftwares());
					sqlInserir.setInt(73, obj.getQtdFilhos());
					sqlInserir.setBoolean(74, obj.getParticipaBancoCurriculum().booleanValue());
					sqlInserir.setBoolean(75, obj.getInformacoesVerdadeiras().booleanValue());
					sqlInserir.setBoolean(76, obj.getDivulgarMeusDados().booleanValue());
					sqlInserir.setString(77, obj.getCertidaoNascimento());
					sqlInserir.setString(78, obj.getInformacoesAdicionais());
					sqlInserir.setBoolean(79, obj.getGerenciaPreInscricao());
					sqlInserir.setBoolean(80, obj.getCurriculoAtualizado());
					sqlInserir.setString(81, obj.getPispasep());
					sqlInserir.setString(82, obj.getComplementoFiador());
					sqlInserir.setString(83, obj.getNumeroEndFiador());
					sqlInserir.setString(84, obj.getSetorFiador());
					sqlInserir.setString(85, obj.getCepFiador());
					if (obj.getCidadeFiador().getCodigo().intValue() != 0) {
						sqlInserir.setInt(86, obj.getCidadeFiador().getCodigo().intValue());
					} else {
						sqlInserir.setNull(86, 0);
					}
					sqlInserir.setBoolean(87, obj.getPossuiAcessoVisaoPais());
					sqlInserir.setInt(88, obj.getCodProspect());
					sqlInserir.setBoolean(89, obj.getIsentarTaxaBoleto());
					sqlInserir.setBoolean(90, obj.getGravida());
					sqlInserir.setBoolean(91, obj.getCanhoto());
					sqlInserir.setBoolean(92, obj.getPortadorNecessidadeEspecial());

					if (obj.getDataExpedicaoTituloEleitoral() != null) {
						sqlInserir.setTimestamp(93, Uteis.getDataJDBCTimestamp(obj.getDataExpedicaoTituloEleitoral()));
					} else {
						sqlInserir.setNull(93, 0);
					}

					sqlInserir.setString(94, obj.getZonaEleitoral());

					if (obj.getDataExpedicaoTituloEleitoral() != null) {
						sqlInserir.setTimestamp(95, Uteis.getDataJDBCTimestamp(obj.getDataExpedicaoCertificadoMilitar()));
					} else {
						sqlInserir.setNull(95, 0);
					}
					sqlInserir.setString(96, obj.getOrgaoExpedidorCertificadoMilitar());
					if (obj.getSituacaoMilitar() != null) {
						sqlInserir.setString(97, obj.getSituacaoMilitar().name());
					} else {
						sqlInserir.setNull(97, 0);
					}
					sqlInserir.setString(98, obj.getNomeCenso());
					sqlInserir.setString(99, obj.getGrauParentesco());
					sqlInserir.setBoolean(100, obj.isPermiteEnviarRemessa());

					if (Uteis.isAtributoPreenchido(obj.getSenhaCertificadoParaDocumento())) {
						sqlInserir.setString(101, obj.getSenhaCertificadoParaDocumento());
					} else {
						sqlInserir.setNull(101, 0);
					}
					sqlInserir.setString(102, obj.getEstadoCivilFiador());
					sqlInserir.setString(103, obj.getRgFiador());
					sqlInserir.setString(104, obj.getProfissaoFiador());
					if (Uteis.isAtributoPreenchido(obj.getPaisFiador())) {
						sqlInserir.setInt(105, obj.getPaisFiador().getCodigo());
					} else {
						sqlInserir.setNull(105, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataNascimentoFiador())) {
						sqlInserir.setDate(106, Uteis.getDataJDBC(obj.getDataNascimentoFiador()));
					} else {
						sqlInserir.setNull(106, 0);
					}
					sqlInserir.setString(107, obj.getSecaoZonaEleitoral());

					if(obj.getTipoMidiaCaptacao() != null && obj.getTipoMidiaCaptacao().getCodigo() != null && obj.getTipoMidiaCaptacao().getCodigo() > 0) {
						sqlInserir.setInt(108, obj.getTipoMidiaCaptacao().getCodigo());
					}
					else {
						sqlInserir.setNull(108, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getNomeBatismo())) {
						sqlInserir.setString(109, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNomeBatismo()).trim());
					}else {
						sqlInserir.setString(109, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNome()).trim());
					}
					if(Uteis.isAtributoPreenchido(obj.getBanco())) {
						sqlInserir.setString(110, obj.getBanco());
					}else {
						sqlInserir.setNull(110, 0);
					}
					
					if(Uteis.isAtributoPreenchido(obj.getAgencia())) {
						sqlInserir.setString(111, obj.getAgencia());
					}else {
						sqlInserir.setNull(111, 0);
					}
					
					if(Uteis.isAtributoPreenchido(obj.getContaCorrente())) {
						sqlInserir.setString(112, obj.getContaCorrente());
					}else {
						sqlInserir.setNull(112, 0);
					}
					sqlInserir.setString(113, obj.getRegistroAcademico());
					sqlInserir.setBoolean(114, obj.getSabatista());
					
					if(Uteis.isAtributoPreenchido(obj.getUniversidadeParceira())) {
						sqlInserir.setString(115, obj.getUniversidadeParceira());
					}else {
						sqlInserir.setNull(115, 0);
					}
					
					if(Uteis.isAtributoPreenchido(obj.getModalidadeBolsa())) {
						sqlInserir.setString(116, obj.getModalidadeBolsa().name());
					}else {
						sqlInserir.setNull(116, 0);
					}
					
					if(Uteis.isAtributoPreenchido(obj.getValorBolsa())) {
						sqlInserir.setDouble(117, obj.getValorBolsa());
					}else {
						sqlInserir.setNull(117, 0);
					}
					sqlInserir.setString(118, obj.getTipoAssinaturaDocumentoEnum().name());
					sqlInserir.setBoolean(119, obj.getTempoEstendidoProva());
					sqlInserir.setBoolean(120, obj.getRenovacaoAutomatica());
					sqlInserir.setString(121, obj.getTranstornosNeurodivergentes());
					return sqlInserir;

				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);

			if (!editadoPorAluno) {
				getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getFormacaoAcademicaFacade().incluirFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);

//				getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade(this.getIdEntidade());
//				getFacadeFactory().getFormacaoExtraCurricularFacade().incluirFormacaoExtraCurricular(obj, obj.getFormacaoExtraCurricularVOs(), verificarAcesso);

				getFacadeFactory().getDadosComerciaisFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDadosComerciaisFacade().incluirDadosComerciais(obj, obj.getDadosComerciaisVOs(), verificarAcesso);

//				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade(this.getIdEntidade());
//				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().incluirAreaProfissionalInteresseContratacao(obj.getCodigo(), obj.getAreaProfissionalInteresseContratacaoVOs(), usuario);
			}
			if (obj.getProfessor().equals(Boolean.TRUE)) {
				getFacadeFactory().getDocumetacaoPessoaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDocumetacaoPessoaFacade().incluirDocumetacaoPessoas(obj, obj.getCodigo(), obj.getDocumetacaoPessoaVOs(), usuario, configuracaoGeralSistema);

				getFacadeFactory().getDisciplinasInteresseFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDisciplinasInteresseFacade().incluirDisciplinasInteresses(obj.getCodigo(), obj.getDisciplinasInteresseVOs(), usuario);

				getFacadeFactory().getHorarioProfessorFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getHorarioProfessorFacade().incluirHorarioProfessor(obj.getCodigo(), obj.getHorarioProfessorVOs(), null, null, usuario);
			}

			if (obj.getGerenciaPreInscricao().equals(Boolean.TRUE)) {
				getFacadeFactory().getPessoaPreInscricaoCursoFacade().incluirPessoaPreInscricaoCursos(obj.getCodigo(), obj.getPessoaPreInscricaoCursoVOs());
			}

			if (obj.getIncluirProspect()) {
				realizarVinculoPessoaProspect(obj, usuario);
			}
			getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getFiliacaoFacade().incluirFiliacaos(obj, obj.getFiliacaoVOs(), deveValidarCPF, configuracaoGeralSistema, usuario);			
			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
			if (obj.getGerarNumeroCPF()) {
				executarGeracaoNumeroCPF(obj);
			}

		} catch (Exception e) {

			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	private void validarRegistroAcademicoUnico(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {		
		
		if(obj != null &&
		   Uteis.isAtributoPreenchido(obj.getRegistroAcademico()) &&
		   getFacadeFactory().getPessoaFacade().verificarSeExistePessoaUtilizandoRegistroAcademico(obj.getRegistroAcademico(), true, obj.getCodigo())) {
		
			throw new ConsistirException("Já existe um aluno cadastrado com esse Registro Acadêmico.");					
		}	
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(PessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		alterar(obj, true, usuario, configuracaoGeralSistema, editadoPorAluno);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void alterarPessoaConformeProspect(final ProspectsVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, Boolean origemPreInscricao) throws Exception {
		if (obj.getPessoa() != null && obj.getPessoa().getCodigo() != null && obj.getPessoa().getCodigo() > 0) {
			if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
				obj.setNome(obj.getNome().toUpperCase());
			}
			getFacadeFactory().getPessoaFacade().inicializarDadosPessoaBaseadoProspect(obj, usuario);
			getFacadeFactory().getPessoaFacade().validarDadosPessoaCasoPossuaMatricula(obj.getPessoa(), origemPreInscricao, usuario);
			final String sql = " UPDATE Pessoa set nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, cidade=?, sexo=?, estadoCivil=?, telefoneComer=?, " + " telefoneRes=?, telefoneRecado=?, celular=?, email=?, dataNasc=?, naturalidade=?, nacionalidade=?, CPF=?, RG=?, " + " dataEmissaoRG=?, estadoEmissaoRG=?, orgaoEmissor=?, arquivoImagem=?, " + " email2=?, reponsavelUltimaAlteracao=?, dataUltimaAlteracao=?, nomeEmpresa=?, cargoPessoaEmpresa = ?, tipomidiacaptacao = ?, nomeBatismo = ?, sabatista = ? " + " WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(x++, Uteis.removeCaractersEspeciais2(obj.getNome()));
					sqlAlterar.setString(x++, obj.getEndereco());
					sqlAlterar.setString(x++, obj.getSetor());
					sqlAlterar.setString(x++, obj.getNumero());
					sqlAlterar.setString(x++, obj.getCEP());
					sqlAlterar.setString(x++, obj.getComplemento());
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getCidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if(!obj.getSexo().equals("NENHUM")){
						sqlAlterar.setString(x++, obj.getSexo());
					}else{
						sqlAlterar.setString(x++, "");
					}
					sqlAlterar.setString(x++, obj.getEstadoCivil());
					sqlAlterar.setString(x++, obj.getTelefoneComercial());
					sqlAlterar.setString(x++, obj.getTelefoneResidencial());
					sqlAlterar.setString(x++, obj.getTelefoneRecado());
					sqlAlterar.setString(x++, obj.getCelular());
					sqlAlterar.setString(x++, obj.getEmailPrincipal().trim());
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(obj.getDataNascimento()));
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, obj.getCpf());
					sqlAlterar.setString(x++, obj.getRg());
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(obj.getDataExpedicao()));
					sqlAlterar.setString(x++, obj.getEstadoEmissor());
					sqlAlterar.setString(x++, obj.getOrgaoEmissor());
					if (obj.getArquivoFoto().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getArquivoFoto().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, obj.getEmailSecundario().trim());
					if (usuario != null && usuario.getCodigo() != 0) {
						sqlAlterar.setInt(x++, usuario.getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setString(x++, obj.getNomeEmpresa());
					sqlAlterar.setString(x++, obj.getCargo());
					if (obj.getTipoMidia().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getTipoMidia().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
					sqlAlterar.setBoolean(x++, obj.getPessoa().getSabatista());
					sqlAlterar.setInt(x++, obj.getPessoa().getCodigo().intValue());
					
					return sqlAlterar;
				}
			}) > 0) {
				getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(obj.getPessoa().getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);
			}			
			getFacadeFactory().getUsuarioFacade().alterarNomeUsuario(obj.getPessoa().getCodigo(), obj.getNome(), usuario);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		alterar(obj, verificarAcesso, usuario, configuracaoGeralSistema, editadoPorAluno, true, false, false, editadoPorAluno);
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato, boolean validarDadosCensoEnade, boolean validarEndereco) throws Exception {
		Pessoa.alterar(getIdEntidade(), verificarAcesso, usuario);
		boolean validarCPF = false;
		if (deveValidarCPF) {
			validarCPF = getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, usuario);
		}
		if(validarCandidato) {
			PessoaVO.validarDadosCandidato(obj, deveValidarCPF, configuracaoGeralSistema.getConfiguracaoCandidatoProcessoSeletivoVO());
		}else {
			PessoaVO.validarDados(obj, validarCPF, validarDadosCensoEnade, validarEndereco);
		}

		validarCPFUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		validarRegistroAcademicoUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (obj.getArquivoImagem().getPastaBaseArquivoEnum() != null) {
			if (obj.getArquivoImagem().getCodigo() == 0) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			} else {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			}
		}
		if (!obj.getPossuiFilho()) {
			obj.setQtdFilhos(0);
		}
		if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
			obj.setNome(obj.getNome().toUpperCase());
		}
		final String sql = "UPDATE Pessoa set nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, " 
		+ "cidade=?, sexo=?, estadoCivil=?, telefoneComer=?, " 
		+ "telefoneRes=?, telefoneRecado=?, celular=?, email=?, dataNasc=?, naturalidade=?, nacionalidade=?, CPF=?, RG=?, " 
		+ "certificadoMilitar=?, dataEmissaoRG=?, estadoEmissaoRG=?, orgaoEmissor=?, tituloEleitoral=?, necessidadesEspeciais=?, funcionario = ?, professor = ?, aluno=?, candidato=?, " 
		+ "membroComunidade = ?, paginaPessoal = ?, valorCssTopoLogo=?, valorCssBackground=?, valorCssMenu=?, perfilEconomico=?, " 
		+ "atuaComoDocente=?, ativo=?, idalunoinep=?, passaporte=?, corraca=?, deficiencia=?, nomeFiador=?, enderecoFiador=?, telefoneFiador=?, " 
		+ "cpfFiador=?, tipoNecessidadesEspeciais=?, celularFiador=?, arquivoImagem=?, email2=?,reponsavelUltimaAlteracao=?, dataUltimaAlteracao=?, coordenador=?,"
		+ "ingles=?, espanhol=?, frances=?, inglesNivel=?, espanholNivel=?, francesNivel=?, outrosIdiomas=?, outrosIdiomasNivel=?, " 
		+ "windows=?, word=?, excel=?, access=?, powerPoint=?, internet=?, sap=?, corelDraw=?, autoCad=?, photoshop=?, microsiga=?, outrosSoftwares=?, " 
		+ "qtdFilhos=?, participabancocurriculum=?, informacoesverdadeiras=?, divulgarmeusdados=?, certidaoNascimento=?, informacoesAdicionais=?, " 
		+ "gerenciaPreInscricao=?, curriculoAtualizado=?, pispasep=?, complementofiador=?, numeroendfiador=?, setorfiador=?, cepFiador=?, cidadeFiador=?, " 
		+ "possuiAcessoVisaoPais=?, isentarTaxaBoleto = ?, gravida=?, canhoto=?, portadorNecessidadeEspecial=?, dataExpedicaoTituloEleitoral=?, zonaEleitoral=?, dataExpedicaoCertificadoMilitar=?, "
		+ "orgaoExpedidorCertificadoMilitar=?, situacaoMilitar=?, nomeCenso=? , grauParentesco=?, permiteenviarremessa=?, senhacertificadoparadocumento=?, "
		+ "estadoCivilFiador=?, rgFiador=?, profissaoFiador=?, paisFiador=?, dataNascimentoFiador=? , secaoZonaEleitoral=?, tipomidiacaptacao=?, nomeBatismo=?, banco=?, agencia=?, contaCorrente=? ,"
		+ "registroAcademico=?, sabatista=?, universidadeParceira=?, modalidadeBolsa=?, valorBolsa=?, tipoAssinaturaDocumentoEnum=?, tempoEstendidoProva=?, renovacaoAutomatica=?, transtornosNeurodivergentes=? "
		+ " WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNome()));
				sqlAlterar.setString(2, obj.getEndereco());
				sqlAlterar.setString(3, obj.getSetor());
				sqlAlterar.setString(4, obj.getNumero());
				if(Uteis.validarMascaraCEP(obj.getCEP())) {
					sqlAlterar.setString(5, obj.getCEP());
				} else {
					sqlAlterar.setString(5, Uteis.formataMascaraCEP(obj.getCEP()));
				}
				sqlAlterar.setString(6, obj.getComplemento());
				if (obj.getCidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(7, 0);
				}
				sqlAlterar.setString(8, obj.getSexo());
				sqlAlterar.setString(9, obj.getEstadoCivil());
				sqlAlterar.setString(10, obj.getTelefoneComer());
				sqlAlterar.setString(11, obj.getTelefoneRes());
				sqlAlterar.setString(12, obj.getTelefoneRecado());
				sqlAlterar.setString(13, obj.getCelular());
				sqlAlterar.setString(14, obj.getEmail().trim());
				sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataNasc()));
				if (obj.getNaturalidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(16, obj.getNaturalidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(16, 0);
				}
				if (obj.getNacionalidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(17, obj.getNacionalidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(17, 0);
				}
				sqlAlterar.setString(18, obj.getCPF());
				sqlAlterar.setString(19, obj.getRG());
				sqlAlterar.setString(20, obj.getCertificadoMilitar());
				sqlAlterar.setDate(21, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
				sqlAlterar.setString(22, obj.getEstadoEmissaoRG());
				sqlAlterar.setString(23, obj.getOrgaoEmissor());
				sqlAlterar.setString(24, obj.getTituloEleitoral());
				sqlAlterar.setString(25, obj.getNecessidadesEspeciais());
				sqlAlterar.setBoolean(26, obj.getFuncionario().booleanValue());
				sqlAlterar.setBoolean(27, obj.getProfessor().booleanValue());
				sqlAlterar.setBoolean(28, obj.getAluno().booleanValue());
				sqlAlterar.setBoolean(29, obj.getCandidato().booleanValue());
				sqlAlterar.setBoolean(30, obj.getMembroComunidade().booleanValue());
				sqlAlterar.setString(31, obj.getPaginaPessoal());
				sqlAlterar.setString(32, obj.getValorCssTopoLogo());
				sqlAlterar.setString(33, obj.getValorCssBackground());
				sqlAlterar.setString(34, obj.getValorCssMenu());

				if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(35, obj.getPerfilEconomico().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(35, 0);
				}
				sqlAlterar.setString(36, obj.getAtuaComoDocente());
				sqlAlterar.setBoolean(37, obj.getAtivo().booleanValue());
				if (!obj.getIdAlunoInep().equals("")) {
					sqlAlterar.setString(38, obj.getIdAlunoInep());
				} else {
					sqlAlterar.setNull(38, 0);
				}
				sqlAlterar.setString(39, obj.getPassaporte());
				sqlAlterar.setString(40, obj.getCorRaca());
				sqlAlterar.setString(41, obj.getDeficiencia());
				sqlAlterar.setString(42, obj.getNomeFiador());
				sqlAlterar.setString(43, obj.getEnderecoFiador());
				sqlAlterar.setString(44, obj.getTelefoneFiador());
				sqlAlterar.setString(45, obj.getCpfFiador());
				sqlAlterar.setString(46, obj.getTipoNecessidadesEspeciais());
				sqlAlterar.setString(47, obj.getCelularFiador());
				if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(48, obj.getArquivoImagem().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(48, 0);
				}
				// sqlAlterar.setString(49, obj.getNomeEmpresa());
				// sqlAlterar.setString(50, obj.getEnderecoEmpresa());
				// sqlAlterar.setString(51, obj.getCargoPessoaEmpresa());
				// sqlAlterar.setString(52, obj.getCepEmpresa());
				// sqlAlterar.setString(53, obj.getComplementoEmpresa());
				// if (obj.getCidadeEmpresa().getCodigo() != 0) {
				// sqlAlterar.setInt(54,
				// obj.getCidadeEmpresa().getCodigo().intValue());
				// } else {
				// sqlAlterar.setNull(54, 0);
				// }
				// sqlAlterar.setString(55, obj.getSetorEmpresa());
				sqlAlterar.setString(49, obj.getEmail2().trim());
				if (usuario != null && usuario.getCodigo() != 0) {
					sqlAlterar.setInt(50, usuario.getCodigo().intValue());
				} else {
					sqlAlterar.setNull(50, 0);
				}
				sqlAlterar.setTimestamp(51, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setBoolean(52, obj.getCoordenador());

				sqlAlterar.setBoolean(53, obj.getIngles());
				sqlAlterar.setBoolean(54, obj.getEspanhol());
				sqlAlterar.setBoolean(55, obj.getFrances());
				sqlAlterar.setString(56, obj.getInglesNivel());
				sqlAlterar.setString(57, obj.getEspanholNivel());
				sqlAlterar.setString(58, obj.getFrancesNivel());
				sqlAlterar.setString(59, obj.getOutrosIdiomas());
				sqlAlterar.setString(60, obj.getOutrosIdiomasNivel());

				sqlAlterar.setBoolean(61, obj.getWindows());
				sqlAlterar.setBoolean(62, obj.getWord());
				sqlAlterar.setBoolean(63, obj.getExcel());
				sqlAlterar.setBoolean(64, obj.getAccess());
				sqlAlterar.setBoolean(65, obj.getPowerPoint());
				sqlAlterar.setBoolean(66, obj.getInternet());
				sqlAlterar.setBoolean(67, obj.getSap());
				sqlAlterar.setBoolean(68, obj.getCorelDraw());
				sqlAlterar.setBoolean(69, obj.getAutoCad());
				sqlAlterar.setBoolean(70, obj.getPhotoshop());
				sqlAlterar.setBoolean(71, obj.getMicrosiga());
				sqlAlterar.setString(72, obj.getOutrosSoftwares());
				sqlAlterar.setInt(73, obj.getQtdFilhos());
				sqlAlterar.setBoolean(74, obj.getParticipaBancoCurriculum().booleanValue());
				sqlAlterar.setBoolean(75, obj.getInformacoesVerdadeiras().booleanValue());
				sqlAlterar.setBoolean(76, obj.getDivulgarMeusDados().booleanValue());
				sqlAlterar.setString(77, obj.getCertidaoNascimento());
				sqlAlterar.setString(78, obj.getInformacoesAdicionais());
				sqlAlterar.setBoolean(79, obj.getGerenciaPreInscricao());
				sqlAlterar.setBoolean(80, obj.getCurriculoAtualizado());
				sqlAlterar.setString(81, obj.getPispasep());
				sqlAlterar.setString(82, obj.getComplementoFiador());
				sqlAlterar.setString(83, obj.getNumeroEndFiador());
				sqlAlterar.setString(84, obj.getSetorFiador());
				sqlAlterar.setString(85, obj.getCepFiador());
				if (obj.getCidadeFiador().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(86, obj.getCidadeFiador().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(86, 0);
				}
				sqlAlterar.setBoolean(87, obj.getPossuiAcessoVisaoPais());
				sqlAlterar.setBoolean(88, obj.getIsentarTaxaBoleto());
				sqlAlterar.setBoolean(89, obj.getGravida());
				sqlAlterar.setBoolean(90, obj.getCanhoto());
				sqlAlterar.setBoolean(91, obj.getPortadorNecessidadeEspecial());

				if (obj.getDataExpedicaoTituloEleitoral() != null) {
					sqlAlterar.setTimestamp(92, Uteis.getDataJDBCTimestamp(obj.getDataExpedicaoTituloEleitoral()));
				} else {
					sqlAlterar.setNull(92, 0);
				}

				sqlAlterar.setString(93, obj.getZonaEleitoral());

				if (obj.getDataExpedicaoCertificadoMilitar() != null) {
					sqlAlterar.setTimestamp(94, Uteis.getDataJDBCTimestamp(obj.getDataExpedicaoCertificadoMilitar()));
				} else {
					sqlAlterar.setNull(94, 0);
				}

				sqlAlterar.setString(95, obj.getOrgaoExpedidorCertificadoMilitar());
				if (obj.getSituacaoMilitar() != null) {
					sqlAlterar.setString(96, obj.getSituacaoMilitar().name());
				} else {
					sqlAlterar.setNull(96, 0);
				}
				sqlAlterar.setString(97, obj.getNomeCenso());
				sqlAlterar.setString(98, obj.getGrauParentesco());
				sqlAlterar.setBoolean(99, obj.isPermiteEnviarRemessa());
				if (Uteis.isAtributoPreenchido(obj.getSenhaCertificadoParaDocumento())) {
					sqlAlterar.setString(100, obj.getSenhaCertificadoParaDocumento());
				} else {
					sqlAlterar.setNull(100, 0);
				}
				sqlAlterar.setString(101, obj.getEstadoCivilFiador());
				sqlAlterar.setString(102, obj.getRgFiador());
				sqlAlterar.setString(103, obj.getProfissaoFiador());
				if (Uteis.isAtributoPreenchido(obj.getPaisFiador())) {
					sqlAlterar.setInt(104, obj.getPaisFiador().getCodigo());
				} else {
					sqlAlterar.setNull(104, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getDataNascimentoFiador())) {
					sqlAlterar.setDate(105, Uteis.getDataJDBC(obj.getDataNascimentoFiador()));
				} else {
					sqlAlterar.setNull(105, 0);
				}
				sqlAlterar.setString(106, obj.getSecaoZonaEleitoral());

				if(obj.getTipoMidiaCaptacao() != null && obj.getTipoMidiaCaptacao().getCodigo() != null && obj.getTipoMidiaCaptacao().getCodigo() > 0) {
					sqlAlterar.setInt(107, obj.getTipoMidiaCaptacao().getCodigo());
				} else {
					sqlAlterar.setNull(107, 0);
				}
				if(Uteis.isAtributoPreenchido(obj.getNomeBatismo())) {
					sqlAlterar.setString(108, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNomeBatismo()));
				}else {
					sqlAlterar.setString(108, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNome()));
				}
				
				if(Uteis.isAtributoPreenchido(obj.getBanco())) {
					sqlAlterar.setString(109, obj.getBanco());
				} else {
					sqlAlterar.setNull(109, 0);
				}
				
				if(Uteis.isAtributoPreenchido(obj.getAgencia())) {
					sqlAlterar.setString(110, obj.getAgencia());
				} else {
					sqlAlterar.setNull(110, 0);
				}

				if(Uteis.isAtributoPreenchido(obj.getContaCorrente())) {
					sqlAlterar.setString(111, obj.getContaCorrente());
				} else {
					sqlAlterar.setNull(111, 0);
				}
				sqlAlterar.setString(112, obj.getRegistroAcademico());
				sqlAlterar.setBoolean(113, obj.getSabatista().booleanValue());
				sqlAlterar.setString(114, obj.getUniversidadeParceira());
				sqlAlterar.setString(115, obj.getModalidadeBolsa().name());
				sqlAlterar.setDouble(116, obj.getValorBolsa());
				sqlAlterar.setString(117, obj.getTipoAssinaturaDocumentoEnum().name());
				sqlAlterar.setBoolean(118, obj.getTempoEstendidoProva());
				sqlAlterar.setBoolean(119, obj.getRenovacaoAutomatica());
				sqlAlterar.setString(120, obj.getTranstornosNeurodivergentes());
				sqlAlterar.setInt(121, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) <= 0) {
			incluir(obj, usuario, configuracaoGeralSistema, false);
			return;
		}

		if (!editadoPorAluno) {
			// Alterando Formaï¿½ï¿½o Acadï¿½mica
			getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);
            if(!obj.getDadosComerciaisVOs().isEmpty()) {            	
            	getFacadeFactory().getDadosComerciaisFacade().setIdEntidade(this.getIdEntidade());
            	getFacadeFactory().getDadosComerciaisFacade().alterarDadosComerciais(obj, obj.getDadosComerciaisVOs(), verificarAcesso);
            }
//            if(!obj.getAreaProfissionalInteresseContratacaoVOs().isEmpty()) {         
//			 getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade(this.getIdEntidade());
//			 getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().alterarAreaProfissionalInteresseContratacao(obj.getCodigo(), obj.getAreaProfissionalInteresseContratacaoVOs(), usuario);
//            }
//            if(!obj.getFormacaoExtraCurricularVOs().isEmpty()) {
//			  getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade(this.getIdEntidade());
//			  getFacadeFactory().getFormacaoExtraCurricularFacade().alterarFormacaoExtraCurricular(obj, obj.getFormacaoExtraCurricularVOs(), verificarAcesso);
//		    }
		}
		if (obj.getProfessor() || obj.getFuncionario()) {
			getFacadeFactory().getDocumetacaoPessoaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getDocumetacaoPessoaFacade().incluirDocumetacaoPessoas(obj, obj.getCodigo(), obj.getDocumetacaoPessoaVOs(), usuario, configuracaoGeralSistema);
		}
		if (obj.getProfessor()) {

			getFacadeFactory().getDisciplinasInteresseFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getDisciplinasInteresseFacade().alterarDisciplinasInteresses(obj.getCodigo(), obj.getDisciplinasInteresseVOs(), verificarAcesso, usuario);

			getFacadeFactory().getHorarioProfessorFacade().setIdEntidade(this.getIdEntidade());
			// getFacadeFactory().getHorarioProfessorFacade().incluirHorarioProfessor(obj.getCodigo(),
			// obj.getHorarioProfessorVOs());
			getFacadeFactory().getHorarioProfessorFacade().executarAtualizarHorariosProfessor(obj, null, null);
			// getFacadeFactory().getHorarioProfessorFacade().(obj.getCodigo(),
			// obj.getHorarioProfessorVOs());
		}
		if (obj.getGerenciaPreInscricao().equals(Boolean.TRUE)) {
			getFacadeFactory().getPessoaPreInscricaoCursoFacade().alterarPessoaPreInscricaoCursos(obj.getCodigo(), obj.getPessoaPreInscricaoCursoVOs(), verificarAcesso);
		}

		getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getFiliacaoFacade().alterarFiliacaos(obj, obj.getFiliacaoVOs(), deveValidarCPF, configuracaoGeralSistema, usuario);		

		if (obj.getIncluirProspect()) {
			realizarVinculoPessoaProspect(obj, usuario);
		}
		if (obj.getGerarNumeroCPF()) {
			executarGeracaoNumeroCPF(obj);
		}
		getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(obj, false, usuario);
		getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
		getFacadeFactory().getUsuarioFacade().alterarNomeUsuario(obj.getCodigo(), obj.getNome(), usuario);
	}

	public void alterarFoto(final PessoaVO obj, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (obj.getArquivoImagem().getPastaBaseArquivoEnum() != null) {
			if (obj.getArquivoImagem().getCodigo() == 0) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			} else {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			}
		}
		final String sql = "UPDATE Pessoa set  arquivoImagem=? "
				+ " WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getArquivoImagem().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) <= 0) {
			return;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarSemFiliacao(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		Pessoa.alterar(getIdEntidade(), verificarAcesso, usuario);
//		PessoaVO.validarDados(obj, getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, usuario), false);
		if (obj.getArquivoImagem().getPastaBaseArquivoEnum() != null) {
			if (obj.getArquivoImagem().getCodigo() == 0) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			} else {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			}
		}
		if (!obj.getPossuiFilho()) {
			obj.setQtdFilhos(0);
		}
		if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
			obj.setNome(obj.getNome().toUpperCase());
		}
		registrarLogAtualizacaoCadastral(obj, usuario);
		final String sql = "UPDATE Pessoa set nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, " + "cidade=?, sexo=?, estadoCivil=?, telefoneComer=?, " + "telefoneRes=?, telefoneRecado=?, celular=?, email=?, dataNasc=?, naturalidade=?, nacionalidade=?, CPF=?, RG=?, " + "certificadoMilitar=?, " + "dataEmissaoRG=?, estadoEmissaoRG=?, orgaoEmissor=?, tituloEleitoral=?, necessidadesEspeciais=?, funcionario = ?, professor = ?, aluno=?, " + "candidato=?, " + "membroComunidade = ?, paginaPessoal = ?, valorCssTopoLogo=?, valorCssBackground=?, valorCssMenu=?, perfilEconomico=?, " + "atuaComoDocente=?, ativo=?, idalunoinep=?, passaporte=?, corraca=?, deficiencia=?, nomeFiador=?, enderecoFiador=?, telefoneFiador=?, " + "cpfFiador=?, tipoNecessidadesEspeciais=?, celularFiador=?, arquivoImagem=?, " + " email2=?,reponsavelUltimaAlteracao=?, dataUltimaAlteracao=?, coordenador=?,"
				+ "ingles=?, espanhol=?, frances=?, inglesNivel=?, espanholNivel=?, francesNivel=?, outrosIdiomas=?, outrosIdiomasNivel=?, " + "windows=?, word=?, excel=?, access=?, powerPoint=?, internet=?, sap=?, corelDraw=?, autoCad=?, photoshop=?, microsiga=?, outrosSoftwares=?, " + "qtdFilhos=?, participabancocurriculum=?, informacoesverdadeiras=?, divulgarmeusdados=?, certidaoNascimento=?, informacoesAdicionais=?, " + "gerenciaPreInscricao=?, curriculoAtualizado=?, pispasep=?, complementofiador=?, numeroendfiador=?, setorfiador=?, cepFiador=?, cidadeFiador=?, " + "possuiAcessoVisaoPais=?, isentarTaxaBoleto = ?, situacaoMilitar=?, dadospessoaisatualizado=? , dataUltimaAtualizacaoCadatral=?, dataExpedicaoTituloEleitoral=?, zonaEleitoral=?, dataExpedicaoCertificadoMilitar=?, orgaoExpedidorCertificadoMilitar=?, senhacertificadoparadocumento=?, nomeBatismo=? , registroAcademico=?, sabatista=?, transtornosNeurodivergentes=?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
				sqlAlterar.setString(2, obj.getEndereco());
				sqlAlterar.setString(3, obj.getSetor());
				sqlAlterar.setString(4, obj.getNumero());
				sqlAlterar.setString(5, obj.getCEP());
				sqlAlterar.setString(6, obj.getComplemento());
				if (obj.getCidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(7, 0);
				}
				sqlAlterar.setString(8, obj.getSexo());
				sqlAlterar.setString(9, obj.getEstadoCivil());
				sqlAlterar.setString(10, obj.getTelefoneComer());
				sqlAlterar.setString(11, obj.getTelefoneRes());
				sqlAlterar.setString(12, obj.getTelefoneRecado());
				sqlAlterar.setString(13, obj.getCelular());
				sqlAlterar.setString(14, obj.getEmail().trim());
				if (obj.getDataNasc() != null) {
					sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataNasc()));
				} else {
					sqlAlterar.setNull(15, 0);
				}
				if (obj.getNaturalidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(16, obj.getNaturalidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(16, 0);
				}
				if (obj.getNacionalidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(17, obj.getNacionalidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(17, 0);
				}
				sqlAlterar.setString(18, obj.getCPF());
				sqlAlterar.setString(19, obj.getRG());
				sqlAlterar.setString(20, obj.getCertificadoMilitar());
				sqlAlterar.setDate(21, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
				sqlAlterar.setString(22, obj.getEstadoEmissaoRG());
				sqlAlterar.setString(23, obj.getOrgaoEmissor());
				sqlAlterar.setString(24, obj.getTituloEleitoral());
				sqlAlterar.setString(25, obj.getNecessidadesEspeciais());
				sqlAlterar.setBoolean(26, obj.getFuncionario().booleanValue());
				sqlAlterar.setBoolean(27, obj.getProfessor().booleanValue());
				sqlAlterar.setBoolean(28, obj.getAluno().booleanValue());
				sqlAlterar.setBoolean(29, obj.getCandidato().booleanValue());
				sqlAlterar.setBoolean(30, obj.getMembroComunidade().booleanValue());
				sqlAlterar.setString(31, obj.getPaginaPessoal());
				sqlAlterar.setString(32, obj.getValorCssTopoLogo());
				sqlAlterar.setString(33, obj.getValorCssBackground());
				sqlAlterar.setString(34, obj.getValorCssMenu());

				if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(35, obj.getPerfilEconomico().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(35, 0);
				}
				sqlAlterar.setString(36, obj.getAtuaComoDocente());
				sqlAlterar.setBoolean(37, obj.getAtivo().booleanValue());
				if (!obj.getIdAlunoInep().equals("")) {
					sqlAlterar.setString(38, obj.getIdAlunoInep());
				} else {
					sqlAlterar.setNull(38, 0);
				}
				sqlAlterar.setString(39, obj.getPassaporte());
				sqlAlterar.setString(40, obj.getCorRaca());
				sqlAlterar.setString(41, obj.getDeficiencia());
				sqlAlterar.setString(42, obj.getNomeFiador());
				sqlAlterar.setString(43, obj.getEnderecoFiador());
				sqlAlterar.setString(44, obj.getTelefoneFiador());
				sqlAlterar.setString(45, obj.getCpfFiador());
				sqlAlterar.setString(46, obj.getTipoNecessidadesEspeciais());
				sqlAlterar.setString(47, obj.getCelularFiador());
				if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(48, obj.getArquivoImagem().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(48, 0);
				}
				// sqlAlterar.setString(49, obj.getNomeEmpresa());
				// sqlAlterar.setString(50, obj.getEnderecoEmpresa());
				// sqlAlterar.setString(51, obj.getCargoPessoaEmpresa());
				// sqlAlterar.setString(52, obj.getCepEmpresa());
				// sqlAlterar.setString(53, obj.getComplementoEmpresa());
				// if (obj.getCidadeEmpresa().getCodigo() != 0) {
				// sqlAlterar.setInt(54,
				// obj.getCidadeEmpresa().getCodigo().intValue());
				// } else {
				// sqlAlterar.setNull(54, 0);
				// }
				// sqlAlterar.setString(55, obj.getSetorEmpresa());
				sqlAlterar.setString(49, obj.getEmail2().trim());
				if (usuario != null && usuario.getCodigo() != 0) {
					sqlAlterar.setInt(50, usuario.getCodigo().intValue());
				} else {
					sqlAlterar.setNull(50, 0);
				}
				sqlAlterar.setTimestamp(51, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setBoolean(52, obj.getCoordenador());

				sqlAlterar.setBoolean(53, obj.getIngles());
				sqlAlterar.setBoolean(54, obj.getEspanhol());
				sqlAlterar.setBoolean(55, obj.getFrances());
				sqlAlterar.setString(56, obj.getInglesNivel());
				sqlAlterar.setString(57, obj.getEspanholNivel());
				sqlAlterar.setString(58, obj.getFrancesNivel());
				sqlAlterar.setString(59, obj.getOutrosIdiomas());
				sqlAlterar.setString(60, obj.getOutrosIdiomasNivel());

				sqlAlterar.setBoolean(61, obj.getWindows());
				sqlAlterar.setBoolean(62, obj.getWord());
				sqlAlterar.setBoolean(63, obj.getExcel());
				sqlAlterar.setBoolean(64, obj.getAccess());
				sqlAlterar.setBoolean(65, obj.getPowerPoint());
				sqlAlterar.setBoolean(66, obj.getInternet());
				sqlAlterar.setBoolean(67, obj.getSap());
				sqlAlterar.setBoolean(68, obj.getCorelDraw());
				sqlAlterar.setBoolean(69, obj.getAutoCad());
				sqlAlterar.setBoolean(70, obj.getPhotoshop());
				sqlAlterar.setBoolean(71, obj.getMicrosiga());
				sqlAlterar.setString(72, obj.getOutrosSoftwares());
				sqlAlterar.setInt(73, obj.getQtdFilhos());
				sqlAlterar.setBoolean(74, obj.getParticipaBancoCurriculum().booleanValue());
				sqlAlterar.setBoolean(75, obj.getInformacoesVerdadeiras().booleanValue());
				sqlAlterar.setBoolean(76, obj.getDivulgarMeusDados().booleanValue());
				sqlAlterar.setString(77, obj.getCertidaoNascimento());
				sqlAlterar.setString(78, obj.getInformacoesAdicionais());
				sqlAlterar.setBoolean(79, obj.getGerenciaPreInscricao());
				sqlAlterar.setBoolean(80, obj.getCurriculoAtualizado());
				sqlAlterar.setString(81, obj.getPispasep());
				sqlAlterar.setString(82, obj.getComplementoFiador());
				sqlAlterar.setString(83, obj.getNumeroEndFiador());
				sqlAlterar.setString(84, obj.getSetorFiador());
				sqlAlterar.setString(85, obj.getCepFiador());
				if (obj.getCidadeFiador().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(86, obj.getCidadeFiador().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(86, 0);
				}
				sqlAlterar.setBoolean(87, obj.getPossuiAcessoVisaoPais());
				sqlAlterar.setBoolean(88, obj.getIsentarTaxaBoleto());
				if (obj.getSituacaoMilitar() != null) {
					sqlAlterar.setString(89, obj.getSituacaoMilitar().name());
				} else {
					sqlAlterar.setNull(89, 0);
				}
				sqlAlterar.setBoolean(90, obj.getDadosPessoaisAtualizado());
				if (obj.getDataUltimaAtualizacaoCadatral() != null) {
					sqlAlterar.setTimestamp(91, Uteis.getDataJDBCTimestamp(obj.getDataUltimaAtualizacaoCadatral()));
				} else {
					sqlAlterar.setNull(91, 0);
				}
				if (obj.getDataExpedicaoTituloEleitoral() != null) {
					sqlAlterar.setTimestamp(92, Uteis.getDataJDBCTimestamp(obj.getDataExpedicaoTituloEleitoral()));
				} else {
					sqlAlterar.setNull(92, 0);
				}
				sqlAlterar.setString(93, obj.getZonaEleitoral());
				if (obj.getDataExpedicaoCertificadoMilitar() != null) {
					sqlAlterar.setTimestamp(94, Uteis.getDataJDBCTimestamp(obj.getDataExpedicaoCertificadoMilitar()));
				} else {
					sqlAlterar.setNull(94, 0);
				}
				sqlAlterar.setString(95, obj.getOrgaoExpedidorCertificadoMilitar());
				if (Uteis.isAtributoPreenchido(obj.getSenhaCertificadoParaDocumento())) {
					sqlAlterar.setString(96, obj.getSenhaCertificadoParaDocumento());
				} else {
					sqlAlterar.setNull(96, 0);
				}
				sqlAlterar.setString(97, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
				sqlAlterar.setString(98, obj.getRegistroAcademico());
				sqlAlterar.setBoolean(99, obj.getSabatista().booleanValue());
				sqlAlterar.setString(100, obj.getTranstornosNeurodivergentes());
				sqlAlterar.setInt(101, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		}) <= 0) {
			incluir(obj, usuario, configuracaoGeralSistema, false);
			return;
		}

		if (!editadoPorAluno) {
			// Alterando Formaï¿½ï¿½o Acadï¿½mica
			getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);

			getFacadeFactory().getDadosComerciaisFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getDadosComerciaisFacade().alterarDadosComerciais(obj, obj.getDadosComerciaisVOs(), verificarAcesso);

//			getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade(this.getIdEntidade());
//			getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().alterarAreaProfissionalInteresseContratacao(obj.getCodigo(), obj.getAreaProfissionalInteresseContratacaoVOs(), usuario);

//			getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade(this.getIdEntidade());
//			getFacadeFactory().getFormacaoExtraCurricularFacade().alterarFormacaoExtraCurricular(obj, obj.getFormacaoExtraCurricularVOs(), verificarAcesso);
		}
		if (obj.getProfessor().equals(Boolean.TRUE)) {
			getFacadeFactory().getDocumetacaoPessoaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getDocumetacaoPessoaFacade().alterarDocumetacaoPessoas(obj, obj.getCodigo(), obj.getDocumetacaoPessoaVOs(), usuario, configuracaoGeralSistema);

			getFacadeFactory().getDisciplinasInteresseFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getDisciplinasInteresseFacade().alterarDisciplinasInteresses(obj.getCodigo(), obj.getDisciplinasInteresseVOs(), verificarAcesso, usuario);

			getFacadeFactory().getHorarioProfessorFacade().setIdEntidade(this.getIdEntidade());
			// getFacadeFactory().getHorarioProfessorFacade().incluirHorarioProfessor(obj.getCodigo(),
			// obj.getHorarioProfessorVOs());
			getFacadeFactory().getHorarioProfessorFacade().executarAtualizarHorariosProfessor(obj, null, null);
			// getFacadeFactory().getHorarioProfessorFacade().(obj.getCodigo(),
			// obj.getHorarioProfessorVOs());
		}
		if (obj.getGerenciaPreInscricao().equals(Boolean.TRUE)) {
			getFacadeFactory().getPessoaPreInscricaoCursoFacade().alterarPessoaPreInscricaoCursos(obj.getCodigo(), obj.getPessoaPreInscricaoCursoVOs(), verificarAcesso);
		}
		if (obj.getIncluirProspect()) {
			realizarVinculoPessoaProspect(obj, usuario);
		}
		// O código abaixo foi adicionado para alterar filiação apenas qunado a chamado do metodo ocorrer da tela minhas notas, na visão do aluno.
		if (configuracaoGeralSistema.getConfiguracaoAtualizacaoCadastralVO().getHabilitarRecursoAtualizacaoCadastral().booleanValue() &&
				configuracaoGeralSistema.getConfiguracaoAtualizacaoCadastralVO().getApresentarCampoFiliacao().booleanValue()) {
			getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getFiliacaoFacade().alterarFiliacaos(obj, obj.getFiliacaoVOs(), true, configuracaoGeralSistema, usuario);
		}
		getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
		getFacadeFactory().getCurriculumPessoaFacade().incluirCurriculumPessoa(obj, configuracaoGeralSistema, usuario);
		for (CurriculumPessoaVO curriculumPessoaVO : obj.getCurriculumPessoaExcluir()) {
			getFacadeFactory().getCurriculumPessoaFacade().excluir(curriculumPessoaVO, configuracaoGeralSistema, usuario);
		}		
		getFacadeFactory().getUsuarioFacade().alterarNomeUsuario(obj.getCodigo(), obj.getNome(), usuario);
		realizarAtualizacaoCadastralAlunoLDAPeBlackboard(obj, usuario);
	}

	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
	// public void alterarFoto(final Integer codigoPessoa, final byte foto[])
	// throws Exception {
	// try {
	// final String sql = "UPDATE Pessoa set foto=? WHERE ((codigo = ?))";
	// getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
	//
	// public PreparedStatement createPreparedStatement(Connection arg0) throws
	// SQLException {
	// PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
	// sqlAlterar.setBytes(1, foto);
	// sqlAlterar.setInt(2, codigoPessoa.intValue());
	// return sqlAlterar;
	// }
	// });
	// } catch (Exception e) {
	// throw e;
	// }
	// }
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarTipoPessoa(final Integer pessoa, final Boolean funcionario, final Boolean professor, final Boolean aluno, final Boolean candidato, final Boolean membroComunidade, final UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE Pessoa set funcionario = ?, professor = ?, aluno=?,  membroComunidade = ? ,reponsavelUltimaAlteracao=?, dataUltimaAlteracao=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, funcionario.booleanValue());
				sqlAlterar.setBoolean(2, professor.booleanValue());
				sqlAlterar.setBoolean(3, aluno.booleanValue());
				sqlAlterar.setBoolean(4, membroComunidade.booleanValue());
				if(Uteis.isAtributoPreenchido(usuarioVO)){
					sqlAlterar.setInt(5, usuarioVO.getCodigo());
				}else{
					sqlAlterar.setNull(5, 0);
				}
				sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setInt(7, pessoa.intValue());
				return sqlAlterar;

			}
		});
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void registrarLogAtualizacaoCadastral(final PessoaVO pessoa, final UsuarioVO usuarioVO) throws Exception {
		if (pessoa.getRegistrarLogAtualizacaoCadastral().booleanValue()) {
			PessoaVO pessoaAntiga = new PessoaVO();
			pessoaAntiga.setCodigo(pessoa.getCodigo());
			getFacadeFactory().getPessoaFacade().carregarDados(pessoaAntiga, usuarioVO);
			final String log = pessoa.obterStringLogAlteracaoCadastral(pessoaAntiga);

			final String sql = "INSERT INTO PessoaLogAtualizacaoCadastral( dataAlteracao, usuario, alteracao) " + "VALUES (?, ?, ?) returning codigo ";

			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(2, usuarioVO.getCodigo());
					sqlInserir.setString(3, log);
					return sqlInserir;
				}
			}, new ResultSetExtractor() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					return null;
				}
			});
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void resetarDataAtualizacaoCadastralGeral(UsuarioVO usuarioLogado) throws Exception {
		final String sql = "UPDATE Pessoa set atualizardatacadastral=true,dataUltimaAtualizacaoCadatral=null where dataUltimaAtualizacaoCadatral is not null;" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);;
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				return sqlAlterar;

			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarCss(final Integer codigoPessoa, final String valorCssTopoLogo, final String valorCssBackground, final String valorCssMenu) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set valorCssTopoLogo=?, valorCssBackground=?, valorCssMenu=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, valorCssTopoLogo);
					sqlAlterar.setString(2, valorCssBackground);
					sqlAlterar.setString(3, valorCssMenu);
					sqlAlterar.setInt(4, codigoPessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarPessoaRequisitante(final Integer codigoPessoa) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set requisitante=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, true);
					sqlAlterar.setInt(2, codigoPessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarCoordenador(final Integer codigoPessoa, final Boolean coordenador) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set coordenador=? WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, coordenador);
					sqlAlterar.setInt(2, codigoPessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarCodProspect(final Integer codigoPessoa, final Integer codigoProspect, final Boolean coordenador) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set codProspect=? WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (codigoProspect != null) {
						sqlAlterar.setInt(1, codigoProspect.intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, codigoPessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void alterarIsencaoTaxaBoleto(final Integer codigoPessoa, final Boolean isentarTaxaBoleto) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set isentarTaxaBoleto=? WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, isentarTaxaBoleto);
					sqlAlterar.setInt(2, codigoPessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarCandidato(final Integer codigoPessoa, final Boolean candidato) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set candidato=? WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, candidato);
					sqlAlterar.setInt(2, codigoPessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public void executarVerificacaoPessoaVinculadoAProgramacaoAula(Integer pessoa) throws Exception {
		if (getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoProfessorLecionaAlgumaDisciplina(pessoa)) {
			throw new Exception("Este professor Não pode ser excluido, ele está vinculado a programação de aula.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirConexaoInicializada(PessoaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCodigo() > 0) {
			executarVerificacaoPessoaVinculadoAProgramacaoAula(obj.getCodigo());
		}
		String sql = "DELETE FROM Pessoa WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

		getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getFormacaoAcademicaFacade().excluirFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), usuario);

		getFacadeFactory().getPessoaPreInscricaoCursoFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getPessoaPreInscricaoCursoFacade().excluirPessoaPreInscricaoCursos(obj.getCodigo(), obj.getPessoaPreInscricaoCursoVOs());

		getFacadeFactory().getDadosComerciaisFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getDadosComerciaisFacade().excluirDadosComerciais(obj, obj.getDadosComerciaisVOs());

		getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().excluirAreaProfissionalInteresseContratacao(obj.getCodigo(), obj.getAreaProfissionalInteresseContratacaoVOs(), true);

		getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getFormacaoExtraCurricularFacade().excluirFormacaoExtraCurricular(obj, obj.getFormacaoExtraCurricularVOs());

		getFacadeFactory().getDisciplinasInteresseFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getDisciplinasInteresseFacade().excluirDisciplinasInteresses(obj.getCodigo(), usuario);

		getFacadeFactory().getDocumetacaoPessoaFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getDocumetacaoPessoaFacade().excluirDocumetacaoPessoas(obj.getCodigo(), usuario);

		getFacadeFactory().getHorarioProfessorFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getHorarioProfessorFacade().excluirHorarioProfessor(obj.getCodigo(), usuario);

		getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
		getFacadeFactory().getFiliacaoFacade().excluirFiliacaos(obj.getCodigo(), usuario);

	}

	/**
	 * Operaï¿½ï¿½o Responsável por excluir no BD um objeto da classe
	 * <code>PessoaVO</code>. Sempre localiza o registro a ser excluï¿½do atravï¿½s
	 * da chave primï¿½ria da entidade. Primeiramente verifica a conexï¿½o com o
	 * banco de dados e a permissï¿½o do usuï¿½rio para realizar esta operacï¿½o na
	 * entidade. Isto, atravï¿½s da operaï¿½ï¿½o <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>PessoaVO</code> que serï¿½ removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(PessoaVO obj, UsuarioVO usuario) throws Exception {
		try {
			Pessoa.excluir(getIdEntidade(), true, usuario);
			excluirConexaoInicializada(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public PessoaVO consultarAlunoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT pessoa.* FROM Matricula ,Pessoa WHERE lower (matricula.matricula) =( '" + valorConsulta.toLowerCase() + "') and pessoa.codigo = matricula.aluno ORDER BY matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));

	}

	public Boolean verificarPessoaRequisitante(Integer pessoaCodigo) throws Exception {
		String sqlStr = "SELECT requisitante FROM Pessoa WHERE  pessoa.codigo = " + pessoaCodigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("requisitante");
		}
		return false;
	}

	public PessoaVO consultarFuncionarioPorMatricula(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT pessoa.* FROM Funcionario ,Pessoa WHERE lower (funcionario.matricula) =( '" + valorConsulta.toLowerCase() + "') and pessoa.codigo=funcionario.pessoa ORDER BY funcionario.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));

	}

	public PessoaVO consultarPessoaPorCodigoFuncionario(Integer codigoFuncionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		StringBuilder sql = new StringBuilder("SELECT pessoa.* FROM pessoa ");
		sql.append("INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
		sql.append(" WHERE funcionario.codigo = " + codigoFuncionario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));

	}

	public PessoaVO consultarPessoaPorNomeDataNascNomeMae(String nomePessoa, String nomeMae, Date dataNasc, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT ").append(getGroupBy("Pessoa"));
		sqlStr.append(" FROM Pessoa ");
		sqlStr.append(" INNER JOIN filiacao ON filiacao.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN pessoa as pais ON filiacao.pais = pais.codigo ");
		sqlStr.append(" WHERE Pessoa.nome ilike (?) ");
		sqlStr.append(" and Pessoa.dataNasc = '").append(Uteis.getDataJDBC(dataNasc)).append("' ");
		sqlStr.append(" and lower pais.nome ilike (?) and Filiacao.tipo = 'MA' ");
		sqlStr.append(" group by ").append(getGroupBy("Pessoa"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { "%" + nomePessoa + "%", "%" + nomeMae + "%" });
		if (!tabelaResultado.next()) {
			return new PessoaVO();
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));

	}

	public List consultarAlunoPorMatriculaLista(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT matricula.*, pessoa.* FROM Matricula ,Pessoa WHERE lower (matricula.matricula) =( '" + valorConsulta.toLowerCase() + "') and pessoa.codigo=matricula.aluno ORDER BY matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String necessidadesEspeciais</code>. Retorna os
	 * objetos, com inï¿½cio do valor do atributo idï¿½ntico ao parï¿½metro fornecido.
	 * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List<PessoaVO> consultarPorNecessidadesEspeciais(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE lower (necessidadesEspeciais) like(?) ";
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("PR")) {
				sqlStr += "and professor = 'true'";
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr += "and funcionario = 'true'";
			}
			if (tipoPessoa.equals("CA")) {
				sqlStr += "and candidato = 'true'";
			}
			if (tipoPessoa.equals("AL")) {
				sqlStr += "and aluno = 'true'";
			}
			if (tipoPessoa.equals("MC")) {
				sqlStr += "and membroComunidade = 'true'";
			}
		}
		sqlStr += " ORDER BY necessidadesEspeciais";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String RG</code>. Retorna os objetos, com inï¿½cio
	 * do valor do atributo idï¿½ntico ao parï¿½metro fornecido. Faz uso da operaï¿½ï¿½o
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultaRapidaPorRG(valorConsulta, tipoPessoa, controlarAcesso, nivelMontarDados, usuario);
	}

	// public List consultarPorRG(String valorConsulta, String tipoPessoa,
	// boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws
	// Exception {
	// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr = "SELECT * FROM Pessoa WHERE lower (RG) like(?) ";
	// if (!tipoPessoa.equalsIgnoreCase("")) {
	// if (tipoPessoa.equals("PR")) {
	// sqlStr += "and professor = 'true'";
	// }
	// if (tipoPessoa.equals("FU")) {
	// sqlStr += "and funcionario = 'true'";
	// }
	// if (tipoPessoa.equals("CA")) {
	// sqlStr += "and candidato = 'true'";
	// }
	// if (tipoPessoa.equals("AL")) {
	// sqlStr += "and aluno = 'true'";
	// }
	// if (tipoPessoa.equals("MC")) {
	// sqlStr += "and membroComunidade = 'true'";
	// }
	// }
	// sqlStr += " ORDER BY RG";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new
	// Object[]{valorConsulta.toLowerCase() + "%"});
	// return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	// }
	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String CPF</code> . Retorna os objetos, com
	 * inï¿½cio do valor do atributo idï¿½ntico ao parï¿½metro fornecido. Faz uso da
	 * operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PessoaVO> consultarPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultaRapidaPorCPF(valorConsulta, tipoPessoa, controlarAcesso, nivelMontarDados, usuario);
	}

	// @Transactional(readOnly = false, rollbackFor = {Throwable.class},
	// propagation = Propagation.SUPPORTS)
	// public List consultarPorCPF(String valorConsulta, String tipoPessoa,
	// boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws
	// Exception {
	// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr =
	// "SELECT * FROM Pessoa WHERE replace(replace(CPF,'.',''),'-','') like('" +
	// Uteis.retirarMascaraCPF(valorConsulta) + "%') ";
	// if (!tipoPessoa.equalsIgnoreCase("")) {
	// if (tipoPessoa.equals("PR")) {
	// sqlStr += "and professor = 'true'";
	// }
	// if (tipoPessoa.equals("FU")) {
	// sqlStr += "and funcionario = 'true'";
	// }
	// if (tipoPessoa.equals("CA")) {
	// sqlStr += "and candidato = 'true'";
	// }
	// if (tipoPessoa.equals("AL")) {
	// sqlStr += "and aluno = 'true'";
	// }
	// if (tipoPessoa.equals("MC")) {
	// sqlStr += "and membroComunidade = 'true'";
	// }
	// }
	// sqlStr += " ORDER BY CPF";
	//
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	// }
	public PessoaVO consultarPorCPFUnico(String valorConsulta, Integer pessoa, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer("SELECT * FROM Pessoa ");
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append(" WHERE trim(REPLACE(REPLACE(REPLACE(CPF, '.', ''), '-', ''), ' ', '')) = ? ");
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" and professor = 'true'");
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr.append(" and funcionario = 'true'");
			}
			if (tipoPessoa.equals("CA")) {
				sqlStr.append(" and candidato = 'true'");
			}
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" and aluno = 'true'");
			}
			if (tipoPessoa.equals("MC")) {
				sqlStr.append(" and membroComunidade = 'true'");
			}
		}
		sqlStr.append(" and cpf <> '' and cpf is not null ");
		if (Uteis.isAtributoPreenchido(pessoa)) {
			sqlStr.append(" and codigo <> ").append(pessoa);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.trim().replace(" ", "")));
		if (!tabelaResultado.next()) {
			return new PessoaVO();
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public PessoaVO consultarPorTipoPessoa(String tipoPessoa, PessoaVO pessoa) throws Exception {
		StringBuffer sqlStr = new StringBuffer("select pessoa.codigo, (select case when matricula.aluno is not null then true else false end from matricula where matricula.aluno = pessoa.codigo limit 1) as aluno, ");
		sqlStr.append(" (select case when funcionario.codigo is not null then true else false end from funcionario inner join horarioturmadiaitem  on horarioturmadiaitem.professor = funcionario.codigo ");
		sqlStr.append(" where funcionario.pessoa = pessoa.codigo limit 1) as professor, (select case when funcionario.codigo is not null then true else false end from funcionario where funcionario.pessoa = pessoa.codigo limit 1) as funcionario, ");
		sqlStr.append(" (select case when cursocoordenador.codigo is not null then true else false end from cursocoordenador inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" where funcionario.pessoa = pessoa.codigo limit 1) as coordenador from pessoa ");
		sqlStr.append(" where pessoa.codigo = ").append(pessoa.getCodigo());
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("FU") || tipoPessoa.equals("CO")) {
				sqlStr.append(" and (pessoa.funcionario = true or pessoa.coordenador = true) ");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" and pessoa.professor = true");
			}
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" and pessoa.aluno = true");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return pessoa;
	}

	@Override
	public PessoaVO consultarPorEmailUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer("SELECT * FROM Pessoa ");
		sqlStr.append(" WHERE sem_acentos(lower(trim(Pessoa.email))) = sem_acentos('").append(valorConsulta.trim().toLowerCase()).append("') ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new PessoaVO();
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}
	
	@Override
	public PessoaVO consultarPorEmaiInstitucionallUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer("SELECT Pessoa.* FROM Pessoa ");
		sqlStr.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo ");
		sqlStr.append(" WHERE sem_acentos(lower(trim(pessoaemailinstitucional.email))) = sem_acentos('").append(valorConsulta.trim().toLowerCase()).append("') ");
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new PessoaVO();
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public PessoaVO consultarPorCertidaoNascimentoUnico(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE TRIM(replace(replace(certidaoNascimento,'.',''),'-','')) = '" + Uteis.retirarMascaraCPF(valorConsulta) + "' ";
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("PR")) {
				sqlStr += "and professor = 'true'";
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr += "and funcionario = 'true'";
			}
			if (tipoPessoa.equals("CA")) {
				sqlStr += "and candidato = 'true'";
			}
			if (tipoPessoa.equals("AL")) {
				sqlStr += "and aluno = 'true'";
			}
			if (tipoPessoa.equals("MC")) {
				sqlStr += "and membroComunidade = 'true'";
			}
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new PessoaVO();
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public PessoaVO consultarPorCPFProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE replace(replace(CPF,'.',''),'-','') = '" + Uteis.retirarMascaraCPF(valorConsulta) + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new PessoaVO();
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>nome</code> da classe <code>Cidade</code> Faz uso
	 * da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorNomeCidade(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Pessoa.* FROM Pessoa, Cidade WHERE Pessoa.cidade = Cidade.codigo and lower (Cidade.nome) like(?)";
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("PR")) {
				sqlStr += "and professor = 'true'";
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr += "and funcionario = 'true'";
			}
			if (tipoPessoa.equals("CA")) {
				sqlStr += "and candidato = 'true'";
			}
			if (tipoPessoa.equals("AL")) {
				sqlStr += "and aluno = 'true'";
			}
			if (tipoPessoa.equals("MC")) {
				sqlStr += "and membroComunidade = 'true'";
			}
		}
		sqlStr += " ORDER BY Cidade.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toLowerCase() + "%" });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo e do tipo de pessoa <code>String nome</code>. Retorna
	 * os objetos, com inï¿½cio do valor do atributo idï¿½ntico ao parï¿½metro
	 * fornecido e que sejam do tipo de pessoa do parï¿½metro. Faz uso da operaï¿½ï¿½o
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List<PessoaVO> consultarPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNome(valorConsulta, tipoPessoa, controlarAcesso, nivelMontarDados, false, usuario);
	}
	
	public List<PessoaVO> consultarPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, boolean funcionarioObrigatorio, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append((funcionarioObrigatorio ? " INNER " : " LEFT " ) + " join funcionario on pessoa.codigo=funcionario.pessoa ");
		sqlStr.append(" WHERE upper(sem_acentos(pessoa.nome)) ILIKE (upper(sem_acentos(?))) ");
			if(Uteis.isAtributoPreenchido(tipoPessoa)) {
				if (tipoPessoa.equals("PR")) {
					sqlStr.append(" and pessoa.professor = 'true'");
				}
				if (tipoPessoa.equals("FU")) {
					sqlStr.append("and pessoa.funcionario = 'true'");
				}

				if (tipoPessoa.equals("CA")) {
					sqlStr.append(" and candidato = 'true'");
				}
				if (tipoPessoa.equals("AL")) {
					sqlStr.append(" and aluno = 'true'");
				}
				if (tipoPessoa.equals("MC")) {
					sqlStr.append(" and membroComunidade = 'true'");
				}
				if (tipoPessoa.equals(TipoPessoa.COORDENADOR_CURSO.getValor())) {
					sqlStr.append(" and exists (select codigo from cursocoordenador where cursocoordenador.funcionario = funcionario.codigo ");
					sqlStr.append(" limit 1)");

				}
			}

		sqlStr.append(" ORDER BY pessoa.nome limit 10");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), PERCENT + valorConsulta + PERCENT);
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasico(obj, tabelaResultado);
			if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_COMBOBOX) {
				obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, false, usuario));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<PessoaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" left join funcionario on pessoa.codigo=funcionario.pessoa ");
		sqlStr.append(" WHERE (sem_acentos(pessoa.nome)) ilike (upper(sem_acentos(?))) ");
		if (tipoPessoa.equals("PR") || (tipoPessoa.equals("FU"))) {
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" and pessoa.professor = 'true'");
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr.append("and pessoa.funcionario = 'true'");
			}
		} else {
			if (tipoPessoa.equals("CA")) {
				sqlStr.append(" and candidato = 'true'");
			}
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" and aluno = 'true'");
			}
			if (tipoPessoa.equals("MC")) {
				sqlStr.append(" and membroComunidade = 'true'");
			}
		}
		if (unidadeEnsino != 0) {
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta + "%" });
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasico(obj, tabelaResultado);
			if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_COMBOBOX) {
				obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, false, usuario));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List consultarPorCoordenadoresNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		sqlStr = "SELECT pessoa.* FROM Pessoa, Funcionario WHERE lower (Pessoa.nome) like(?) " + "and pessoa.codigo=funcionario.pessoa and Funcionario.exerceCargoAdministrativo = 'true' ";
		sqlStr = sqlStr + " Order By nome";
		// if (unidadeEnsino.intValue() != 0) {
		// sqlStr =
		// "SELECT pessoa.* FROM Pessoa, Funcionario, UnidadeEnsino WHERE lower (Pessoa.nome)like('"
		// + valorConsulta.toLowerCase() +
		// "%') and pessoa.codigo=funcionario.pessoa and Funcionario.exerceCargoAdministrativo = 'true' "
		// +
		// " and Funcionario.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = "
		// + unidadeEnsino.intValue();
		// }
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarAlunoMatriculadoPorNome(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PE.* FROM Pessoa PE " + " WHERE PE.nome ilike (?)";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT PE.* FROM Pessoa PE " + " INNER JOIN Matricula MA ON MA.aluno = PE.codigo " + "WHERE PE.nome ilike (?)" + " AND (MA.unidadeEnsino) = " + unidadeEnsino.intValue();
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr += " AND PE.aluno = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr += " AND PE.professor = 'true'";
		}
		sqlStr += " ORDER BY PE.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta + "%" });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarAlunoMatriculadoPorCPF(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PE.* FROM Pessoa PE " + " WHERE lower (replace(replace((PE.CPF),'.',''),'-','')) like('" + Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()) + "%')";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT PE.* FROM Pessoa PE " + " INNER JOIN Matricula MA ON MA.aluno = PE.codigo WHERE lower (replace(replace((PE.CPF),'.',''),'-','')) like('" + Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()) + "%')" + " AND (MA.unidadeEnsino) = " + unidadeEnsino.intValue();
		}

		if (tipoPessoa.equals("AL")) {
			sqlStr += " AND PE.aluno = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr += " AND PE.professor = 'true'";
		}
		sqlStr += " ORDER BY CPF";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo e do tipo Pessoa <code>Integer codigo</code>. Retorna
	 * os objetos com valores iguais ou superiores ao parï¿½metro fornecido e
	 * iguais ao tipo da pessoa passado. Faz uso da operaï¿½ï¿½o
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	@Override
	public List<PessoaVO> consultarPorCodigo(Integer valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE codigo >= " + valorConsulta.intValue();
		if (tipoPessoa.equals("PR")) {
			sqlStr += "and professor = 'true'";
		} else if (tipoPessoa.equals("FU")) {
			sqlStr += "and funcionario = 'true'";
		} else if (tipoPessoa.equals("CA")) {
			sqlStr += "and candidato = 'true'";
		} else if (tipoPessoa.equals("AL")) {
			sqlStr += "and aluno = 'true'";
		}
		if (tipoPessoa.equals("MC")) {
			sqlStr += "and membroComunidade = 'true'";
		}
		sqlStr = sqlStr + " Order By codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigo(Integer valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE codigo >= " + valorConsulta.intValue();
		if (tipoPessoa.equals("PR")) {
			sqlStr += "and professor = 'true'";
		} else if (tipoPessoa.equals("FU")) {
			sqlStr += "and funcionario = 'true'";
		} else if (tipoPessoa.equals("CA")) {
			sqlStr += "and candidato = 'true'";
		} else if (tipoPessoa.equals("AL")) {
			sqlStr += "and aluno = 'true'";
		}
		if (tipoPessoa.equals("MC")) {
			sqlStr += "and membroComunidade = 'true'";
		}
		if (unidadeEnsino != 0) {
			sqlStr += "and membroComunidade = 'true'";
		}
		sqlStr = sqlStr + " Order By codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String necessidadesEspeciais</code>. Retorna os
	 * objetos, com inï¿½cio do valor do atributo idï¿½ntico ao parï¿½metro fornecido.
	 * Faz uso da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorNecessidadesEspeciais(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE lower (necessidadesEspeciais) like('" + valorConsulta.toLowerCase() + "%') ORDER BY necessidadesEspeciais";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String RG</code>. Retorna os objetos, com inï¿½cio
	 * do valor do atributo idï¿½ntico ao parï¿½metro fornecido. Faz uso da operaï¿½ï¿½o
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorRG(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE lower (RG) like('" + valorConsulta.toLowerCase() + "%') ORDER BY RG";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String CPF</code> . Retorna os objetos, com
	 * inï¿½cio do valor do atributo idï¿½ntico ao parï¿½metro fornecido. Faz uso da
	 * operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorCPF(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE lower (replace(replace(CPF,'.',''),'-','')) like('" + Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()) + "%') ORDER BY CPF";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>nome</code> da classe <code>Cidade</code> Faz uso
	 * da operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorNomeCidade(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Pessoa.* FROM Pessoa, Cidade WHERE Pessoa.cidade = Cidade.codigo and lower (Cidade.nome) like(?) ORDER BY Cidade.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toLowerCase() + "%" });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>String nome</code>. Retorna os objetos, com
	 * inï¿½cio do valor do atributo idï¿½ntico ao parï¿½metro fornecido. Faz uso da
	 * operaï¿½ï¿½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultaRapidaPorNome(valorConsulta, "", controlarAcesso, nivelMontarDados, usuario);
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		// String sqlStr =
		// "SELECT * FROM Pessoa WHERE lower (nome) like(?) ORDER BY nome";
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new
		// Object[]{valorConsulta.toLowerCase() + "%"});
		// return (montarDadosConsulta(tabelaResultado, nivelMontarDados,
		// usuario));
	}

	public List consultarPorMatriculaTipoPessoa(String valorConsulta, Boolean aluno, Boolean professor, Boolean funcionario, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa ");
		if (aluno != null && aluno) {
			sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
			sqlStr.append(" WHERE lower (matricula.matricula) like('").append(valorConsulta.toLowerCase()).append("%')");
		}
		if (((professor != null && professor) || (funcionario != null && funcionario)) && (aluno == null || !aluno)) {
			sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
			sqlStr.append(" INNER JOIN funcionariocargo ON funcionariocargo.funcionario = funcionario.codigo");
			sqlStr.append(" WHERE lower (funcionario.matricula) like('").append(valorConsulta.toLowerCase()).append("%')");
		}
		if (aluno != null) {
			sqlStr.append(" AND Pessoa.aluno = ").append(aluno);
			if (aluno && unidadeEnsino != 0) {
				sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino);
			}
		}
		if (professor != null) {
			sqlStr.append(" AND Pessoa.professor = ").append(professor);
			if (professor && unidadeEnsino != 0) {
				sqlStr.append(" AND funcionariocargo.unidadeensino = ").append(unidadeEnsino);
			}
		}
		if (funcionario != null) {
			sqlStr.append(" AND Pessoa.funcionario = ").append(funcionario);
			if (funcionario && unidadeEnsino != 0) {
				sqlStr.append(" AND funcionariocargo.unidadeensino = ").append(unidadeEnsino);
			}
		}
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeTipoPessoa(String valorConsulta, Boolean aluno, Boolean professor, Boolean funcionario, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa ");
		if (aluno != null && aluno && unidadeEnsino != 0) {
			sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
		}
		if ((professor != null && professor && unidadeEnsino != 0) || (funcionario != null && funcionario && unidadeEnsino != 0)) {
			sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
			sqlStr.append(" INNER JOIN funcionariocargo ON funcionariocargo.funcionario = funcionario.codigo");
		}
		sqlStr.append(" WHERE lower (pessoa.nome) like(?)");
		if (aluno != null) {
			sqlStr.append(" AND Pessoa.aluno = ").append(aluno);
			if (aluno && unidadeEnsino != 0) {
				sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino);
			}
		}
		if (professor != null) {
			sqlStr.append(" AND Pessoa.professor = ").append(professor);
			if (professor && unidadeEnsino != 0) {
				sqlStr.append(" AND funcionariocargo.unidadeensino = ").append(unidadeEnsino);
			}
		}
		if (funcionario != null) {
			sqlStr.append(" AND Pessoa.funcionario = ").append(funcionario);
			if (funcionario && unidadeEnsino != 0) {
				sqlStr.append(" AND funcionariocargo.unidadeensino = ").append(unidadeEnsino);
			}
		}
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarProfessorPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa  WHERE lower (pessoa.nome) ilike('" + valorConsulta + "%') AND Pessoa.professor = true  ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" WHERE lower (curso.nome) like('").append(valorConsulta.toLowerCase()).append("%') ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeUnidadeEnsinoTipoPessoa(String valorConsulta, Boolean aluno, Boolean professor, Boolean funcionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa ");
		if (aluno != null && aluno) {
			sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
			sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino");
		}
		if (((professor != null && professor) || (funcionario != null && funcionario)) && (aluno == null || !aluno)) {
			sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
			sqlStr.append(" INNER JOIN funcionariocargo ON funcionariocargo.funcionario = funcionario.codigo");
			sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = funcionariocargo.unidadeensino");
		}
		sqlStr.append(" WHERE lower (unidadeensino.nome) like('").append(valorConsulta.toLowerCase()).append("%')");
		if (aluno != null) {
			sqlStr.append(" AND Pessoa.aluno = ").append(aluno);
		}
		if (professor != null) {
			sqlStr.append(" AND Pessoa.professor = ").append(professor);
		}
		if (funcionario != null) {
			sqlStr.append(" AND Pessoa.funcionario = ").append(funcionario);
		}
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorIdentificadorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" INNER JOIN turma ON turma.curso = curso.codigo");
		sqlStr.append(" WHERE lower (turma.identificadorturma) like('").append(valorConsulta.toLowerCase()).append("%')");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarProfessoresDoAlunoPorTurmaDisciplina(Integer disciplina, Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append(" SELECT distinct pessoa.* FROM pessoa");
		selectStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.professor = pessoa.codigo");
		selectStr.append(" WHERE pessoa.professor = true and horarioturmaprofessordisciplina.disciplina = ").append(disciplina);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());

		List listaProfessores = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return listaProfessores;
	}

	public List consultarProfessoresDoAlunoPorTurmaDisciplinaAnoSemestre(Integer disciplina, Integer turma, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append(" SELECT distinct pessoa.* FROM pessoa");
		selectStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.professor = pessoa.codigo");
		selectStr.append(" INNER JOIN horarioturma on horarioturma.codigo = horarioturmaprofessordisciplina.horarioturma ");
		selectStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = ( SELECT codigo FROM matriculaPeriodo WHERE MatriculaPeriodo.matricula = '").append(matricula).append("' ");
		selectStr.append(" order by (MatriculaPeriodo.ano ||'/' || MatriculaPeriodo.semestre) desc limit 1) ");
		selectStr.append(" WHERE pessoa.professor = true and horarioturma.anovigente = MatriculaPeriodo.ano ");
		selectStr.append(" and horarioturma.semestrevigente = MatriculaPeriodo.semestre and horarioturmaprofessordisciplina.disciplina = ").append(disciplina);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());

		List listaProfessores = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return listaProfessores;
	}

	public List<TurmaProfessorDisciplinaVO> consultarProfessorDisciplina(String matricula, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaEUltimaMatriculaPeriodo(matricula, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVOs)) {
			return new ArrayList<TurmaProfessorDisciplinaVO>(0);
		}
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\", disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\" ");
		sql.append(" from horarioturmadetalhado(null, null, '" + matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno() + "', '" + matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre() + "', null, null, null, null) as t ");
		sql.append(" inner join pessoa on pessoa.codigo = t.professor ");
		sql.append(" inner join disciplina on disciplina.codigo = t.disciplina ");
		sql.append(" inner join turma on turma.codigo = t.turma ");
		sql.append(" inner join unidadeensino on turma.unidadeensino = unidadeensino.codigo");
		sql.append(" inner join configuracaogeralsistema on configuracaogeralsistema.configuracoes = unidadeensino.configuracoes");
		sql.append(" inner join turno on turno.codigo = turma.turno  ");
		sql.append(" where ( ");
		int x = 0;
		for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
			if (mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta()) {
				if (x > 0) {
					sql.append(" or ");
				}
				sql.append(" ((turma.codigo = ").append(mp.getTurma().getCodigo()).append(" or turma.codigo in (select turmaOrigem from TurmaAgrupada where turma = " + mp.getTurma().getCodigo() + ")) ");
				sql.append(" and disciplina.codigo in ( ");
				sql.append(" select disciplina from turmadisciplina  where turmadisciplina.turma = t.turma ");
				sql.append(" and turmadisciplina.disciplina in (");
				sql.append(" select ").append(mp.getDisciplina().getCodigo());
				sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
				sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
				sql.append(" ) ");
				sql.append(" union ");
				sql.append(" select gradedisciplinacomposta.disciplina from turmadisciplina ");
				sql.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
				sql.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina ");
				sql.append(" where turmadisciplina.turma = t.turma and turmadisciplina.disciplina in ( ");
				sql.append(" select ").append(mp.getDisciplina().getCodigo());
				sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
				sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
				sql.append(" ) ");
				sql.append(" union ");
				sql.append(" select gradedisciplinacomposta.disciplina from  turmadisciplina ");
				sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
				sql.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina ");
				sql.append(" where turmadisciplina.turma = t.turma and turmadisciplina.disciplina in ( ");
				sql.append(" select ").append(mp.getDisciplina().getCodigo());
				sql.append(" union select disciplina from disciplinaequivalente  where equivalente = ").append(mp.getDisciplina().getCodigo());
				sql.append(" union select equivalente from disciplinaequivalente  where disciplina = ").append(mp.getDisciplina().getCodigo());
				sql.append(" ) ");

				sql.append(" ) ");
				sql.append(" ) ");
				x++;
			}
		}
		sql.append(") ");
		/**
		 * Adicionada regra para resolver impactos relacionados a transferencia
		 * de matriz curricular pois estava validando choque de horário com
		 * disciplinas da grade anterior
		 */
		sql.append(" and exists (");
		sql.append(" select disciplina from historico ");
		sql.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
		sql.append(" inner join periodoauladisciplinaaluno(historico.codigo) as professores on professores.professor_codigo is not null ");
		sql.append(" where matricula.matricula = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getMatricula()).append("' ");
		sql.append(" and disciplina.codigo = historico.disciplina and pessoa.codigo = professores.professor_codigo ");
		sql.append(" and ((matricula.gradecurricularatual = historico.matrizcurricular ");
		sql.append(" and (historico.historicocursandoporcorrespondenciaapostransferencia is null or");
		sql.append(" historico.historicocursandoporcorrespondenciaapostransferencia = false)");
		sql.append(" and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sql.append(" and historico.disciplina not in (select disciplina from historico his");
		sql.append(" where his.matricula = historico.matricula");
		sql.append(" and his.anohistorico = historico.anohistorico");
		sql.append(" and his.semestrehistorico = historico.semestrehistorico");
		sql.append(" and his.disciplina = historico.disciplina");
		sql.append(" and his.historicocursandoporcorrespondenciaapostransferencia");
		sql.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sql.append(" and his.matrizcurricular != matricula.gradecurricularatual limit 1");
		sql.append(" )))) or (matricula.gradecurricularatual != historico.matrizcurricular");
		sql.append(" and historico.historicocursandoporcorrespondenciaapostransferencia ");
		sql.append(" and historico.transferenciamatrizcurricularmatricula IS NOT NULL ");
		sql.append(" and historico.disciplina = (select disciplina from historico his");
		sql.append(" where his.matricula = historico.matricula ");
		sql.append(" and his.anohistorico = historico.anohistorico");
		sql.append(" and his.semestrehistorico = historico.semestrehistorico");
		sql.append(" and his.disciplina = historico.disciplina");
		sql.append(" and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula");
		sql.append(" and (his.historicocursandoporcorrespondenciaapostransferencia is null or ");
		sql.append(" his.historicocursandoporcorrespondenciaapostransferencia = false)");
		sql.append(" and his.matrizcurricular = matricula.gradecurricularatual limit 1");
		sql.append(" )) ");
		// Essa condição OR é responsável por trazer as disciplinas que fazem parte da composição após realizar transferencia
		// de matriz curricular. Isso porque após a transferência o aluno irá cursar a disciplina na grade antiga mesmo estando
		// já realizada a transferência para nova grade. O sistema então irá trazer o histórico da matriz antiga caso não possua a mesma disciplina na nova grade.
		sql.append(" or (historico.matrizcurricular = matriculaPeriodo.gradecurricular ");
		sql.append(" and matricula.gradecurricularatual != historico.matrizcurricular ");
		sql.append(" and historico.historicodisciplinafazpartecomposicao ");
		sql.append(" and historico.disciplina not in (");
		sql.append(" select his.disciplina from historico his ");
		sql.append(" where his.matriculaperiodo = historico.matriculaperiodo ");
		sql.append(" and his.disciplina = historico.disciplina ");
		sql.append(" and matricula.gradecurricularatual = his.matrizcurricular))	");
		sql.append(") ");
		sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
		sql.append(" and (historico.situacao not in ('AA', 'CC', 'CH', 'IS')) ");
		if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno().isEmpty()) {
			sql.append(" and historico.anohistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getAno()).append("' ");
		}
		if (!matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre().isEmpty()) {
			sql.append(" and historico.semestrehistorico = '").append(matriculaPeriodoTurmaDisciplinaVOs.get(0).getSemestre()).append("' ");
		}
		for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
			if (mp.isNovoObj() && (mp.getDisciplinaFazParteComposicao() || !mp.getDisciplinaComposta())) {
				sql.append(" union ");
				sql.append(" select ").append(mp.getDisciplina().getCodigo());
				x++;
			}
		}
		sql.append(" ) ");
		sql.append(" order by pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
		List<TurmaProfessorDisciplinaVO> turmaProfessorDisciplinaVOs = new ArrayList<TurmaProfessorDisciplinaVO>(0);
		while (rs.next()) {
			TurmaProfessorDisciplinaVO obj = new TurmaProfessorDisciplinaVO();
			obj.getProfessorVO().setCodigo(rs.getInt("pessoa.codigo"));
			obj.getProfessorVO().setNome(rs.getString("pessoa.nome"));
			obj.getDisciplinaVO().setCodigo(rs.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
			obj.getProfessorVO().setEmail(rs.getString("pessoa.email"));
			turmaProfessorDisciplinaVOs.add(obj);
		}
		return turmaProfessorDisciplinaVOs;
	}

	// public List consultarProfessoresDoAluno(Integer codigoAluno, Integer
	// unidadeEnsino, boolean controlarAcesso, int nivelMontarDados) throws
	// Exception {
	// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	//
	// String sqlStr = "Select " + getGroupBy("pr") + " " +
	// "from RegistroAula, FrequenciaAula, Matricula, Pessoa as al, Pessoa as pr "
	// +
	// "where FrequenciaAula.matricula = Matricula.matricula and Matricula.aluno = al.codigo and al.codigo = "
	// + codigoAluno.intValue() + " "
	// +
	// "and FrequenciaAula.registroAula = RegistroAula.codigo and RegistroAula.professor = pr.codigo "
	// + "Group By  " + getGroupBy("pr");
	// if (unidadeEnsino.intValue() != 0) {
	// sqlStr = "Select " + getGroupBy("pr") + " " +
	// "from RegistroAula, FrequenciaAula, Matricula, Pessoa as al, Pessoa as pr, UnidadeEnsino, Funcionario "
	// +
	// "where FrequenciaAula.matricula = Matricula.matricula and Matricula.aluno = al.codigo and al.codigo = "
	// + codigoAluno.intValue() + " "
	// +
	// "and FrequenciaAula.registroAula = RegistroAula.codigo and RegistroAula.professor = pr.codigo and Funcionario.pessoa = pr.codigo "
	// +
	// "and Funcionario.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = "
	// + unidadeEnsino.intValue() + "Group By  " + getGroupBy("pr");
	// }
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	//
	// List listaProfessores = montarDadosConsulta(tabelaResultado,
	// nivelMontarDados);
	// listaProfessores = consultarProfessorPorMatriculaPeriodo(codigoAluno,
	// unidadeEnsino, listaProfessores, nivelMontarDados);
	// return listaProfessores;
	// }
	public List consultarProfessoresDoAluno(Integer codigoAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "Select distinct pr.codigo, pr.nome, pr.cpf, pr.email, pr.email2, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo from HorarioTurmaProfessorDisciplina  "
				+ "inner join funcionario on funcionario.pessoa = HorarioTurmaProfessorDisciplina.professor  " + "inner join pessoa as pr on pr.codigo = funcionario.pessoa  " + "inner join Turma on HorarioTurmaProfessorDisciplina.turma = turma.codigo "
				+ "inner join MatriculaPeriodo on MatriculaPeriodo.turma = turma.codigo " + "inner join matricula on matriculaPeriodo.matricula = matricula.matricula " + "inner join pessoa as al on Matricula.aluno  = al.codigo and al.codigo = "
				+ codigoAluno.intValue() + " " + "left join Arquivo on arquivo.codigo = pr.arquivoimagem " + "inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino  ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select distinct pr.codigo, pr.nome, pr.cpf, pr.email, pr.email2, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo from HorarioTurmaProfessorDisciplina  "
					+ "inner join funcionario on funcionario.pessoa = HorarioTurmaProfessorDisciplina.professor  " + "inner join pessoa as pr on pr.codigo = funcionario.pessoa  " + "inner join Turma on HorarioTurmaProfessorDisciplina.turma = turma.codigo "
					+ "inner join MatriculaPeriodo on MatriculaPeriodo.turma = turma.codigo " + "inner join matricula on matriculaPeriodo.matricula = matricula.matricula " + "inner join pessoa as al on Matricula.aluno  = al.codigo and al.codigo = "
					+ codigoAluno.intValue() + " inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino  " + "left join Arquivo on arquivo.codigo = pr.arquivoimagem " + "WHERE UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List listaProfessores = montarDadosConsultaRapidaVisaoAluno(tabelaResultado);
		listaProfessores = consultarProfessorPorMatriculaPeriodo(codigoAluno, unidadeEnsino, listaProfessores, nivelMontarDados, configuracaoFinanceiroVO, usuario);
		return listaProfessores;
	}

	public List<PessoaVO> consultarProfessoresDoAlunoVisaoAluno(Integer codigoAluno, String valorConsulta, Integer unidadeEnsino, String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT professor.codigo, professor.nome, professor.cpf, professor.email, professor.email2, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo ");
		sqlStr.append(" FROM matricula INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = ( ");
		sqlStr.append(" SELECT mp.codigo from matriculaperiodo mp ");
		sqlStr.append(" WHERE mp.matricula = matricula.matricula ");
		sqlStr.append(" order by case when mp.situacaomatriculaperiodo in ('AT','FI') and ((mp.ano <> '' and mp.semestre <> '' and mp.ano = '").append(Uteis.getAnoDataAtual()).append("' ");
		sqlStr.append(" and mp.semestre = '").append(Uteis.getSemestreAtual()).append("') or (mp.ano <> '' and mp.semestre = '' and mp.ano = '").append(Uteis.getAnoDataAtual()).append("')) then 2 else ");
		sqlStr.append(" case when mp.situacaomatriculaperiodo IN ('AT', 'FI') then 1 else 0 end end desc, (mp.ano ||'/' || mp.semestre) desc limit 1) ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaPeriodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN historico ON historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append(" INNER JOIN periodoauladisciplinaaluno(historico.codigo) as professores ON professores.professor_codigo is not null ");
		sqlStr.append(" INNER JOIN pessoa as professor on professor.codigo = professores.professor_codigo ");
		sqlStr.append(" LEFT JOIN Arquivo on arquivo.codigo = professor.arquivoimagem WHERE matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" AND upper(professor.nome) like upper(?) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY professor.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), PERCENT + valorConsulta + PERCENT);
		return montarDadosConsultaRapidaVisaoAluno(tabelaResultado);

	}

	public List consultarProfessorPorMatriculaPeriodo(Integer codigoAluno, Integer unidadeEnsino, List listaProfessores, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculas = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoa(codigoAluno, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, configuracaoFinanceiroVO, usuario);
		for (MatriculaVO obj : matriculas) {
			Integer codigoMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, usuario);
			if (Uteis.isAtributoPreenchido(codigoMatriculaPeriodo)) {
				consultarProfessorTurmaDisciplina(codigoMatriculaPeriodo, codigoAluno, obj.getUnidadeEnsino().getCodigo(), listaProfessores, usuario);
			}
		}
		return listaProfessores;
	}

	public List consultarProfessorTurmaDisciplina(Integer matriculaPeriodo, Integer codigoAluno, Integer unidadeEnsino, List listaProfessores, UsuarioVO usuario) throws Exception {
		List listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaPeriodo(matriculaPeriodo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		Iterator i = listaMatriculaPeriodoTurmaDisciplina.iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			List listaProfessores2 = consultarProfessoresDaTurmaDisciplinaDoAluno(codigoAluno, obj.getTurma().getCodigo(), obj.getDisciplina().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			Iterator j = listaProfessores2.iterator();
			while (j.hasNext()) {
				PessoaVO objs = (PessoaVO) j.next();
				adicionarProfessoresAluno(listaProfessores, objs);
			}
		}
		return listaProfessores;
	}

	public void adicionarProfessoresAluno(List listaProfessores, PessoaVO professor) {
		Iterator i = listaProfessores.iterator();
		while (i.hasNext()) {
			PessoaVO obj = (PessoaVO) i.next();
			if (professor.getCodigo().intValue() == obj.getCodigo()) {
				return;
			}

		}
		listaProfessores.add(professor);
	}

	public List consultarProfessoresDaTurmaDoAluno(Integer codigoAluno, Integer turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("pr") + " " + "From Pessoa al, Pessoa pr, HorarioTurmaProfessorDisciplina, HorarioTurma, Turma, MatriculaPeriodo, Matricula " + "where HorarioTurmaProfessorDisciplina.professor = pr.codigo and HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.turma = Turma.codigo " + "and MatriculaPeriodo.turma = Turma.codigo and MatriculaPeriodo.matricula = Matricula.matricula  and Matricula.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + " and Turma.codigo = " + turma.intValue() + " Group By  " + getGroupBy("pr");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("pr") + " " + "From UnidadeEnsino, Pessoa al, Pessoa pr, HorarioTurmaProfessorDisciplina, HorarioTurma, Turma, MatriculaPeriodo, Matricula " + "where HorarioTurmaProfessorDisciplina.professor = pr.codigo and HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.turma = Turma.codigo " + "and MatriculaPeriodo.turma = Turma.codigo and MatriculaPeriodo.matricula = Matricula.matricula  and Matricula.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + " " + "and Turma.codigo = " + turma.intValue() + " and Matricula.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " Group By  " + getGroupBy("pr");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List consultarProfessoresDaTurmaDisciplinaDoAluno(Integer codigoAluno, Integer turma, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("pr") + " " + " From Pessoa al, Pessoa pr, Disciplina, HorarioTurmaProfessorDisciplina, HorarioTurma, Turma, MatriculaPeriodoTurmaDisciplina, Matricula " + " where HorarioTurmaProfessorDisciplina.professor = pr.codigo and HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.turma = Turma.codigo " + " and MatriculaPeriodoTurmaDisciplina.turma = Turma.codigo and MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula  and Matricula.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + " " + " and Turma.codigo = " + turma.intValue() + " HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo and disciplina.codigo = " + disciplina.intValue() + " Group By  " + getGroupBy("pr");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("pr") + " " + " From UnidadeEnsino,Disciplina, Pessoa al, Pessoa pr, HorarioTurmaProfessorDisciplina, HorarioTurma, Turma, MatriculaPeriodoTurmaDisciplina, Matricula " + " where HorarioTurmaProfessorDisciplina.professor = pr.codigo and HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.turma = Turma.codigo " + " and MatriculaPeriodoTurmaDisciplina.turma = Turma.codigo and MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula  and Matricula.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + " " + " and Turma.codigo = " + turma.intValue() + " and Matricula.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + "" + " and HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo and disciplina.codigo = " + disciplina.intValue() + " Group By  " + getGroupBy("pr");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List consultarProfessoresDaTurmaDoProfessor(Integer codigoProfessor, List turma, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("Pessoa") + " From Pessoa, HorarioTurmaProfessorDisciplina, HorarioTurma, Turma" + " where HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo and HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.turma = Turma.codigo " + " and Pessoa.codigo != " + codigoProfessor.intValue() + " ";
		Iterator i = turma.iterator();
		while (i.hasNext()) {
			TurmaVO obj = (TurmaVO) i.next();
			sqlStr += " and Turma.codigo = " + obj.getCodigo().intValue();
		}
		sqlStr += " Group By  " + getGroupBy("Pessoa");

		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("Pessoa") + " From UnidadeEnsino, Pessoa, HorarioTurmaProfessorDisciplina, HorarioTurma, Turma" + " where HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo and HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.turma = Turma.codigo " + " and Pessoa.codigo != " + codigoProfessor.intValue() + " and Turma.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue();
			Iterator j = turma.iterator();
			while (j.hasNext()) {
				TurmaVO obj = (TurmaVO) j.next();
				sqlStr += " and Turma.codigo = " + obj.getCodigo().intValue();
			}
			sqlStr += " Group By  " + getGroupBy("Pessoa");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

	}

	public List consultarColegasDoAlunoPorNome(Integer codigoAluno, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("pr") + " " + "from Turma, Matricula as ma, Matricula as mc, MatriculaPeriodoTurmaDisciplina as mpa, MatriculaPeriodoTurmaDisciplina as mpc, Pessoa as al, Pessoa as pr " + "where mpa.turma = Turma.codigo and mpa.matricula = ma.matricula and ma.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + "" + "and mpc.turma = Turma.codigo and mpc.matricula = mc.matricula and mc.aluno = pr.codigo and pr.codigo != " + codigoAluno.intValue() + " " + "and lower (pr.nome) like(?) " + "Group By  " + getGroupBy("pr");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("pr") + " " + "from UnidadeEnsino, Turma, Matricula as ma, Matricula as mc, MatriculaPeriodoTurmaDisciplina as mpa, MatriculaPeriodoTurmaDisciplina as mpc, Pessoa as al, Pessoa as pr " + "where mpa.turma = Turma.codigo and mpa.matricula = ma.matricula and ma.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + "" + "and mpc.turma = Turma.codigo and mpc.matricula = mc.matricula and mc.aluno = pr.codigo and pr.codigo != " + codigoAluno.intValue() + " " + "and lower (pr.nome) like(?) and Turma.UnidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " " + "Group By  " + getGroupBy("pr");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	// public List consultarAlunoPorNomeVisaoAluno(String valorConsulta, Integer
	// unidadeEnsino, boolean controlarAcesso, int nivelMontarDados) throws
	// Exception {
	// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	//
	// String sqlStr = "select " + getGroupBy("al") + " from pessoa  " +
	// "left join matricula on matricula.aluno = pessoa.codigo " +
	// "left join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula "
	// +
	// "left join Turma on matriculaPeriodo.turma = turma.codigo " +
	// " WHERE lower(pessoa.nome) like('" + valorConsulta.toLowerCase() +
	// "%') and pessoa.aluno = 'TRUE'";
	// if (unidadeEnsino.intValue() != 0) {
	// sqlStr = "select " + getGroupBy("al") + " from pessoa  " +
	// "left join matricula on matricula.aluno = pessoa.codigo " +
	// "left join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula "
	// +
	// "left join Turma on matriculaPeriodo.turma = turma.codigo " +
	// "left join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino"
	// +
	// " WHERE lower(pessoa.nome) like('" + valorConsulta.toLowerCase() +
	// "%') and pessoa.aluno = 'TRUE' and unidadeEnsino.codigo = " +
	// unidadeEnsino.intValue() + " ";
	// }
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	// }
	public List consultarAlunoDoProfessorPorNome(Integer codigoProfessor, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "Select " + getGroupBy("al") + " " + " from RegistroAula " + "inner join FrequenciaAula on RegistroAula.codigo =FrequenciaAula.RegistroAula " + "inner join Matricula on FrequenciaAula.Matricula = Matricula.matricula " + "inner join pessoa as al on Matricula.aluno  = al.codigo " + "inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino " + "inner join funcionario on funcionario.codigo = RegistroAula.professor " + "inner join pessoa as pr on pr.codigo = funcionario.pessoa and pr.codigo = " + codigoProfessor.intValue() + " " + " WHERE lower (al.nome) like(?) " + " Group By  " + getGroupBy("al");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("al") + " " + "from RegistroAula " + "inner join FrequenciaAula on RegistroAula.codigo =FrequenciaAula.RegistroAula " + "inner join Matricula on FrequenciaAula.Matricula = Matricula.matricula " + "inner join pessoa as al on Matricula.aluno  = al.codigo " + "inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino " + "inner join funcionario on funcionario.codigo = RegistroAula.professor " + "inner join pessoa as pr on pr.codigo = funcionario.pessoa and pr.codigo = " + codigoProfessor.intValue() + " " + "WHERE lower (al.nome) like(?)  " + "and Turma.UnidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " " + "Group By  " + getGroupBy("al");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarAlunoDoProfessorPorTurma(Integer codigoProfessor, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("al") + " " + " from RegistroAula " + "inner join FrequenciaAula on RegistroAula.codigo =FrequenciaAula.RegistroAula " + "inner join Matricula on FrequenciaAula.Matricula = Matricula.matricula " + "inner join pessoa as al on Matricula.aluno  = al.codigo " + "inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino " + "inner join funcionario on funcionario.codigo = RegistroAula.professor " + "inner join pessoa as pr on pr.codigo = funcionario.pessoa and pr.codigo = " + codigoProfessor.intValue() + " " + "inner join Turma on turma.codigo = RegistroAula.turma " + " WHERE lower (Turma.identificadorTurma) like('" + valorConsulta.toLowerCase() + "%') " + " Group By  " + getGroupBy("al");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("al") + " " + "from RegistroAula " + "inner join FrequenciaAula on RegistroAula.codigo =FrequenciaAula.RegistroAula " + "inner join Matricula on FrequenciaAula.Matricula = Matricula.matricula " + "inner join pessoa as al on Matricula.aluno  = al.codigo " + "inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino " + "inner join funcionario on funcionario.codigo = RegistroAula.professor " + "inner join pessoa as pr on pr.codigo = funcionario.pessoa and pr.codigo = " + codigoProfessor.intValue() + " " + "inner join Turma on turma.codigo = RegistroAula.turma" + "WHERE lower (Turma.identificadorTurma) like('" + valorConsulta.toLowerCase() + "%')  and Turma.UnidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " " + "Group By  " + getGroupBy("al");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarAlunosDoProfessorPorNome(Integer codigoProfessor, String nomeAluno, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN turma ON matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.turma = matriculaperiodo.turma ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa AS pr ON pr.codigo = funcionario.pessoa AND pr.codigo = ").append(codigoProfessor);
		sqlStr.append(" WHERE LOWER (pessoa.nome) LIKE(?) AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { nomeAluno.toLowerCase() + "%" });
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}
	
	public List consultarAlunosDoProfessorTutorPorNome(Integer codigoProfessor, String nomeAluno, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN turma ON matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.turma = matriculaperiodo.turma ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa AS pr ON pr.codigo = funcionario.pessoa AND pr.codigo = ").append(codigoProfessor);
		sqlStr.append(" WHERE LOWER (pessoa.nome) LIKE('").append(nomeAluno.toLowerCase()).append("%') AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		
		sqlStr.append(" union ");
		
		sqlStr.append(getSelectConsultaDadosComunicadoInterno());	
		
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN turma ON matriculaperiodoturmadisciplina.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN programacaotutoriaonline ON programacaotutoriaonline.turma = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" INNER JOIN programacaotutoriaonlineprofessor ON programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa AS pr ON pr.codigo = funcionario.pessoa AND pr.codigo = ").append(codigoProfessor);
		sqlStr.append(" WHERE LOWER (pessoa.nome) LIKE('").append(nomeAluno.toLowerCase()).append("%') AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodoturmadisciplina.ano = '' AND matriculaperiodoturmadisciplina.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' AND matriculaperiodoturmadisciplina.semestre = '')) ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarProfessoresDaTurmaPorTurma(Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN turma ON horarioturmaprofessordisciplina.turma = turma.codigo ");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino ");
		sqlStr.append(" INNER JOIN horarioturma ON horarioturma.codigo = horarioturmaprofessordisciplina.horarioturma ");
		sqlStr.append(" WHERE turma.codigo = ").append(turma).append(" AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((turma.anual = false and turma.semestral = false AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' OR turma.turmaagrupada = true or turma.subturma = true) OR ");
		sqlStr.append(" (turma.semestral AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("' OR turma.turmaagrupada = true or turma.subturma = true) OR ");
		sqlStr.append(" (turma.anual AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '' OR turma.turmaagrupada = true or turma.subturma = true)) ");
		sqlStr.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultarProfessoresDaTurmaPorTurmaAgrupada(Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("((");
		sqlStr.append(getSQLPadraoConsultaBasica(false, false));
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN turma ON horarioturmaprofessordisciplina.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino ");
		sqlStr.append(" INNER JOIN horarioturma ON horarioturma.codigo = horarioturmaprofessordisciplina.horarioturma ");
		sqlStr.append(" WHERE turma.codigo = ").append(turma).append(" AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append(" ) union (");
		sqlStr.append(getSQLPadraoConsultaBasica(false, false));
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN turma ON horarioturmaprofessordisciplina.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN turmaagrupada ta ON ta.turmaorigem = turma.codigo ");
		sqlStr.append(" INNER JOIN turma t2 ON t2.codigo = ta.turma ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = t2.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino ");
		sqlStr.append(" INNER JOIN horarioturma ON horarioturma.codigo = horarioturmaprofessordisciplina.horarioturma ");
		sqlStr.append(" WHERE ta.turmaorigem = ").append(turma).append(" AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append(" )) ORDER BY nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultarAlunosDaTurmaPorNome(String nomeAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN unidadeensinocurso ON matriculaperiodo.unidadeensinocurso = unidadeensinocurso.codigo ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN processomatricula ON processomatricula.codigo = matriculaperiodo.processomatricula ");
		sqlStr.append(" INNER JOIN processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatricula.codigo AND processomatriculacalendario.curso = unidadeensinocurso.curso AND processomatriculacalendario.turno = unidadeensinocurso.turno ");
		sqlStr.append(" INNER JOIN periodoletivoativounidadeensinocurso ON periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append(" INNER JOIN turma on turma.codigo = mptd.turma ");
		sqlStr.append(" INNER JOIN curso on curso.codigo = turma.curso ");
		sqlStr.append(" WHERE UPPER(pessoa.nome) LIKE('%").append(nomeAluno.toUpperCase()).append("%') ");
		sqlStr.append(" AND turma.codigo = ").append(turma);
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" AND CASE WHEN curso.periodicidade = 'IN' THEN true ELSE ");
		sqlStr.append(" periodoletivoativounidadeensinocurso.datainicioperiodoletivo::DATE <= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.datafimperiodoletivo::DATE >= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.situacao IN ('AT') END ");
//		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
//		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
//		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDaTurmaPorCpf(String cpf, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN turma on turma.codigo = mptd.turma ");
		sqlStr.append(" INNER JOIN curso on curso.codigo = turma.curso ");
		sqlStr.append(" WHERE LOWER (pessoa.cpf) LIKE('").append(cpf.toLowerCase()).append("%') AND ");
		sqlStr.append(" turma.codigo = ").append(turma).append(" AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDaTurmaPorCurso(String nomeCurso, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN turma on turma.codigo = mptd.turma ");
		sqlStr.append(" INNER JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append(" WHERE LOWER (curso.nome) LIKE('").append(nomeCurso.toLowerCase()).append("%') AND ");
		sqlStr.append(" turma.codigo = ").append(turma).append(" AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDaTurma(Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN turma on turma.codigo = mptd.turma ");
		sqlStr.append(" WHERE turma.codigo = ").append(turma).append(" AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDaTurmaTodos(Integer turma, Integer disciplina, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = mptd.turma ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append(" WHERE MatriculaPeriodo.situacaomatriculaperiodo IN ('AT','FI') AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND (mptd.turma = ").append(turma).append(" OR mptd.turma in(select turma from turmaagrupada where turmaorigem = ").append(turma).append(") or mptd.turmapratica = ").append(turma).append(" or mptd.turmateorica = ").append(turma).append(") ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append("  ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" AND mptd.disciplina = ").append(disciplina).append(" ");
		}

		if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
			sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
			sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		}

		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDaTurmaTodos(List listaTurma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append(" WHERE (mptd.turma <> matriculaperiodo.turma OR mptd.turma = matriculaperiodo.turma) ");
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo = 'AT' AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND mptd.turma in (");
		Iterator i = listaTurma.iterator();
		while (i.hasNext()) {
			Integer codTurma = (Integer) i.next();
			sqlStr.append(codTurma).append(", ");
		}
		sqlStr.append("0) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append("  ");
		}
		if (Uteis.isAtributoPreenchido(ano) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
			sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
			sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	@Override
	public List consultarAlunosDaTurmaNormal(Integer turma, Integer disciplina, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
//		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
//		}
		//sqlStr.append(" WHERE MatriculaPeriodo.situacaomatriculaperiodo = 'AT' AND Matricula.situacao = 'AT' ");
		sqlStr.append(" WHERE MatriculaPeriodo.situacaomatriculaperiodo  IN ('AT','FI') AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND case when (select subturma from turma where codigo = ").append(turma).append(") then (matriculaperiodoturmadisciplina.turma = ").append(turma).append(" or matriculaperiodoturmadisciplina.turmapratica = ").append(turma).append(" or matriculaperiodoturmadisciplina.turmateorica = ").append(turma).append(") ");
		sqlStr.append(" else (MatriculaPeriodo.turma = ").append(turma).append(" OR MatriculaPeriodo.turma in(select turma from turmaagrupada where turmaorigem = ").append(turma).append("))  end ");

		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append("  ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina).append(" ");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND (matriculaperiodo.ano = '").append(ano).append("' or matriculaperiodo.ano = '') ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND (matriculaperiodo.semestre = '").append(semestre).append("' or matriculaperiodo.semestre = '')");
		}
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	@Override
	public List consultarAlunosDaTurmaReposicaoInclusao(Integer turma, Integer disciplina, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" WHERE mptd.turma <> matriculaperiodo.turma ");
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo IN ('AT','FI') AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND (mptd.turma = ").append(turma).append(" OR MatriculaPeriodo.turma in(select turma from turmaagrupada where turmaorigem = ").append(turma).append(")) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append("  ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" AND mptd.disciplina = ").append(disciplina).append(" ");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND (matriculaperiodo.ano = '").append(ano).append("' or matriculaperiodo.ano = '')");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND (matriculaperiodo.semestre = '").append(semestre).append("' or matriculaperiodo.semestre = '')");
		}
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDoCursoPorNome(String valorConsulta, Integer curso, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN turma ON matriculaperiodo.turma = turma.codigo and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula order by mp.codigo desc limit 1)");
		sqlStr.append(" WHERE curso.codigo = ").append(curso);
		sqlStr.append(" AND upper(pessoa.nome) like ('").append(valorConsulta.toUpperCase()).append("%')");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDoCursoPorCPF(String valorConsulta, Integer curso, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN turma ON matriculaperiodo.turma = turma.codigo and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula order by mp.codigo desc limit 1)");
		sqlStr.append("WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE('");
		sqlStr.append(Uteis.retirarMascaraCPF(valorConsulta));
		sqlStr.append("%')");
		sqlStr.append(" AND curso.codigo = ").append(curso);
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public StringBuilder getSelectConsultaDadosComunicadoInterno() {
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2, pessoa.celular, turma.identificadorturma as turma  FROM pessoa ");
		return sqlStr;
	}

	public List<PessoaVO> montarDadosParaComunicadoInterno(SqlRowSet tabelaResultado) throws Exception {
		PessoaVO pessoaVO = null;
		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>();
		while (tabelaResultado.next()) {
			pessoaVO = new PessoaVO();
			pessoaVO.setNome(tabelaResultado.getString("nome"));
			pessoaVO.setCodigo(tabelaResultado.getInt("codigo"));
			pessoaVO.setEmail(tabelaResultado.getString("email"));
			pessoaVO.setEmail2(tabelaResultado.getString("email2"));
			pessoaVO.setCelular(tabelaResultado.getString("celular"));
			pessoaVO.setInformacoesAdicionais(tabelaResultado.getString("turma"));
			pessoaVOs.add(pessoaVO);
		}
		return pessoaVOs;
	}

	public List consultarAlunosDoProfessor(Integer codigoProfessor, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.turma = matriculaperiodo.turma ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = horarioturmaprofessordisciplina.turma ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa AS pr ON pr.codigo = funcionario.pessoa AND pr.codigo = ").append(codigoProfessor);
		sqlStr.append(" WHERE ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosDoProfessorPorTurma(Integer codigoProfessor, String nomeTurma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.turma = matriculaperiodo.turma ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = horarioturmaprofessordisciplina.turma ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = horarioturmaprofessordisciplina.professor ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa AS pr ON pr.codigo = funcionario.pessoa AND pr.codigo = ").append(codigoProfessor);
		sqlStr.append(" WHERE LOWER (turma.identificadorturma) LIKE('").append(nomeTurma.toLowerCase()).append("%') AND ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND ");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarColegasDoAlunoPorTurma(Integer codigoAluno, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("pr") + " " + "from Turma, Matricula as ma, Matricula as mc, MatriculaPeriodoTurmaDisciplina as mpa, MatriculaPeriodoTurmaDisciplina as mpc, Pessoa as al, Pessoa as pr " + "where mpa.turma = Turma.codigo and mpa.matricula = ma.matricula and ma.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + "" + "and mpc.turma = Turma.codigo and mpc.matricula = mc.matricula and mc.aluno = pr.codigo and pr.codigo != " + codigoAluno.intValue() + " " + "and lower (Turma.identificadorTurma) like('" + valorConsulta.toLowerCase() + "%')" + "Group By  " + getGroupBy("pr");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select " + getGroupBy("pr") + " " + "from UnidadeEnsino, Turma, Matricula as ma, Matricula as mc, MatriculaPeriodoTurmaDisciplina as mpa, MatriculaPeriodoTurmaDisciplina as mpc, Pessoa as al, Pessoa as pr " + "where mpa.turma = Turma.codigo and mpa.matricula = ma.matricula and ma.aluno = al.codigo and al.codigo = " + codigoAluno.intValue() + "" + "and mpc.turma = Turma.codigo and mpc.matricula = mc.matricula and mc.aluno = pr.codigo and pr.codigo != " + codigoAluno.intValue() + " " + "and lower (Turma.identificadorTurma) like('" + valorConsulta.toLowerCase() + "%') and Turma.UnidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " " + "Group By  " + getGroupBy("pr");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNumeroInscricaoProcessoSeletivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM pessoa LEFT JOIN inscricao ON inscricao.candidato = pessoa.codigo");
		sqlStr.append(" WHERE inscricao.codigo >= ").append(valorConsulta.toLowerCase());
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public String getGroupBy(String apelido) {
		return " " + apelido + ".perfilEconomico, " + apelido + ".codigo, " + apelido + ".nome, " + apelido + ".endereco, " + apelido + ".setor, " + apelido + ".numero, " + apelido + ".CEP, " + apelido + ".complemento, " + apelido + ".cidade, " + apelido + ".sexo, " + apelido + ".estadoCivil, " + "" + apelido + ".telefoneComer, " + apelido + ".telefoneRes, " + apelido + ".telefoneRecado, " + apelido + ".celular, " + apelido + ".email, " + apelido + ".email2, " + apelido + ".dataNasc, " + apelido + ".naturalidade, " + apelido + ".nacionalidade, " + apelido + ".CPF, " + apelido + ".RG, " + apelido + ".certificadoMilitar, " + apelido + ".dataEmissaoRG, " + apelido + ".estadoEmissaoRG, " + apelido + ".orgaoEmissor, " + apelido + ".tituloEleitoral, " + apelido + ".necessidadesEspeciais, " + "" + apelido + ".funcionario, " + apelido + ".professor, " + apelido + ".aluno, " + apelido + ".possuiAcessoVisaoPais, " + apelido + ".candidato, " + apelido + ".membroComunidade, " + apelido
				+ ".paginaPessoal, " + apelido + ".atuacomodocente, " + apelido + ".ativo, " + apelido + ".idalunoinep, " + "" + apelido + ".corraca, " + apelido + ".deficiencia, " + apelido + ".passaporte, " + apelido + ".nomefiador, " + apelido + ".enderecofiador, " + apelido + ".telefonefiador, " + apelido + ".cpffiador," + apelido + ".tipoNecessidadesEspeciais," + apelido + ".celularFiador, " + apelido + ".valorcsstopologo, " + apelido + ".valorcssbackground, " + apelido + ".valorcssmenu, " + apelido + ".arquivoImagem, " + apelido + ".coordenador,  " + apelido + ".curriculoAtualizado,  " + apelido + ".espanholNivel,  " + apelido + ".francesNivel,  " + apelido + ".outrosIdiomas,  " + apelido + ".outrosIdiomasNivel,  " + apelido + ".windows,  " + apelido + ".word,  " + apelido + ".powerPoint,  " + apelido + ".internet,  " + apelido + ".sap,  " + apelido + ".corelDraw,  " + apelido + ".pispasep,  " + apelido + ".autoCad,  " + apelido + ".photoshop,  " + apelido + ".microsiga,  "
				+ apelido + ".outrosSoftwares,  " + apelido + ".qtdFilhos,  " + apelido + ".participabancocurriculum,  " + apelido + ".informacoesverdadeiras,  " + apelido + ".informacoesAdicionais,  " + apelido + ".excel,  " + apelido + ".access,  " + apelido + ".divulgarmeusdados,  " + apelido + ".certidaoNascimento,  " + apelido + ".ingles,  " + apelido + ".espanhol,  " + apelido + ".frances,  " + apelido + ".inglesNivel,  " + apelido + ".isentarTaxaBoleto, " + apelido + ".ocultarDadosCRM, " + apelido + ".permiteenviarremessa, " + apelido + ".nomeBatismo, " + apelido + ".tipoAssinaturaDocumentoEnum";

	}

	public List consultarColegasDoAlunoPorCodigoTurma(Integer codigoAluno, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("Pessoa") + " from Turma, Matricula, MatriculaPeriodo, Pessoa " + "where MatriculaPeriodo.turma = Turma.codigo and MatriculaPeriodo.matricula = Matricula.matricula " + "and Matricula.aluno = pessoa.codigo and pessoa.codigo != " + codigoAluno.intValue() + " ";
		if (valorConsulta.intValue() != 0) {
			sqlStr += "and Turma.codigo = " + valorConsulta.intValue() + " ";
		}
		sqlStr += " Group By " + getGroupBy("Pessoa");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarColegasDoAlunoPorCodigoTurmaTodos(Integer codigoAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.codigo = matriculaperiodo.unidadeensinocurso ");
		sqlStr.append(" INNER JOIN processomatricula ON processomatricula.codigo = matriculaperiodo.processomatricula ");		
		sqlStr.append(" INNER JOIN processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatricula.codigo AND processomatriculacalendario.curso = unidadeensinocurso.curso AND processomatriculacalendario.turno = unidadeensinocurso.turno ");
		sqlStr.append(" INNER JOIN periodoletivoativounidadeensinocurso ON periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd ON mptd.matriculaperiodo = matriculaPeriodo.codigo ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" WHERE (mptd.turma <> matriculaperiodo.turma OR mptd.turma = matriculaperiodo.turma) ");
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo = 'AT' AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND mptd.turma = ").append(turma).append(" AND pessoa.codigo != ").append(codigoAluno.intValue());
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" AND CASE WHEN curso.periodicidade = 'IN' THEN true ELSE ");
		sqlStr.append(" periodoletivoativounidadeensinocurso.datainicioperiodoletivo::DATE <= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.datafimperiodoletivo::DATE >= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.situacao = 'AT' END ");
//		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
//		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
//		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarColegasDoAlunoPorCodigoTurmaNormal(Integer codigoAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.codigo = matriculaperiodo.unidadeensinocurso ");
		sqlStr.append(" INNER JOIN processomatricula ON processomatricula.codigo = matriculaperiodo.processomatricula ");
		sqlStr.append(" INNER JOIN processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatricula.codigo AND processomatriculacalendario.curso = unidadeensinocurso.curso AND processomatriculacalendario.turno = unidadeensinocurso.turno ");
		sqlStr.append(" INNER JOIN periodoletivoativounidadeensinocurso ON periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" WHERE MatriculaPeriodo.situacaomatriculaperiodo = 'AT' AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND MatriculaPeriodo.turma = ").append(turma).append(" AND pessoa.codigo != ").append(codigoAluno.intValue());
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" AND CASE WHEN curso.periodicidade = 'IN' THEN true ELSE ");
		sqlStr.append(" periodoletivoativounidadeensinocurso.datainicioperiodoletivo::DATE <= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.datafimperiodoletivo::DATE >= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.situacao = 'AT' END ");
//		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
//		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
//		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarColegasDoAlunoPorCodigoTurmaReposicaoInclusao(Integer codigoAluno, Integer turma, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSelectConsultaDadosComunicadoInterno();
		sqlStr.append(" INNER JOIN matricula ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.codigo = matriculaperiodo.unidadeensinocurso ");
		sqlStr.append(" INNER JOIN processomatricula ON processomatricula.codigo = matriculaperiodo.processomatricula ");
		sqlStr.append(" INNER JOIN processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatricula.codigo AND processomatriculacalendario.curso = unidadeensinocurso.curso AND processomatriculacalendario.turno = unidadeensinocurso.turno ");
		sqlStr.append(" INNER JOIN periodoletivoativounidadeensinocurso ON periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" WHERE mptd.turma <> matriculaperiodo.turma ");
		sqlStr.append(" AND MatriculaPeriodo.situacaomatriculaperiodo = 'AT' AND Matricula.situacao = 'AT' ");
		sqlStr.append(" AND mptd.turma = ").append(turma).append(" AND pessoa.codigo != ").append(codigoAluno.intValue());
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" AND CASE WHEN curso.periodicidade = 'IN' THEN true ELSE ");
		sqlStr.append(" periodoletivoativounidadeensinocurso.datainicioperiodoletivo::DATE <= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.datafimperiodoletivo::DATE >= current_date ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.situacao = 'AT' END ");
//		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
//		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
//		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(tabelaResultado);
	}

	public List consultarAlunosPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "Select " + getGroupBy("Pessoa") + " from Turma, Matricula, MatriculaPeriodo, Pessoa " + "where MatriculaPeriodo.turma = Turma.codigo and MatriculaPeriodo.matricula = Matricula.matricula " + "and Matricula.aluno = pessoa.codigo and Turma.codigo = " + valorConsulta.intValue() + " ";
		sqlStr += " Group By " + getGroupBy("Pessoa");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> atravï¿½s do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parï¿½metro fornecido. Faz uso da operaï¿½ï¿½o
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicaï¿½ï¿½o deverï¿½ verificar se o usuï¿½rio possui
	 *            permissï¿½o para esta consulta ou Não.
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriï¿½ï¿½o de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return this.consultaRapidaPorCodigo(valorConsulta, idEntidade, controlarAcesso, nivelMontarDados, usuario);
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		// String sqlStr = "SELECT * FROM Pessoa WHERE codigo >= " +
		// valorConsulta.intValue() + " ORDER BY codigo";
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		// return (montarDadosConsulta(tabelaResultado, nivelMontarDados,
		// usuario));
	}

	public List consultarCss(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Pessoa WHERE codigo >= " + valorConsulta.intValue() + "";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarProfessoresInteressadosDisciplina(Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select pessoa.* from disciplinasinteresse, pessoa where disciplinasinteresse.disciplina = " + disciplina.intValue() + " and disciplinasinteresse.professor = pessoa.codigo and pessoa.professor = 'true'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vï¿½rios objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operaï¿½ï¿½o
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vï¿½rios objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 */
	public static List<PessoaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		PessoaVO obj = null;
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PessoaVO</code>.
	 *
	 * @return O objeto da classe <code>PessoaVO</code> com os dados devidamente
	 *         montados.
	 */
	public static PessoaVO montarDados(SqlRowSet dadosSQL, PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return montarDados(dadosSQL, obj, false, nivelMontarDados, usuario);
	}

	public static PessoaVO montarDados(SqlRowSet dadosSQL, PessoaVO obj, Boolean funcionario, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// //System.out.println(">> Montar dados(Pessoa) - " + new Date());
		obj = new PessoaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.getNomeOriginal();
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.setCPF(dadosSQL.getString("CPF"));
		obj.setRG(dadosSQL.getString("RG"));
		obj.setRegistroAcademico(dadosSQL.getString("registroAcademico"));
		if (Uteis.NIVELMONTARDADOS_COMBOBOX == nivelMontarDados) {
			if(Uteis.isColunaExistente(dadosSQL, "email")) {
				obj.setEmail(dadosSQL.getString("email"));
			}
			if(Uteis.isColunaExistente(dadosSQL, "email2")) {
				obj.setEmail2(dadosSQL.getString("email2"));
			}
			if(Uteis.isColunaExistente(dadosSQL, "celular")) {
				obj.setCelular(dadosSQL.getString("celular"));
			}
			return obj;
		}		
		obj.setValorCssTopoLogo(dadosSQL.getString("valorCssTopoLogo"));
		obj.setValorCssBackground(dadosSQL.getString("valorCssBackground"));
		obj.setValorCssMenu(dadosSQL.getString("valorCssMenu"));
		obj.setCurriculoAtualizado(dadosSQL.getBoolean("curriculoAtualizado"));
		obj.setNovoObj(false);
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setEstadoCivil(dadosSQL.getString("estadoCivil"));
		obj.setTelefoneComer(dadosSQL.getString("telefoneComer"));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setTelefoneRecado(dadosSQL.getString("telefoneRecado"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.getEmailOriginal();
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setDataNasc(dadosSQL.getDate("dataNasc"));
		obj.setCorRaca(dadosSQL.getString("corraca"));
		obj.getArquivoImagem().setCodigo(dadosSQL.getInt("arquivoImagem"));
		obj.setProfessor(dadosSQL.getBoolean("professor"));
		obj.setAluno(dadosSQL.getBoolean("aluno"));
		obj.setFuncionario(dadosSQL.getBoolean("funcionario"));
		obj.setCandidato(dadosSQL.getBoolean("candidato"));
		obj.setCoordenador(dadosSQL.getBoolean("coordenador"));
		obj.setMembroComunidade(dadosSQL.getBoolean("membroComunidade"));
		obj.setIsentarTaxaBoleto(dadosSQL.getBoolean("isentarTaxaBoleto"));
		obj.setOcultarDadosCRM(dadosSQL.getBoolean("ocultarDadosCRM"));
		obj.setIngles(dadosSQL.getBoolean("ingles"));
		obj.setEspanhol(dadosSQL.getBoolean("espanhol"));
		obj.setFrances(dadosSQL.getBoolean("frances"));
		obj.setInglesNivel(dadosSQL.getString("inglesNivel"));
		obj.setEspanholNivel(dadosSQL.getString("espanholNivel"));
		obj.setFrancesNivel(dadosSQL.getString("francesNivel"));
		obj.setOutrosIdiomas(dadosSQL.getString("outrosIdiomas"));
		obj.setOutrosIdiomasNivel(dadosSQL.getString("outrosIdiomasNivel"));
		obj.getTipoMidiaCaptacao().setCodigo(dadosSQL.getInt("tipomidiacaptacao"));
		obj.setPortadorNecessidadeEspecial(dadosSQL.getBoolean("portadorNecessidadeEspecial"));
		obj.setWindows(dadosSQL.getBoolean("windows"));
		obj.setWord(dadosSQL.getBoolean("word"));
		obj.setExcel(dadosSQL.getBoolean("excel"));
		obj.setAccess(dadosSQL.getBoolean("access"));
		obj.setPowerPoint(dadosSQL.getBoolean("powerPoint"));
		obj.setInternet(dadosSQL.getBoolean("internet"));
		obj.setSap(dadosSQL.getBoolean("sap"));
		obj.setCorelDraw(dadosSQL.getBoolean("corelDraw"));
		obj.setAutoCad(dadosSQL.getBoolean("autoCad"));
		obj.setPhotoshop(dadosSQL.getBoolean("photoshop"));
		obj.setMicrosiga(dadosSQL.getBoolean("microsiga"));
		obj.setOutrosSoftwares(dadosSQL.getString("outrosSoftwares"));
		obj.setQtdFilhos(dadosSQL.getInt("qtdFilhos"));
		obj.setParticipaBancoCurriculum(dadosSQL.getBoolean("participabancocurriculum"));
		obj.setInformacoesVerdadeiras(dadosSQL.getBoolean("informacoesverdadeiras"));
		obj.setInformacoesAdicionais(dadosSQL.getString("informacoesAdicionais"));
		obj.setDivulgarMeusDados(dadosSQL.getBoolean("divulgarmeusdados"));
		obj.setCertidaoNascimento(dadosSQL.getString("certidaoNascimento"));
		obj.setPispasep(dadosSQL.getString("pispasep"));
		obj.setPermiteEnviarRemessa(dadosSQL.getBoolean("permiteenviarremessa"));
		obj.setSabatista(dadosSQL.getBoolean("sabatista"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAssinaturaDocumentoEnum"))){
			obj.setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("tipoAssinaturaDocumentoEnum")));	
		}
		obj.setTempoEstendidoProva(dadosSQL.getBoolean("tempoestendidoprova"));
		
		// ESTES CAMPOS ESTï¿½O NO VO DADOSCOMERCIAIS

		// obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
		// obj.setEnderecoEmpresa(dadosSQL.getString("enderecoEmpresa"));
		// obj.setCargoPessoaEmpresa(dadosSQL.getString("cargoPessoaEmpresa"));
		// obj.setCepEmpresa(dadosSQL.getString("cepEmpresa"));
		// obj.setComplementoEmpresa(dadosSQL.getString("complementoEmpresa"));
		// obj.setSetorEmpresa(dadosSQL.getString("setorEmpresa"));
		// obj.getCidadeEmpresa().setCodigo(dadosSQL.getInt("cidadeEmpresa"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCidade(obj, usuario);
			// montarDadosCidadeEmpresa(obj, usuario);
			return obj;
		}
		obj.setPaginaPessoal(dadosSQL.getString("paginaPessoal"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setCEP(dadosSQL.getString("CEP"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.getPerfilEconomico().setCodigo(dadosSQL.getInt("perfilEconomico"));
		obj.getNaturalidade().setCodigo(dadosSQL.getInt("naturalidade"));
		obj.getNacionalidade().setCodigo(dadosSQL.getInt("nacionalidade"));
		obj.setCertificadoMilitar(dadosSQL.getString("certificadoMilitar"));
		obj.setDataEmissaoRG(dadosSQL.getDate("dataEmissaoRG"));
		obj.setEstadoEmissaoRG(dadosSQL.getString("estadoEmissaoRG"));
		obj.setOrgaoEmissor(dadosSQL.getString("orgaoEmissor"));
		obj.setTituloEleitoral(dadosSQL.getString("tituloEleitoral"));
		obj.setNecessidadesEspeciais(dadosSQL.getString("necessidadesEspeciais"));
		obj.setTipoNecessidadesEspeciais(dadosSQL.getString("tipoNecessidadesEspeciais"));
		// obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
		// obj.setEnderecoEmpresa(dadosSQL.getString("enderecoEmpresa"));
		// obj.setCargoPessoaEmpresa(dadosSQL.getString("cargoPessoaEmpresa"));
		// obj.setCepEmpresa(dadosSQL.getString("cepEmpresa"));
		// obj.setComplementoEmpresa(dadosSQL.getString("complementoEmpresa"));
		// obj.getCidadeEmpresa().setCodigo(dadosSQL.getInt("cidadeEmpresa"));
		obj.setAtuaComoDocente(dadosSQL.getString("atuaComoDocente"));
		obj.setAtivo(dadosSQL.getBoolean("ativo"));

		obj.setIdAlunoInep(dadosSQL.getString("idalunoinep"));
		obj.setPassaporte(dadosSQL.getString("passaporte"));
		obj.setDeficiencia(dadosSQL.getString("deficiencia"));
		obj.setPispasep(dadosSQL.getString("pispasep"));
		obj.setBanco(dadosSQL.getString("banco"));
		obj.setAgencia(dadosSQL.getString("agencia"));
		obj.setContaCorrente(dadosSQL.getString("contaCorrente"));
		obj.setUniversidadeParceira(dadosSQL.getString("universidadeParceira"));
		obj.setModalidadeBolsa(ModalidadeBolsaEnum.getEnumPorValor(dadosSQL.getString("modalidadeBolsa")));	
		obj.setValorBolsa(dadosSQL.getDouble("valorBolsa"));;		
		obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, funcionario, usuario));
//		obj.setPessoaPreInscricaoCursoVOs(PessoaPreInscricaoCurso.consultarPessoaPreInscricaoCursos(obj.getCodigo(), false, usuario));
//		obj.setDadosComerciaisVOs(DadosComerciais.consultarDadosComerciais(obj.getCodigo(), false, funcionario, usuario));
//		obj.setAreaProfissionalInteresseContratacaoVOs(getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().consultarAreaProfissionalInteresseContratacaoPorCodigoPessoa(obj.getCodigo(), false, usuario));
//		obj.setFormacaoExtraCurricularVOs(FormacaoExtraCurricular.consultarFormacaoExtraCurricular(obj.getCodigo(), false, funcionario, usuario));
//		obj.setDisciplinasInteresseVOs(DisciplinasInteresse.consultarDisciplinasInteresses(obj.getCodigo(), false, usuario));
//		obj.setDocumetacaoPessoaVOs(DocumetacaoPessoa.consultarDocumetacaoPessoas(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
		obj.setTranstornosNeurodivergentes(dadosSQL.getString("transtornosNeurodivergentes"));
//		obj.setHorarioProfessorVOs(getFacadeFactory().getHorarioProfessorFacade().consultarHorarioProfessor(obj.getCodigo(), false, null, null, usuario));
		montarDadosArquivo(obj, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSLOGIN) {
			return obj;
		}
		montarDadosCidade(obj, usuario);
		// montarDadosCidadeEmpresa(obj, usuario);
		montarDadosNaturalidade(obj, usuario);
		montarDadosNacionalidade(obj, usuario);
//		montarDadosPerfilEconomico(obj, nivelMontarDados, usuario);
//		montarDadosTipoMidiaCaptacao(obj, usuario);
		obj.setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(obj.getCodigo(), false, usuario));
		
		 
			
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS || nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			getFacadeFactory().getPessoaEmailInstitucionalFacade().preencherPessoaEmailInstitucional(obj, usuario);
			return obj;
		}
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS_CANDIDATO) {
        	obj.setCanhoto(dadosSQL.getBoolean("canhoto"));
    		obj.setGravida(dadosSQL.getBoolean("gravida"));
        	obj.setNomeFiador(dadosSQL.getString("nomeFiador"));
			obj.setCpfFiador(dadosSQL.getString("cpfFiador"));
			obj.setEnderecoFiador(dadosSQL.getString("enderecoFiador"));
			obj.setTelefoneFiador(dadosSQL.getString("telefoneFiador"));
			obj.setCelularFiador(dadosSQL.getString("celularFiador"));
			obj.setCepFiador(dadosSQL.getString("cepFiador"));
			obj.setSetorFiador(dadosSQL.getString("setorFiador"));
			obj.setNumeroEndFiador(dadosSQL.getString("numeroEndFiador"));
		    obj.setDataNascimentoFiador(dadosSQL.getDate("dataNascimentoFiador"));
			obj.setComplementoFiador(dadosSQL.getString("complementoFiador"));			
			obj.getCidadeFiador().setCodigo(dadosSQL.getInt("cidadefiador"));		
			obj.setDataExpedicaoTituloEleitoral(dadosSQL.getDate("dataExpedicaoTituloEleitoral"));			
			obj.setZonaEleitoral(dadosSQL.getString("zonaEleitoral"));
			obj.setSecaoZonaEleitoral(dadosSQL.getString("secaoZonaEleitoral"));
			obj.setDataExpedicaoCertificadoMilitar(dadosSQL.getDate("dataExpedicaoCertificadoMilitar"));
			obj.setOrgaoExpedidorCertificadoMilitar(dadosSQL.getString("orgaoExpedidorCertificadoMilitar"));			
			if (dadosSQL.getString("situacaoMilitar") != null) {
				obj.setSituacaoMilitar(SituacaoMilitarEnum.valueOf(dadosSQL.getString("situacaoMilitar").toUpperCase()));
			} else {
				obj.setSituacaoMilitar(null);
			}
			obj.setNomeCenso(dadosSQL.getString("nomeCenso"));
			obj.setGrauParentesco(dadosSQL.getString("grauParentesco"));
			obj.setSenhaCertificadoParaDocumento(dadosSQL.getString("senhaCertificadoParaDocumento"));
			
			obj.setEstadoCivilFiador(dadosSQL.getString("estadoCivilFiador"));
			obj.setProfissaoFiador(dadosSQL.getString("profissaoFiador"));
			obj.setRgFiador(dadosSQL.getString("rgFiador"));
			obj.setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiAcessoVisaoPais"));
			obj.setEnviarComunicadoPessoa(dadosSQL.getBoolean("enviarcomunicadopessoa"));
			obj.setGerenciaPreInscricao(dadosSQL.getBoolean("gerenciaPreInscricao"));
			obj.getPaisFiador().setCodigo(dadosSQL.getInt("paisfiador"));
			obj.setPossuiFilho(dadosSQL.getBoolean("possuiFilho"));
			return obj;
		}
      
		return obj;
	}

	public static PessoaVO montarDadosEnderecoCompleto(SqlRowSet dadosSQL, PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// //System.out.println(">> Montar dados(Pessoa) - " + new Date());
		obj = new PessoaVO();
		obj.setEndereco(dadosSQL.getString("pessoa_endereco"));
		obj.setCEP(dadosSQL.getString("pessoa_cep"));
		obj.setSetor(dadosSQL.getString("pessoa_setor"));
		obj.setTelefoneRes(dadosSQL.getString("pessoa_telefoneRes"));
		obj.getCidade().setNome(dadosSQL.getString("cidade"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado"));
		obj.getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastabase"));
		obj.getArquivoImagem().setCodigo(dadosSQL.getInt("codigoArquivo"));
		return obj;
	}

	public static void montarDadosArquivo(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getArquivoImagem().getCodigo().intValue() == 0) {
			obj.setArquivoImagem(new ArquivoVO());
			return;
		}
		obj.setArquivoImagem(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoImagem().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosPerfilEconomico(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilEconomico().getCodigo().intValue() == 0) {
			obj.setPerfilEconomico(new PerfilEconomicoVO());
			return;
		}
		obj.setPerfilEconomico(getFacadeFactory().getPerfilEconomicoFacade().consultarPorChavePrimaria(obj.getPerfilEconomico().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operaï¿½ï¿½o Responsável por montar os dados de um objeto da classe
	 * <code>PaizVO</code> relacionado ao objeto <code>PessoaVO</code>. Faz uso
	 * da chave primï¿½ria da classe <code>PaizVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual serï¿½ montado os dados consultados.
	 */
	public static void montarDadosNacionalidade(PessoaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getNacionalidade().getCodigo().intValue() == 0) {
			obj.setNacionalidade(new PaizVO());
			return;
		}
		obj.setNacionalidade(getFacadeFactory().getPaizFacade().consultarPorChavePrimaria(obj.getNacionalidade().getCodigo(), false, usuario));
	}

	/**
	 * Operaï¿½ï¿½o Responsável por montar os dados de um objeto da classe
	 * <code>CidadeVO</code> relacionado ao objeto <code>PessoaVO</code>. Faz
	 * uso da chave primï¿½ria da classe <code>CidadeVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual serï¿½ montado os dados consultados.
	 */
	public static void montarDadosNaturalidade(PessoaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getNaturalidade().getCodigo().intValue() == 0) {
			obj.setNaturalidade(new CidadeVO());
			return;
		}
		obj.setNaturalidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getNaturalidade().getCodigo(), false, usuario));
	}

	/**
	 * Operaï¿½ï¿½o Responsável por montar os dados de um objeto da classe
	 * <code>CidadeVO</code> relacionado ao objeto <code>PessoaVO</code>. Faz
	 * uso da chave primï¿½ria da classe <code>CidadeVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual serï¿½ montado os dados consultados.
	 */
	public static void montarDadosCidade(PessoaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCidade().getCodigo().intValue() == 0) {
			obj.setCidade(new CidadeVO());
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}

	// public static void montarDadosCidadeEmpresa(PessoaVO obj, UsuarioVO
	// usuario) throws Exception {
	// if (obj.getCidadeEmpresa().getCodigo().intValue() == 0) {
	// obj.setCidadeEmpresa(new CidadeVO());
	// return;
	// }
	// obj.setCidadeEmpresa(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidadeEmpresa().getCodigo(),
	// false, usuario));
	// }
	/**
	 * Operaï¿½ï¿½o Responsável por localizar um objeto da classe
	 * <code>PessoaVO</code> atravï¿½s de sua chave primï¿½ria.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou localizaï¿½ï¿½o do objeto
	 *                procurado.
	 */
	public PessoaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorChavePrimaria(codigoPrm, controlarAcesso, false, nivelMontarDados, usuario);
	}

	public PessoaVO consultarEnderecoCompletoPorCodigo(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT pessoa.cep AS pessoa_cep, pessoa.endereco AS pessoa_endereco, pessoa.setor AS pessoa_setor," + " pessoa.telefoneRes AS pessoa_telefoneRes, cidade.nome AS cidade, estado.sigla AS estado, " + " arquivo.pastabasearquivo AS pastabase, arquivo.nome AS nomeArquivo, arquivo.codigo AS codigoArquivo " + " FROM Pessoa " + " LEFT JOIN arquivo ON arquivo.codigo = Pessoa.arquivoimagem " + " LEFT JOIN cidade ON cidade.codigo = Pessoa.cidade " + " LEFT JOIN estado ON cidade.estado = estado.codigo " + " WHERE pessoa.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		PessoaVO obj = null;
		return (montarDadosEnderecoCompleto(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public String consultarEmailCodigo(Integer codigoPrm) throws Exception {
		String sql = "SELECT pessoa.email " + " FROM Pessoa " + " WHERE pessoa.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		return tabelaResultado.getString("email");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String consultarSenhaCertificadoParaDocumento(Integer codigoPrm) throws Exception {
		String sql = "SELECT senhaCertificadoParaDocumento  FROM Pessoa  WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		return tabelaResultado.getString("senhaCertificadoParaDocumento");
	}

	public String consultarNomePorCodigo(Integer codigoPrm) throws Exception {
		String sql = "SELECT pessoa.nome " + " FROM Pessoa " + " WHERE pessoa.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		return tabelaResultado.getString("nome");
	}

	// public List<PessoaVO> consultarAlunosReprovadosPorUnidade(String
	// motivoReprovacao, String tipoRelatorio, Integer unidadeEnsino, Integer
	// codigoAluno, String nomeAluno) throws
	// Exception {
	// StringBuilder sqlStr = new
	// StringBuilder("SELECT distinct pessoa.codigo as codigo, pessoa.nome as nome ");
	// sqlStr.append("FROM pessoa ");
	// sqlStr.append("INNER JOIN matricula  ON matricula.aluno = pessoa.codigo ");
	// sqlStr.append("INNER JOIN historico ON historico.matricula = matricula.matricula ");
	// sqlStr.append("INNER JOIN unidadeensino ue ON m.unidadeensino = ue.codigo ");
	// if (motivoReprovacao.equals("falta")) {
	// sqlStr.append("WHERE h.situacao = 'RF' ");
	// } else if (motivoReprovacao.equals("nota")) {
	// sqlStr.append("WHERE h.situacao = 'RE' ");
	// } else {
	// sqlStr.append("WHERE (h.situacao = 'RE' OR h.situacao = 'RF') ");
	// }
	// sqlStr.append("AND ue.codigo = " + unidadeEnsino.intValue() + " ");
	// if (codigoAluno != -1) {
	// sqlStr.append("AND pessoa.codigo = " + codigoAluno.intValue() + " ");
	// } else {
	// sqlStr.append("AND sem_acentos(pessoa.nome) ilike " +
	// Uteis.removerAcentos(nomeAluno) + " ");
	// }
	//
	//
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	// List<PessoaVO> listaAlunos = new ArrayList<PessoaVO>(0);
	// while (tabelaResultado.next()) {
	// PessoaVO aluno = new PessoaVO();
	// aluno.setCodigo(tabelaResultado.getInt("codigo"));
	// aluno.setNome(tabelaResultado.getString("nome"));
	// listaAlunos.add(aluno);
	// }
	// return listaAlunos;
	// }
	public PessoaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, Boolean funcionario, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Pessoa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, funcionario, nivelMontarDados, usuario));
	}

	@Override
	public PessoaVO consultaRapidaCompletaPorCPFUnico(String cpf, Boolean funcionario, Boolean trazerDisciplinasInteresse, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) = '");
		sqlStr.append(Uteis.retirarMascaraCPF(cpf));
		sqlStr.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		PessoaVO obj = new PessoaVO();
		if (!tabelaResultado.next()) {
			return obj;
		}
		montarDadosCompleto(obj, tabelaResultado, funcionario, trazerDisciplinasInteresse);
		return (obj);
	}

	public PessoaVO consultaRapidaCompletaPorChavePrimaria(Integer codigoPrm, Boolean funcionario, Boolean trazerDisciplinasInteresse, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE pessoa.codigo = ").append(codigoPrm);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		PessoaVO obj = new PessoaVO();
		montarDadosCompleto(obj, tabelaResultado, funcionario, trazerDisciplinasInteresse);
		return (obj);
	}

	public PessoaVO consultaRapidaPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" where pessoa.codigo = ").append(codigoPrm);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			PessoaVO pessoaVO = new PessoaVO();
			montarDadosBasico(pessoaVO, tabelaResultado);
			return pessoaVO;
		}
		return null;
	}

	public PessoaVO consultaRapidaPorContaReceberTipoAlunoSemMatricula(Integer contaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" Inner Join contaReceber cr ON cr.pessoa = pessoa.codigo");
		sqlStr.append(" where cr.codigo = ").append(contaReceber);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			PessoaVO pessoaVO = new PessoaVO();
			montarDadosBasico(pessoaVO, tabelaResultado);
			return pessoaVO;
		}
		return null;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		return getSQLPadraoConsultaBasica(true, false);
	}
	private StringBuffer getSQLPadraoConsultaBasica(Boolean filtrarMatricula, Boolean usarTurmaMatriculaPeriodTurmaDisciplina) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT distinct pessoa.codigo,pessoa.email, pessoa.email2, pessoa.nome,  pessoa.cpf, pessoa.rg, pessoa.certidaoNascimento, pessoa.participabancocurriculum, pessoa.informacoesverdadeiras, pessoa.informacoesAdicionais, pessoa.divulgarmeusdados, pessoa.telefoneRes, pessoa.celular, pessoa.email, pessoa.email2, pessoa.dataUltimaAlteracao,  pessoa.informacoesverdadeiras, pessoa.divulgarmeusdados, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, pessoa.funcionario, pessoa.coordenador, pessoa.professor,  pessoa.nomeBatismo, ");
		str.append(" pessoa.aluno as pessoa_aluno, pessoa.candidato, pessoa.membrocomunidade, pessoa.possuiAcessoVisaoPais, pessoa.requisitante, ");
		str.append(" pessoa.cep, pessoa.endereco, pessoa.complemento, pessoa.setor, pessoa.numero, cidade.codigo AS cidadeCodigo, cidade.nome AS cidadeNome, estado.codigo AS estadoCodigo, estado.nome AS estadoNome, estado.sigla as estadoSigla , pessoa.sexo , pessoa.estadoCivil , pessoa.dataNasc , pessoa.dataEmissaoRg , pessoa.orgaoEmissor , pessoa.naturalidade , pessoa.nacionalidade, pessoa.contacorrente, pessoa.agencia, pessoa.banco  , pessoa.registroAcademico, pessoa.sabatista, pessoa.tipoAssinaturaDocumentoEnum ");
		str.append(" FROM pessoa ");
		str.append("LEFT JOIN cidade on pessoa.cidade = cidade.codigo ");
		str.append("LEFT JOIN estado on estado.codigo = cidade.estado ");
		if(filtrarMatricula) {
				str.append("LEFT JOIN matricula ON matricula.aluno = pessoa.codigo ");
				str.append("LEFT JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = (select codigo from matriculaPeriodo mp where mp.matricula = matricula.matricula order by mp.ano||'/'||mp.semestre desc limit 1 ) ");
				if(usarTurmaMatriculaPeriodTurmaDisciplina) {
					str.append("LEFT JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
					str.append("LEFT JOIN Turma on turma.codigo = mptd.turma ");
				}else {
					str.append("LEFT JOIN Turma on turma.codigo = matriculaPeriodo.turma ");
				}
				str.append("LEFT JOIN UnidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
				str.append("LEFT JOIN curso on curso.codigo = matricula.curso ");
		}
		str.append("LEFT JOIN Arquivo on arquivo.codigo = pessoa.arquivoimagem ");
		return str;
	}


	private StringBuffer getSQLPadraoConsultaBasicaDocumentoAssinado() {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT distinct pessoa.codigo as \"pessoa.codigo\",  ");
		str.append(" pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\", ");
		str.append(" pessoa.email AS \"pessoa.email\", pessoa.email2 AS \"pessoa.email2\", ");
		str.append(" pessoa.endereco AS \"pessoa.endereco\", pessoa.numero AS \"pessoa.numero\", ");
		str.append(" pessoa.setor AS \"pessoa.setor\",  pessoa.senhaCertificadoParaDocumento AS \"pessoa.senhaCertificadoParaDocumento\", ");
		str.append(" pessoa.tipoAssinaturaDocumentoEnum AS \"pessoa.tipoAssinaturaDocumentoEnum\", ");
		str.append(" cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", ");
		str.append(" estado.codigo AS \"estado.codigo\", estado.nome AS \"estado.nome\", estado.sigla AS \"estado.sigla\" ");
		str.append(" FROM pessoa ");
		str.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		str.append("LEFT JOIN estado ON estado.codigo = cidade.estado ");
		return str;
	}

	public List<PessoaVO> montarDadosParaDocumentoAssinado(SqlRowSet tabelaResultado) throws Exception {
		PessoaVO pessoaVO = null;
		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>();
		while (tabelaResultado.next()) {
			pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			pessoaVO.setNome(tabelaResultado.getString("pessoa.nome"));
			pessoaVO.setCPF(tabelaResultado.getString("pessoa.cpf"));
			pessoaVO.setSenhaCertificadoParaDocumento(tabelaResultado.getString("pessoa.senhaCertificadoParaDocumento"));
			if(Uteis.isAtributoPreenchido(tabelaResultado.getString("pessoa.tipoAssinaturaDocumentoEnum"))){
				pessoaVO.setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(tabelaResultado.getString("pessoa.tipoAssinaturaDocumentoEnum")));
			}
			pessoaVO.setEmail(tabelaResultado.getString("pessoa.email"));
			pessoaVO.setEmail2(tabelaResultado.getString("pessoa.email2"));
			pessoaVO.setEndereco(tabelaResultado.getString("pessoa.endereco"));
			pessoaVO.setNumero(tabelaResultado.getString("pessoa.numero"));
			pessoaVO.setSetor(tabelaResultado.getString("pessoa.setor"));
			pessoaVO.getCidade().setCodigo(tabelaResultado.getInt("cidade.codigo"));
			pessoaVO.getCidade().setNome(tabelaResultado.getString("cidade.nome"));
			pessoaVO.getCidade().getEstado().setCodigo(tabelaResultado.getInt("estado.codigo"));
			pessoaVO.getCidade().getEstado().setNome(tabelaResultado.getString("estado.nome"));
			pessoaVO.getCidade().getEstado().setSigla(tabelaResultado.getString("estado.sigla"));
			pessoaVOs.add(pessoaVO);
		}
		return pessoaVOs;
	}


	public List consultaRapidaAlunoPorNomeVisaoAluno(String valorConsulta, Integer codAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT distinct pessoa.codigo,pessoa.email, pessoa.email2, pessoa.nome,  pessoa.cpf, pessoa.rg, pessoa.certidaoNascimento, pessoa.participabancocurriculum, pessoa.informacoesverdadeiras, pessoa.informacoesAdicionais, pessoa.divulgarmeusdados, pessoa.telefoneRes, pessoa.celular, pessoa.email, pessoa.email2, pessoa.dataUltimaAlteracao,  pessoa.informacoesverdadeiras, pessoa.divulgarmeusdados, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, pessoa.funcionario, pessoa.coordenador, pessoa.professor FROM pessoa ");
		sqlStr.append("LEFT JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append("LEFT JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append("LEFT JOIN Turma on turma.codigo = matriculaPeriodo.turma ");
		sqlStr.append("LEFT JOIN UnidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append("LEFT JOIN Arquivo on arquivo.codigo = pessoa.arquivoimagem ");
		sqlStr.append("WHERE lower(pessoa.nome) like(?)");
		sqlStr.append(" and pessoa.codigo <>  ").append(codAluno);
		sqlStr.append(" and pessoa.aluno = 'TRUE' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" and turma.codigo in ");
		sqlStr.append(" ( select Distinct turma.codigo from turma  ");
		sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodo.turma = turma.codigo ");
		sqlStr.append(" inner join matricula on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno  and pessoa.codigo =  ");
		sqlStr.append(codAluno.intValue());
		sqlStr.append(")");
		sqlStr.append(" Group By pessoa.codigo, pessoa.email, pessoa.email2, pessoa.nome,  pessoa.cpf, pessoa.rg, pessoa.certidaoNascimento, pessoa.participabancocurriculum, pessoa.informacoesverdadeiras, pessoa.informacoesAdicionais, pessoa.divulgarmeusdados, pessoa.telefoneRes, pessoa.celular, pessoa.email, pessoa.email2, pessoa.dataUltimaAlteracao,  pessoa.informacoesverdadeiras, pessoa.divulgarmeusdados, arquivo.codigo , arquivo.pastaBaseArquivo, arquivo.nome, pessoa.funcionario, pessoa.coordenador, pessoa.professor   ");
		sqlStr.append(" order by  pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}

	public List<PessoaVO> consultaRapidaFuncionariosPorCodigoDepartamento(Integer departamento, Integer unidadeEnsino, Boolean pessoasAtivas, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN funcionario ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" INNER JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		sqlStr.append(" INNER JOIN cargo on cargo.codigo = funcionarioCargo.cargo ");
		sqlStr.append(" inner JOIN departamento ON ((funcionarioCargo.departamento is not null and funcionarioCargo.departamento = departamento.codigo) or (funcionarioCargo.departamento is null and cargo.departamento = departamento.codigo)) ");
		sqlStr.append(" LEFT JOIN unidadeEnsino ON funcionarioCargo.unidadeEnsino =  unidadeEnsino.codigo ");
		sqlStr.append(" WHERE pessoa.funcionario = true ");
		if (pessoasAtivas != null) {
			sqlStr.append(" AND pessoa.ativo = ").append(pessoasAtivas).append(" ");
		}
		if (Uteis.isAtributoPreenchido(departamento)) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento).append(" ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaFuncionariosPorCodigoCargo(Integer cargo, Integer unidadeEnsino, Boolean pessoasAtivas, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN funcionario ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" INNER JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		sqlStr.append(" INNER JOIN cargo on cargo.codigo = funcionarioCargo.cargo ");
		sqlStr.append(" LEFT JOIN unidadeEnsino ON funcionarioCargo.unidadeEnsino =  unidadeEnsino.codigo ");
		sqlStr.append(" WHERE pessoa.funcionario = true ");
		if (pessoasAtivas != null) {
			sqlStr.append(" AND pessoa.ativo = ").append(pessoasAtivas).append(" ");
		}
		if (cargo != null && !cargo.equals(0)) {
			sqlStr.append(" AND cargo.codigo = ").append(cargo).append(" ");
		}
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append("");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaPorCodigoAreaConhecimento(Integer areaConhecimento, Integer unidadeEnsino, Boolean pessoasAtivas, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN areaConhecimento ON areaConhecimento.pessoa = pessoa.codigo ");
		sqlStr.append(" WHERE pessoa.ativo = ").append(pessoasAtivas).append(" ");
		if (areaConhecimento != null && !areaConhecimento.equals(0)) {
			sqlStr.append(" AND areaConhecimento.codigo = ").append(areaConhecimento).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaAlunoVisaoAluno(Integer codAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE pessoa.aluno = 'TRUE' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" and turma.codigo in ");
		sqlStr.append(" ( select Distinct turma.codigo from turma  ");
		sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodo.turma = turma.codigo ");
		sqlStr.append(" inner join matricula on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno  and pessoa.codigo =  ");
		sqlStr.append(codAluno.intValue());
		sqlStr.append(")");
		sqlStr.append("Group By pessoa.codigo, pessoa.nome, pessoa.cpf, pessoa.rg, pessoa.telefoneRes, pessoa.dataultimaalteracao, pessoa.certidaoNascimento, pessoa.informacoesverdadeiras,pessoa.informacoesAdicionais, pessoa.divulgarmeusdados, pessoa.participabancocurriculum, pessoa.celular, pessoa.email, pessoa.email2, arquivo.codigo, arquivo.pastaBaseArquivo, arquivo.nome, turma.identificadorturma, cidade.codigo, estado.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaAlunoPorTurmaVisaoAluno(String valorConsulta, Integer codAluno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE turma.identificadorTurma ilike('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		sqlStr.append(" and pessoa.aluno = 'TRUE' ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and unidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" and turma.codigo in ");
		sqlStr.append(" ( select Distinct turma.codigo from turma  ");
		sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodo.turma = turma.codigo ");
		sqlStr.append(" inner join matricula on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno  and pessoa.codigo =  ");
		sqlStr.append(codAluno.intValue());
		sqlStr.append(")");
		sqlStr.append("Group By pessoa.codigo, pessoa.nome, pessoa.cpf, pessoa.rg, pessoa.telefoneRes, pessoa.certidaoNascimento, pessoa.celular, pessoa.participabancocurriculum, pessoa.email, pessoa.email2, arquivo.codigo, pessoa.dataultimaalteracao, arquivo.pastaBaseArquivo, arquivo.nome, pessoa.informacoesverdadeiras, pessoa.informacoesAdicionais, pessoa.divulgarmeusdados, turma.identificadorturma, cidade.codigo, estado.codigo  ");
		sqlStr.append(" order by  pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Carlos
	 */
	public List<PessoaVO> consultaRapidaAlunoMatriculadoPorNome(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE LOWER (pessoa.nome) LIKE(?)");
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" AND matricula.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Carlos
	 */
	public List<PessoaVO> consultaRapidaAlunoPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, boolean somenteAtivo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(true, true);
		sqlStr.append("WHERE turma.codigo = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" and matricula.situacao = 'AT' ");
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaAlunoPorCodigoTurmaSituacaoMatriculaPeriodo(Integer valorConsulta, String situacaoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE turma.codigo = ");
		sqlStr.append(valorConsulta.intValue());
		sqlStr.append(" AND matriculaperiodo.situacaoMatriculaperiodo = '").append(situacaoMatriculaPeriodo).append("'");
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Alberto
	 */
	public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaSituacao(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		sqlStr.append("LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append("WHERE mptd.turma = ");
		sqlStr.append(codigoTurma.intValue());
		sqlStr.append(" AND matricula.situacao = '").append(situacaoMatricula).append("' ");
		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaPadraoSituacao(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		sqlStr.append("LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append("WHERE mptd.turma = ").append(codigoTurma);
		sqlStr.append(" AND matriculaperiodo.turma = mptd.turma");
		sqlStr.append(" AND matricula.situacao = '").append(situacaoMatricula).append("' ");
		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaInclusaoSituacao(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		sqlStr.append("LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append("WHERE mptd.turma = ").append(codigoTurma);
		sqlStr.append(" AND matriculaperiodo.turma != mptd.turma");
		sqlStr.append(" AND matricula.situacao = '").append(situacaoMatricula).append("' ");
		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaInclusaoSituacaoDisciplinas(Integer codigoTurma, String situacaoMatricula, String ano, String semestre, List<Integer> listaCodigoDisciplinas, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("LEFT JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		sqlStr.append("LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append("WHERE mptd.turma = ").append(codigoTurma);
		sqlStr.append(" AND matriculaperiodo.turma != mptd.turma");
		if (!listaCodigoDisciplinas.isEmpty()) {
			sqlStr.append(" AND mptd.disciplina in(");
			for (Integer codigo : listaCodigoDisciplinas) {
				sqlStr.append(codigo);
				if (!codigo.equals(listaCodigoDisciplinas.get(listaCodigoDisciplinas.size() - 1))) {
					sqlStr.append(", ");
				}
			}
			sqlStr.append(")");
		}
		sqlStr.append(" AND matricula.situacao = '").append(situacaoMatricula).append("' ");
		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override
	public List<PessoaVO> consultaRapidaAlunosPorCodigoTurmaSituacaoEDisciplina(TurmaVO turmaVO, String situacaoMatricula, Integer codigoDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append("INNER JOIN matricula on matricula.aluno = pessoa.codigo ");
		sqlStr.append("INNER JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matricula = matricula.matricula ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = mptd.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = mptd.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON mptd.turma = turma.codigo ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON mptd.turma = turma.codigo ");
		}
		sqlStr.append(" WHERE ");
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turmaVO.getCodigo() + ")");
		} else {
			sqlStr.append(" Turma.codigo  = " + turmaVO.getCodigo());
		}
		if (codigoDisciplina != null && codigoDisciplina > 0) {
			sqlStr.append(" AND mptd.disciplina = ").append(codigoDisciplina);
		}
		if (situacaoMatricula != null && !situacaoMatricula.trim().isEmpty()) {
			sqlStr.append(" AND matricula.situacao = '").append(situacaoMatricula).append("' ");
			sqlStr.append(" AND matriculaperiodo.situacaomatriculaperiodo = '").append(situacaoMatricula).append("' ");
		}
		sqlStr.append(" AND historico.situacao in ('CS', 'AP', 'RE', 'RF') ");
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Carlos
	 */
	public List<PessoaVO> consultaRapidaAlunoMatriculadoPorCPF(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE LOWER (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE('");
		sqlStr.append(Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()));
		sqlStr.append("%')");
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" AND matricula.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		sqlStr.append(" ORDER BY pessoa.cpf");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorCPF(valorConsulta, tipoPessoa, controlarAcesso, nivelMontarDados, false, usuario);
	}
	
	public List<PessoaVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, boolean funcionarioObrigatorio, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append("WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");
		if (funcionarioObrigatorio) {
			sqlStr.append(" AND EXISTS (SELECT FROM funcionario WHERE funcionario.pessoa = pessoa.codigo) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" AND pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals(TipoPessoa.COORDENADOR_CURSO.getValor())) {
			sqlStr.append(" and exists (select codigo from cursocoordenador where cursocoordenador.funcionario = funcionario.codigo limit 1)");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta) + PERCENT);
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Metodo responsavel por consultar a pessoa por cpf vinculada a unidade de
	 * ensino
	 */
	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorCPF(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer(getSQLPadraoConsultaBasica(true, false));
		sqlStr.append("WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE('");
		sqlStr.append(Uteis.retirarMascaraCPF(valorConsulta));
		sqlStr.append("%')");
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" AND pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("MC")) {
			sqlStr.append(" and membroComunidade = 'true'");
		}
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaResumidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE replace(replace((pessoa.cpf),'.',''),'-','') LIKE('");
		sqlStr.append(Uteis.retirarMascaraCPF(valorConsulta));
		sqlStr.append("%')");
		sqlStr.append(" AND pessoa.aluno = 'true'");
		sqlStr.append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaResumidaPorCPF(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE replace(replace((pessoa.cpf),'.',''),'-','') LIKE('");
			sqlStr.append(Uteis.retirarMascaraCPF(valorConsulta));
			sqlStr.append("%')");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		//sqlStr.append(" AND (pessoa.aluno = 'true'  or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaResumidaPorCPF(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT COUNT(DISTINCT pessoa.nome) from pessoa ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE replace(replace((pessoa.cpf),'.',''),'-','') LIKE('");
			sqlStr.append(Uteis.retirarMascaraCPF(valorConsulta));
			sqlStr.append("%')");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;

	}

	public List<PessoaVO> consultaRapidaResumidaPorRG(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE LOWER (pessoa.rg) LIKE('");
			sqlStr.append(valorConsulta.toLowerCase());
			sqlStr.append("%')");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		//sqlStr.append(" AND (pessoa.aluno = 'true'  or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaResumidaPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append("WHERE LOWER (pessoa.rg) LIKE('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		sqlStr.append(" AND pessoa.aluno = 'true'");
		sqlStr.append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaResumidaPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT COUNT(pessoa.codigo) From pessoa ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE LOWER (pessoa.rg) LIKE('");
			sqlStr.append(valorConsulta.toLowerCase());
			sqlStr.append("%')");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;

	}

	public List<PessoaVO> consultaRapidaPorRG(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append("WHERE LOWER (pessoa.rg) LIKE('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true'  or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" AND pessoa.funcionario = 'true'");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Metodo responsavel por consultar a pessoa por RG vinculada a unidade de
	 * ensino
	 */
	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorRG(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer(getSQLPadraoConsultaBasica(true, false));
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append("WHERE LOWER (pessoa.rg) LIKE('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')");
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" AND pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("MC")) {
			sqlStr.append(" and membroComunidade = 'true'");
		}
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append("WHERE sem_acentos((pessoa.nome)) iLIKE(trim(sem_acentos(?)))");
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" AND pessoa.aluno = 'true'");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" AND pessoa.professor = 'true'");
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr.append(" AND pessoa.funcionario = 'true'");
			}
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Metodo responsavel por consultar a pessoa por nome vinculada a unidade de
	 * ensino
	 */
	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorNome(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorUnidadeEnsinoPorNome(valorConsulta, unidadeEnsinoVOs, tipoPessoa, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorNome(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append(" WHERE sem_acentos((pessoa.nome)) iLIKE(trim(sem_acentos(?)))");
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" AND pessoa.aluno = 'true'");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" AND pessoa.professor = 'true'");
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr.append(" AND pessoa.funcionario = 'true'");
			}
			if (tipoPessoa.equals("MC")) {
				sqlStr.append(" and membroComunidade = 'true'");
			}
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND unidadeEnsino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaPorNomePessoaAutoComplete(String valorConsulta, String tipoPessoa, int limit, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append("WHERE sem_acentos((pessoa.nome)) iLIKE(trim(sem_acentos(?)))");
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" AND pessoa.aluno = 'true'");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" AND pessoa.professor = 'true'");
			}
			if (tipoPessoa.equals("FU")) {
				sqlStr.append(" AND pessoa.funcionario = 'true'");
			}
			if (unidade.intValue() != 0) {
				sqlStr.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
			}
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		sqlStr.append(" limit ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> consultaRapidaResumidaPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append("WHERE (sem_acentos(Pessoa.nome) ilike sem_acentos(?)) ");
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}

		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public List consultaRapidaResumidaPorMatricula(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(true, false);
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (sem_acentos(Matricula.matricula) ilike sem_acentos(?)) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaRapida(tabelaResultado);
		} else {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			return montarDadosConsultaRapida(tabelaResultado);
		}
	}

	public List<PessoaVO> consultaRapidaResumidaPorNome(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (f_sem_acentos(Pessoa.nome) ilike f_sem_acentos(?)) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor ");
		}

		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaRapida(tabelaResultado);
		} else {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			return montarDadosConsultaRapida(tabelaResultado);
		}
	}
	
	@Override                                         
	public List<PessoaVO> consultaRapidaResumidaPorRegistroAcademico(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados,DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);		
		SqlRowSet tabelaResultado = null ;	
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sqlStr.append(getSQLPadraoConsultaBasica(false, false));		
		
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE Pessoa.registroAcademico ilike  ?  ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor ");
		}
		sqlStr.append(" ) AS t ");		
	
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		 dataModelo.setTotalRegistrosEncontrados(0);
		if (valorConsulta.replace("%", "").trim().isEmpty()) {			
			 tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			}
			tabelaResultado.beforeFirst();	
			
		} else {
			 tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			 if (tabelaResultado.next()) {
					dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
				}
			 tabelaResultado.beforeFirst();	
		}
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public Integer consultaTotalDeRegistroRapidaResumidaPorMatricula(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT COUNT(pessoa.codigo) FROM pessoa ");
		sqlStr.append(" LEFT JOIN matricula on pessoa.codigo = matricula.aluno ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (sem_acentos(matricula.matricula) ilike sem_acentos(?)) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			}
		} else {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			}
		}
		return 0;
	}

	public Integer consultaTotalDeRegistroRapidaResumidaPorNome(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT COUNT(pessoa.codigo) FROM pessoa ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (f_sem_acentos(Pessoa.nome) ilike f_sem_acentos(?)) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}

		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			}
		} else {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			}
		}
		return 0;
	}

	@Override
	public List<PessoaVO> consultaRapidaResumidaPorNomeMae(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" inner join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'MA' ");
		sqlStr.append(" inner join Pessoa mae on filiacao.pais = mae.codigo ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (sem_acentos(mae.nome) ilike sem_acentos(?)) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}

		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaRapida(tabelaResultado);
		} else {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			return montarDadosConsultaRapida(tabelaResultado);
		}
	}

	@Override
	public Integer consultaTotalDeRegistroRapidaResumidaPorNomeMae(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT COUNT(distinct pessoa.codigo) FROM pessoa ");
		sqlStr.append(" inner join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'MA' ");
		sqlStr.append(" inner join Pessoa mae on filiacao.pais = mae.codigo ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append(" WHERE (sem_acentos(mae.nome) ilike sem_acentos(?)) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}

		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			}
		} else {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("count");
			}
		}
		return 0;
	}
	
	@Override
	public void consultaPessoaDataModeloPorNome(DataModelo dataModelo, TipoPessoa tipoPessoa, UsuarioVO usuario) throws Exception {
		dataModelo.setListaConsulta(consultaPorNome(dataModelo, tipoPessoa, usuario));
		dataModelo.setTotalRegistrosEncontrados(consultaTotalPorNome(dataModelo, tipoPessoa, usuario));
	}

	public List<PessoaVO> consultaPorNome(DataModelo dataModelo, TipoPessoa tipoPessoa, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT codigo, nome, professor, aluno FROM pessoa ");
		sql.append("WHERE (f_sem_acentos(Pessoa.nome) ilike f_sem_acentos(?)) ");

		switch (tipoPessoa) {
		case ALUNO:
			sql.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
			break;
		case PROFESSOR:
			sql.append(" AND pessoa.professor = 'true'");
			break;
		default:
			break;
		}

		sql.append(" ORDER BY pessoa.nome");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Uteis.removerAcentuacao(dataModelo.getValorConsulta().toLowerCase()) + "%" );
		PessoaVO obj = new PessoaVO();
		List<PessoaVO> lista = new ArrayList<>(0);
		while(tabelaResultado.next()) {
			obj = new PessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			lista.add(obj);
		}
		return lista;
	}

	public Integer consultaTotalPorNome(DataModelo dataModelo, TipoPessoa tipoPessoa, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(pessoa.codigo) as qtde FROM pessoa ");
		sql.append("WHERE (f_sem_acentos(Pessoa.nome) ilike f_sem_acentos(?)) ");

		switch (tipoPessoa) {
		case ALUNO:
			sql.append(" AND (pessoa.aluno = 'true' or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
			break;
		case PROFESSOR:
			sql.append(" AND pessoa.professor = 'true'");
			break;
		default:
			break;
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Uteis.removerAcentuacao(dataModelo.getValorConsulta().toLowerCase()) + "%" );
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	/**
	 * Consulta que espera um ResultSet com os campos mï¿½nimos para uma consulta
	 * rï¿½pida e intelegente. Desta maneira, a mesma serï¿½ sempre capaz de montar
	 * os atributos bï¿½sicos do objeto e alguns atributos relacionados de
	 * relevï¿½ncia para o contexto da aplicaï¿½ï¿½o.
	 *
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(PessoaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Pessoa
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.getNomeOriginal();
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.setCPF(dadosSQL.getString("cpf"));
		obj.setRG(dadosSQL.getString("rg"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.getEmailOriginal();
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.setParticipaBancoCurriculum(dadosSQL.getBoolean("participabancocurriculum"));
		obj.setInformacoesVerdadeiras(dadosSQL.getBoolean("informacoesverdadeiras"));
		obj.setInformacoesAdicionais(dadosSQL.getString("informacoesAdicionais"));
		obj.setDivulgarMeusDados(dadosSQL.getBoolean("divulgarmeusdados"));
		obj.setCertidaoNascimento(dadosSQL.getString("certidaoNascimento"));
		obj.setProfessor(dadosSQL.getBoolean("professor"));
		obj.setCoordenador(dadosSQL.getBoolean("coordenador"));
		obj.setFuncionario(dadosSQL.getBoolean("funcionario"));
		obj.setAluno(dadosSQL.getBoolean("pessoa_aluno"));
		obj.setCandidato(dadosSQL.getBoolean("candidato"));
		obj.setMembroComunidade(dadosSQL.getBoolean("membrocomunidade"));
		obj.setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiAcessoVisaoPais"));
		obj.setRequisitante(dadosSQL.getBoolean("requisitante"));
		obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidadecodigo")));
		obj.getCidade().setNome(dadosSQL.getString("cidadenome"));
		obj.setContaCorrente(dadosSQL.getString("contacorrente"));
		obj.setAgencia(dadosSQL.getString("agencia"));
		obj.setBanco(dadosSQL.getString("banco"));
        obj.setRegistroAcademico(dadosSQL.getString("registroAcademico"));
        obj.setSabatista(dadosSQL.getBoolean("sabatista"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAssinaturaDocumentoEnum"))){
        	obj.setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("tipoAssinaturaDocumentoEnum")));
        }
		obj.setNovoObj(false);
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
	}
	
	private void montarDadosBasicoPessoaRemessa(PessoaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Pessoa
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setCPF(dadosSQL.getString("cpf"));
		obj.setRG(dadosSQL.getString("rg"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.setParticipaBancoCurriculum(dadosSQL.getBoolean("participabancocurriculum"));
		obj.setInformacoesVerdadeiras(dadosSQL.getBoolean("informacoesverdadeiras"));
		obj.setSabatista(dadosSQL.getBoolean("sabatista"));
		obj.setInformacoesAdicionais(dadosSQL.getString("informacoesAdicionais"));
		obj.setDivulgarMeusDados(dadosSQL.getBoolean("divulgarmeusdados"));
		obj.setCertidaoNascimento(dadosSQL.getString("certidaoNascimento"));
		obj.setProfessor(dadosSQL.getBoolean("professor"));
		obj.setCoordenador(dadosSQL.getBoolean("coordenador"));
		obj.setFuncionario(dadosSQL.getBoolean("funcionario"));
		
		obj.setCEP(dadosSQL.getString("cep"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		
		// DADOS CIDADE
		obj.getCidade().setCodigo(dadosSQL.getInt("cidadeCodigo"));
		obj.getCidade().setNome(dadosSQL.getString("cidadeNome"));
				
		// DADOS ESTADO
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("estadoCodigo"));
		obj.getCidade().getEstado().setNome(dadosSQL.getString("estadoNome"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estadoSigla"));
		
		obj.setNovoObj(false);
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
	}

	/**
	 * Consulta que espera um ResultSet com os campos mï¿½nimos para uma consulta
	 * rï¿½pida e intelegente. Desta maneira, a mesma serï¿½ sempre capaz de montar
	 * os atributos bï¿½sicos do objeto e alguns atributos relacionados de
	 * relevï¿½ncia para o contexto da aplicaï¿½ï¿½o.
	 *
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasicoVisaoAluno(PessoaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Pessoa
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setCPF(dadosSQL.getString("cpf"));
		obj.getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
	}

	public List<PessoaVO> consultaRapidaPorCodigo(Integer valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE pessoa.codigo = ");
		sqlStr.append(valorConsulta.intValue());
		if (!tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" AND pessoa.aluno = 'true'");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" AND pessoa.professor = 'true'");
			}
		}
		sqlStr.append(" ORDER BY pessoa.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<PessoaVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<PessoaVO> montarDadosConsultaRapidaVisaoAluno(SqlRowSet tabelaResultado) throws Exception {
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasicoVisaoAluno(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void carregarDados(PessoaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((PessoaVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(PessoaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((PessoaVO) obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((PessoaVO) obj, resultado);
		}
	}
	
	@Override
	public void carregarDadosPessoaRemessaContaPagar(PessoaVO obj, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if (nivelmontardadosDadosminimos == Uteis.NIVELMONTARDADOS_DADOSBASICOS && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasicoPessoaRemessa((PessoaVO) obj, resultado);
		}
		if (nivelmontardadosDadosminimos == Uteis.NIVELMONTARDADOS_TODOS && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((PessoaVO) obj, resultado);
		}
		
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codPessoa, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Pessoa.codigo= ").append(codPessoa).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codPessoa, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(codPessoa).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	/**
	 * Mï¿½todo responsavel por fazer uma seleï¿½ï¿½o completa da Entidade Unidade
	 * Ensino e mais algumas outras entidades que possuem relacionamento com a
	 * mesma. ï¿½ uma consulta que busca completa e padrï¿½o.
	 *
	 * @return List Contendo vï¿½rios objetos da classe
	 * @author Carlos
	 */
	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT pessoa.codigo, pessoa.sexo, pessoa.contacorrente, pessoa.agencia, pessoa.banco, pessoa.universidadeparceira, pessoa.modalidadebolsa, pessoa.valorbolsa,pessoa.tempoestendidoprova, pessoa.permiteEnviarRemessa as \"pessoa.permiteEnviarRemessa\", pessoa.ocultarDadosCRM as ocultarDadosCRM,  pessoa.isentarTaxaBoleto as isentarTaxaBoleto, pessoa.codProspect AS codProspect, pessoa.curriculoatualizado AS curriculoatualizado, pessoa.*, cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", cidade.cep AS \"cidade.cep\", pessoa.pispasep, ");
		str.append("cidade.estado AS \"cidade.estado\", cidade.codigoibge AS \"cidade.codigoibge\", pessoa.curriculoAtualizado AS \"pessoa.curriculoAtualizado\", cidade.codigoInep AS \"cidade.codigoInep\", ");
		str.append("estado.codigo AS \"estado.codigo\", estado.nome AS \"estado.nome\", estado.sigla AS \"estado.sigla\", estado.paiz AS \"estado.paiz\", ");
		str.append("estado.codigoInep AS \"estado.codigoInep\", paiz.codigo AS \"nacionalidade.codigo\", paiz.nacionalidade AS \"nacionalidade.nome\", ");
		str.append("naturalidade.codigo AS \"naturalidade.codigo\", naturalidade.nome AS \"naturalidade.nome\", naturalidade.estado AS \"naturalidade.estado\", estadoNaturalidade.codigo AS \"estadoNaturalidade.codigo\",  estadoNaturalidade.nome AS \"estadoNaturalidade.nome\", estadoNaturalidade.sigla AS \"estadoNaturalidade.sigla\", pessoa.gravida AS \"pessoa.gravida\", pessoa.canhoto AS \"pessoa.canhoto\", pessoa.portadorNecessidadeEspecial AS \"pessoa.portadorNecessidadeEspecial\", ");
//		str.append("perfilEconomico.codigo AS \"perfilEconomico.codigo\", perfilEconomico.nome AS \"perfilEconomico.nome\", ");
		str.append("formacaoAcademica.codigo AS \"formacaoAcademica.codigo\", formacaoAcademica.instituicao AS \"formacaoAcademica.instituicao\", formacaoAcademica.tipoInst AS \"formacaoAcademica.tipoInst\", ");
		str.append("formacaoAcademica.escolaridade AS \"formacaoAcademica.escolaridade\", formacaoAcademica.curso AS \"formacaoAcademica.curso\", ");
		str.append("formacaoAcademica.situacao AS \"formacaoAcademica.situacao\", formacaoAcademica.dataInicio AS \"formacaoAcademica.dataInicio\", formacaoAcademica.modalidade AS \"formacaoAcademica.modalidade\",  ");
		str.append("formacaoAcademica.dataFim AS \"formacaoAcademica.dataFim\", formacaoAcademica.pessoa AS \"formacaoAcademica.pessoa\", ");
		str.append("formacaoAcademica.anoDataFim AS \"formacaoAcademica.anoDataFim\", formacaoAcademica.semestreDataFim AS \"formacaoAcademica.semestreDataFim\", ");
		str.append("areaConhecimento.codigo AS \"formacaoAcademica.areaConhecimento.codigo\", areaConhecimento.nome AS \"formacaoAcademica.areaConhecimento.nome\", ");
		str.append("cidadeFormacao.codigo AS \"formacaoAcademica.cidade.codigo\", cidadeFormacao.nome AS \"formacaoAcademica.cidade.nome\", ");
		str.append("estadoFormacao.codigo AS \"formacaoAcademica.estado.codigo\", estadoFormacao.nome AS \"formacaoAcademica.estado.nome\", ");
		str.append("estadoFormacao.sigla AS \"formacaoAcademica.estado.sigla\", estadoFormacao.paiz AS \"formacaoAcademica.estado.paiz\", formacaoAcademica.titulo AS \"formacaoAcademica.titulo\", ");
		//str.append("dadosComerciais.codigo AS \"dadosComerciais.codigo\", dadosComerciais.nomeEmpresa AS \"dadosComerciais.nomeEmpresa\", ");
		//str.append("dadosComerciais.enderecoEmpresa AS \"dadosComerciais.enderecoEmpresa\", dadosComerciais.cargoPessoaEmpresa AS \"dadosComerciais.cargoPessoaEmpresa\", ");
		//str.append("dadosComerciais.cepEmpresa AS \"dadosComerciais.cepEmpresa\", dadosComerciais.complementoEmpresa AS \"dadosComerciais.complementoEmpresa\", ");
		//str.append("cidadeEmpresa.codigo AS \"dadosComerciais.codigoCidadeEmpresa\", cidadeEmpresa.nome AS \"dadosComerciais.nomeCidadeEmpresa\", ");
//		str.append("dadosComerciais.setorEmpresa AS \"dadosComerciais.setorEmpresa\", dadosComerciais.telefoneComer AS \"dadosComerciais.telefoneComer\", ");
//		str.append("dadosComerciais.empregoAtual AS \"dadosComerciais.empregoAtual\", dadosComerciais.principaisAtividades AS \"dadosComerciais.principaisAtividades\", ");
//		str.append("dadosComerciais.salario AS \"dadosComerciais.salario\", dadosComerciais.motivoDesligamento AS \"dadosComerciais.motivoDesligamento\", ");
//		str.append("dadosComerciais.dataAdmissao AS \"dadosComerciais.dataAdmissao\", dadosComerciais.dataDemissao AS \"dadosComerciais.dataDemissao\", ");
//		str.append("dadosComerciais.pessoa AS \"dadosComerciais.pessoa\", formacaoExtraCurricular.codigo AS \"formacaoExtraCurricular.codigo\", ");
//		str.append("formacaoExtraCurricular.instituicao AS \"formacaoExtraCurricular.instituicao\", formacaoExtraCurricular.curso AS \"formacaoExtraCurricular.curso\", ");
//		str.append("formacaoExtraCurricular.anoRealizacaoConclusao AS \"formacaoExtraCurricular.anoRealizacaoConclusao\", ");
//		str.append("formacaoExtraCurricular.cargaHoraria AS \"formacaoExtraCurricular.cargaHoraria\", ");
//		str.append("formacaoExtraCurricular.pessoa AS \"formacaoExtraCurricular.pessoa\", ");
		str.append("filiacao.codigo AS \"filiacao.codigo\", pais.nome AS \"filiacao.nome\", ");
		str.append("filiacao.tipo AS \"filiacao.tipo\", ");
		str.append(" filiacao.aluno AS \"filiacao.aluno\", filiacao.responsavelFinanceiro AS \"filiacao.responsavelFinanceiro\", filiacao.responsavelLegal AS \"filiacao.responsavelLegal\" ,");
		str.append("arquivo.pastaBaseArquivo, arquivo.codigo AS codArquivo, arquivo.nome AS nomeArquivo, ");
		//str.append("areaProfissionalInteresseContratacao.codigo AS \"areaProfissionalInteresseContratacao.codigo\", areaProfissionalInteresseContratacao.pessoa ");
		//str.append("AS \"areaProfissionalInteresseContratacao.pessoa\", areaProfissional.codigo AS \"areaProfissional.codigo\", ");
		//str.append("areaProfissional.descricaoAreaProfissional AS \"areaProfissional.descricaoAreaProfissional\", ");
		str.append("pais.codigo as \"pais.codigo\", pais.nome as \"pais.nome\", pais.cpf as \"pais.cpf\", pais.RG as \"pais.RG\", pais.dataNasc as \"pais.DataNasc\", pais.orgaoEmissor as \"pais.orgaoEmissor\", ");
		str.append("pais.endereco as \"pais.endereco\", pais.telefoneComer as \"pais.telefoneComer\", pais.telefoneRes as \"pais.telefoneRes\", ");
		str.append("pais.telefoneRecado as \"pais.telefoneRecado\", pais.celular as \"pais.celular\", pais.email as \"pais.email\", pais.nacionalidade as \"pais.nacionalidade\", pais.estadoCivil as \"pais.estadoCivil\", pais.setor as \"pais.setor\", ");
		str.append(" pais.cep AS \"pais.cep\",  pais.numero AS \"pais.numero\", pais.complemento AS \"pais.complemento\", pais.possuiAcessoVisaoPais AS \"pais.possuiAcessoVisaoPais\" , pais.grauParentesco AS \"pais.grauParentesco\" , pais.nomeBatismo AS \"pais.nomeBatismo\", ");
		str.append(" cidadePais.nome as \"cidadePais.nome\", cidadePais.codigo as \"cidadePais.codigo\", cidadePais.estado as \"cidadePais.estado\", estadoPais.nome as \"estadoPais.nome\", estadoPais.sigla \"estadoPais.sigla\", ");
		//str.append(" cidadefiador.nome as \"cidadefiador.nome\", cidadefiador.estado as \"cidadefiador.estado\", estadoFiador.nome as \"estadoFiador.nome\",estadoFiador.sigla as \"estadoFiador.sigla\" ,  cidadefiador.codigo as \"cidadefiador.codigo\", ");
		str.append(" pessoa.senhaCertificadoParaDocumento  as  \"pessoa.senhaCertificadoParaDocumento\", pessoa.dadospessoaisatualizado as \"pessoa.dadospessoaisatualizado\", pessoa.estadoCivilFiador as \"pessoa.estadoCivilFiador\", pessoa.rgFiador as \"pessoa.rgFiador\", pessoa.profissaoFiador as \"pessoa.profissaoFiador\", pessoa.paisFiador as \"pessoa.paisFiador\", pessoa.dataNascimentoFiador as \"pessoa.dataNascimentoFiador\", pais.professor AS \"pais.professor\", ");
		//str.append(" tipomidiacaptacao.nomemidia as \"tipomidiacaptacao.nomemidia\", tipomidiacaptacao.codigo as \"tipomidiacaptacao.codigo\", ");
		str.append(" pessoa.nomeBatismo, ");
//		str.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
//		str.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
//		str.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\", ");
		str.append(" pessoaEmailInstitucional.codigo as \"pessoaEmailInstitucional.codigo\", ");
		str.append(" pessoaEmailInstitucional.email as \"pessoaEmailInstitucional.email\", ");
		str.append(" pessoaEmailInstitucional.statusAtivoInativoEnum as \"pessoaEmailInstitucional.statusAtivoInativoEnum\", pessoa.renovacaoAutomatica as \"pessoa.renovacaoAutomatica\" ");
		str.append("FROM pessoa ");
//		str.append("LEFT JOIN perfilEconomico ON pessoa.perfilEconomico = perfilEconomico.codigo ");
		str.append("LEFT JOIN paiz ON pessoa.nacionalidade = paiz.codigo ");
		str.append("LEFT JOIN cidade ON pessoa.cidade = cidade.codigo ");
		str.append("LEFT JOIN estado on estado.codigo = cidade.estado ");
//		str.append("LEFT JOIN FormacaoExtraCurricular ON FormacaoExtraCurricular.pessoa = pessoa.codigo ");
		str.append("LEFT JOIN formacaoAcademica ON formacaoAcademica.pessoa = pessoa.codigo ");
		str.append("LEFT JOIN areaConhecimento ON formacaoAcademica.areaConhecimento = areaConhecimento.codigo ");
		str.append("LEFT JOIN filiacao ON filiacao.aluno = pessoa.codigo ");
		str.append("LEFT JOIN pessoa as pais ON filiacao.pais = pais.codigo ");
		str.append("LEFT JOIN cidade as cidadePais ON pais.cidade = cidadePais.codigo ");
		str.append("LEFT JOIN estado as estadoPais ON estadoPais.codigo = cidadePais.estado ");
//		str.append("LEFT JOIN cidade as cidadeFiador ON pessoa.cidadeFiador = cidadeFiador.codigo ");
//		str.append("LEFT JOIN estado as estadoFiador ON estadoFiador.codigo = cidadeFiador.estado ");
		 str.append("LEFT JOIN cidade AS filiacaoCidade ON filiacao.cidade = filiacaoCidade.codigo ");
		str.append("LEFT JOIN cidade cidadeFormacao ON formacaoAcademica.cidade = cidadeFormacao.codigo ");
		str.append("LEFT JOIN estado estadoFormacao ON estadoFormacao.codigo = cidadeFormacao.estado ");
//		str.append("LEFT JOIN dadosComerciais ON dadosComerciais.pessoa = pessoa.codigo ");
//		str.append("LEFT JOIN AreaProfissionalInteresseContratacao ON AreaProfissionalInteresseContratacao.pessoa = pessoa.codigo ");
//		str.append("LEFT JOIN AreaProfissional ON AreaProfissionalInteresseContratacao.AreaProfissional = AreaProfissional.codigo ");
//		str.append("LEFT JOIN cidade cidadeEmpresa on dadosComerciais.cidadeEmpresa = cidadeEmpresa.codigo ");
		str.append("LEFT JOIN cidade naturalidade ON pessoa.naturalidade = naturalidade.codigo ");
		str.append("LEFT JOIN estado estadoNaturalidade on estadoNaturalidade.codigo = naturalidade.estado ");
		str.append("LEFT JOIN arquivo ON pessoa.arquivoImagem = arquivo.codigo ");
//		str.append(" left join tipomidiacaptacao on tipomidiacaptacao.codigo = pessoa.tipomidiacaptacao ");
//		str.append(" left join pessoagsuite on pessoagsuite.pessoa = pessoa.codigo ");		
		str.append(" left join pessoaEmailInstitucional on pessoaEmailInstitucional.pessoa = pessoa.codigo ");		
		return str;
	}

	private void montarDadosCompleto(PessoaVO obj, SqlRowSet dadosSQL) throws Exception {
		montarDadosCompleto(obj, dadosSQL, false, true);
	}

	private void montarDadosCompleto(PessoaVO obj, SqlRowSet dadosSQL, Boolean funcionario, Boolean trazerDisciplinasInteresse) throws Exception {
		obj.getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.getNomeOriginal();
		obj.setCPF(dadosSQL.getString("CPF"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setCodProspect(dadosSQL.getInt("codProspect"));
		obj.setRG(dadosSQL.getString("RG"));
		obj.setCurriculoAtualizado(dadosSQL.getBoolean("curriculoAtualizado"));
		obj.setValorCssTopoLogo(dadosSQL.getString("valorCssTopoLogo"));
		obj.setValorCssBackground(dadosSQL.getString("valorCssBackground"));
		obj.setValorCssMenu(dadosSQL.getString("valorCssMenu"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setEstadoCivil(dadosSQL.getString("estadoCivil"));
		obj.setTelefoneComer(dadosSQL.getString("telefoneComer"));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setTelefoneRecado(dadosSQL.getString("telefoneRecado"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.getEmailOriginal();
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setDataNasc(dadosSQL.getDate("dataNasc"));
		obj.setCorRaca(dadosSQL.getString("corraca"));
		obj.setPaginaPessoal(dadosSQL.getString("paginaPessoal"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setCEP(dadosSQL.getString("CEP"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setCertificadoMilitar(dadosSQL.getString("certificadoMilitar"));
		if (dadosSQL.getString("situacaoMilitar") != null) {
			obj.setSituacaoMilitar(SituacaoMilitarEnum.valueOf(dadosSQL.getString("situacaoMilitar").toUpperCase()));
		} else {
			obj.setSituacaoMilitar(null);
		}
		obj.setDataEmissaoRG(dadosSQL.getDate("dataEmissaoRG"));
		obj.setEstadoEmissaoRG(dadosSQL.getString("estadoEmissaoRG"));
		obj.setOrgaoEmissor(dadosSQL.getString("orgaoEmissor"));
		obj.setTituloEleitoral(dadosSQL.getString("tituloEleitoral"));
		obj.setDataExpedicaoTituloEleitoral(dadosSQL.getDate("dataExpedicaoTituloEleitoral"));
		obj.setZonaEleitoral(dadosSQL.getString("zonaEleitoral"));
		obj.setSecaoZonaEleitoral(dadosSQL.getString("secaoZonaEleitoral"));
		obj.setDataExpedicaoCertificadoMilitar(dadosSQL.getDate("dataExpedicaoCertificadoMilitar"));
		obj.setOrgaoExpedidorCertificadoMilitar(dadosSQL.getString("orgaoExpedidorCertificadoMilitar"));
		obj.setNecessidadesEspeciais(dadosSQL.getString("necessidadesEspeciais"));
		obj.setTipoNecessidadesEspeciais(dadosSQL.getString("tipoNecessidadesEspeciais"));

		obj.setProfessor(dadosSQL.getBoolean("professor"));
		obj.setAluno(dadosSQL.getBoolean("aluno"));
		obj.setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiAcessoVisaoPais"));
		obj.setFuncionario(dadosSQL.getBoolean("funcionario"));
		obj.setCandidato(dadosSQL.getBoolean("candidato"));
		obj.setMembroComunidade(dadosSQL.getBoolean("membroComunidade"));
		obj.setAtuaComoDocente(dadosSQL.getString("atuaComoDocente"));
		obj.setAtivo(dadosSQL.getBoolean("ativo"));
		obj.setIdAlunoInep(dadosSQL.getString("idalunoinep"));
		obj.setPassaporte(dadosSQL.getString("passaporte"));
		obj.setDeficiencia(dadosSQL.getString("deficiencia"));
		obj.setNomeFiador(dadosSQL.getString("nomeFiador"));
		obj.setCpfFiador(dadosSQL.getString("cpfFiador"));
		obj.setEnderecoFiador(dadosSQL.getString("enderecoFiador"));
		obj.setTelefoneFiador(dadosSQL.getString("telefoneFiador"));
		obj.setCelularFiador(dadosSQL.getString("celularFiador"));
		obj.setCepFiador(dadosSQL.getString("cepFiador"));
		obj.setSetorFiador(dadosSQL.getString("setorFiador"));
		obj.setNumeroEndFiador(dadosSQL.getString("numeroEndFiador"));
		obj.setComplementoFiador(dadosSQL.getString("complementoFiador"));
//		obj.getCidadeFiador().setNome(dadosSQL.getString("cidadefiador.nome"));
//		obj.getCidadeFiador().setCodigo(dadosSQL.getInt("cidadefiador.codigo"));
//		obj.getCidadeFiador().getEstado().setCodigo(dadosSQL.getInt("cidadefiador.estado"));
//		obj.getCidadeFiador().getEstado().setNome(dadosSQL.getString("estadoFiador.nome"));
//		obj.getCidadeFiador().getEstado().setSigla(dadosSQL.getString("estadoFiador.sigla"));
		obj.setCoordenador(dadosSQL.getBoolean("coordenador"));
		obj.setIsentarTaxaBoleto(dadosSQL.getBoolean("isentarTaxaBoleto"));
		obj.setCertidaoNascimento(dadosSQL.getString("certidaoNascimento"));
		obj.setOcultarDadosCRM(dadosSQL.getBoolean("ocultarDadosCRM"));
		obj.setIngles(dadosSQL.getBoolean("ingles"));
		obj.setEspanhol(dadosSQL.getBoolean("espanhol"));
		obj.setFrances(dadosSQL.getBoolean("frances"));
		obj.setInglesNivel(dadosSQL.getString("inglesNivel"));
		obj.setEspanholNivel(dadosSQL.getString("espanholNivel"));
		obj.setFrancesNivel(dadosSQL.getString("francesNivel"));
		obj.setOutrosIdiomas(dadosSQL.getString("outrosIdiomas"));
		obj.setOutrosIdiomasNivel(dadosSQL.getString("outrosIdiomasNivel"));
		obj.setWindows(dadosSQL.getBoolean("windows"));
		obj.setWord(dadosSQL.getBoolean("word"));
		obj.setExcel(dadosSQL.getBoolean("excel"));
		obj.setAccess(dadosSQL.getBoolean("access"));
		obj.setPowerPoint(dadosSQL.getBoolean("powerPoint"));
		obj.setInternet(dadosSQL.getBoolean("internet"));
		obj.setSap(dadosSQL.getBoolean("sap"));
		obj.setCorelDraw(dadosSQL.getBoolean("corelDraw"));
		obj.setAutoCad(dadosSQL.getBoolean("autoCad"));
		obj.setPhotoshop(dadosSQL.getBoolean("photoshop"));
		obj.setMicrosiga(dadosSQL.getBoolean("microsiga"));
		obj.setOutrosSoftwares(dadosSQL.getString("outrosSoftwares"));
		obj.setQtdFilhos(dadosSQL.getInt("qtdFilhos"));
		obj.setParticipaBancoCurriculum(dadosSQL.getBoolean("participaBancoCurriculum"));
		obj.setInformacoesVerdadeiras(dadosSQL.getBoolean("informacoesVerdadeiras"));
		obj.setSabatista(dadosSQL.getBoolean("sabatista"));
		obj.setInformacoesAdicionais(dadosSQL.getString("informacoesAdicionais"));
		obj.setDivulgarMeusDados(dadosSQL.getBoolean("divulgarMeusDados"));
		obj.setPispasep(dadosSQL.getString("pispasep"));
		obj.setGravida(dadosSQL.getBoolean("pessoa.gravida"));
		obj.setCanhoto(dadosSQL.getBoolean("pessoa.canhoto"));
		obj.setPortadorNecessidadeEspecial(dadosSQL.getBoolean("pessoa.portadorNecessidadeEspecial"));
		obj.setDadosPessoaisAtualizado(dadosSQL.getBoolean("pessoa.dadospessoaisatualizado"));
		obj.setNomeCenso(dadosSQL.getString("nomeCenso"));
		obj.setGrauParentesco(dadosSQL.getString("grauParentesco"));
		obj.setSenhaCertificadoParaDocumento(dadosSQL.getString("pessoa.senhaCertificadoParaDocumento"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAssinaturaDocumentoEnum"))){
			obj.setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("tipoAssinaturaDocumentoEnum")));
		}
		obj.setPermiteEnviarRemessa(dadosSQL.getBoolean("pessoa.permiteEnviarRemessa"));
		obj.setRegistroAcademico(dadosSQL.getString("registroAcademico"));
		obj.setTempoEstendidoProva(dadosSQL.getBoolean("tempoestendidoprova"));

		obj.setNovoObj(false);
		// DADOS CIDADE
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
		obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getCidade().setCep(dadosSQL.getString("cidade.cep"));
		obj.getCidade().setCodigoInep(dadosSQL.getInt("cidade.codigoInep"));
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("cidade.estado"));
		obj.getCidade().setCodigoIBGE(dadosSQL.getString("cidade.codigoibge"));
		// DADOS ESTADO
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
		obj.getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
		obj.getCidade().getEstado().setCodigoInep(dadosSQL.getInt("estado.codigoInep"));
		obj.getCidade().getEstado().getPaiz().setCodigo(dadosSQL.getInt("estado.paiz"));
		// DADOS NACIONALIDADE
		obj.getNacionalidade().setCodigo(dadosSQL.getInt("nacionalidade.codigo"));
		obj.getNacionalidade().setNacionalidade(dadosSQL.getString("nacionalidade.nome"));
		// DADOS NATURALIDADE
		obj.getNaturalidade().setCodigo(dadosSQL.getInt("naturalidade.codigo"));
		obj.getNaturalidade().setNome(dadosSQL.getString("naturalidade.nome"));
		obj.getNaturalidade().getEstado().setCodigo(dadosSQL.getInt("estadoNaturalidade.codigo"));
		obj.getNaturalidade().getEstado().setNome(dadosSQL.getString("estadoNaturalidade.nome"));
		obj.getNaturalidade().getEstado().setSigla(dadosSQL.getString("estadoNaturalidade.sigla"));
		// DADOS PERFIL ECONï¿½MICO
//		obj.getPerfilEconomico().setCodigo(dadosSQL.getInt("perfilEconomico.codigo"));
//		obj.getPerfilEconomico().setNome(dadosSQL.getString("perfilEconomico.nome"));

		obj.setEstadoCivilFiador(dadosSQL.getString("pessoa.estadoCivilFiador"));
		obj.setProfissaoFiador(dadosSQL.getString("pessoa.profissaoFiador"));
		obj.setRgFiador(dadosSQL.getString("pessoa.rgFiador"));
		obj.getPaisFiador().setCodigo(dadosSQL.getInt("pessoa.paisFiador"));
		obj.setContaCorrente(dadosSQL.getString("contacorrente"));
		obj.setAgencia(dadosSQL.getString("agencia"));
		obj.setBanco(dadosSQL.getString("banco"));
		obj.setUniversidadeParceira(dadosSQL.getString("universidadeparceira"));
		obj.setModalidadeBolsa(ModalidadeBolsaEnum.getEnumPorValor((dadosSQL.getString("modalidadebolsa"))));			
		obj.setValorBolsa(dadosSQL.getDouble("valorbolsa"));
		obj.setTranstornosNeurodivergentes(dadosSQL.getString("transtornosNeurodivergentes"));
//		if (Uteis.isAtributoPreenchido(obj.getPaisFiador())) {
//			obj.setPaisFiador(getFacadeFactory().getPaizFacade().consultarPorChavePrimaria(obj.getPaisFiador().getCodigo(), false, null));
//		}
		obj.setDataNascimentoFiador(dadosSQL.getDate("pessoa.dataNascimentoFiador"));
		obj.setRenovacaoAutomatica(dadosSQL.getBoolean("pessoa.renovacaoAutomatica"));
		FormacaoAcademicaVO formacaoAcademicaVO = null;
		obj.setFormacaoAcademicaVOs(new ArrayList<FormacaoAcademicaVO>(0));
		FiliacaoVO filiacaoVO = null;
		obj.setFiliacaoVOs(new ArrayList<FiliacaoVO>(0));
		// DADOS DADOSCOMERCIAIS
//		DadosComerciaisVO dadosComerciaisVO = null;
		// DADOS DADOSCOMERCIAIS
//		AreaProfissionalInteresseContratacaoVO areaProfissionalInteresseContratacaoVO = null;
		// FormacaoExtraCurricular
//		FormacaoExtraCurricularVO formacaoExtraCurricularVO = null;
		// DisciplinasInteresse
//		DisciplinasInteresseVO disciplinasInteresseVO = null;
		PessoaVO pais = null;
		CidadeVO cidadePais = null;
//		TipoMidiaCaptacaoVO tipoMidiaCaptacaoVO = null;
//		PessoaGsuiteVO pessoaGsuiteVO = null;
		PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = null;
		// HASH PARA TRATAR LISTAS DE UM MESMO OBJETO
		HashMap<Integer, FormacaoAcademicaVO> hashtableFormacaoAcademica = new HashMap<Integer, FormacaoAcademicaVO>(0);
		HashMap<Integer, FiliacaoVO> hashtableFiliacao = new HashMap<Integer, FiliacaoVO>(0);
//		HashMap<Integer, DadosComerciaisVO> hashtableDadosComerciais = new HashMap<Integer, DadosComerciaisVO>(0);
//		HashMap<Integer, FormacaoExtraCurricularVO> hashtableFormacaoExtraCurricular = new HashMap<Integer, FormacaoExtraCurricularVO>(0);
//		HashMap<Integer, AreaProfissionalInteresseContratacaoVO> hashtableAreaProfissionalInteresseContratacao = new HashMap<Integer, AreaProfissionalInteresseContratacaoVO>(0);
//		HashMap<Integer, DisciplinasInteresseVO> hashtableDisciplinasInteresse = new HashMap<Integer, DisciplinasInteresseVO>(0);
//		HashMap<Integer, PessoaGsuiteVO> hashtablePessoaGsuite = new HashMap<Integer, PessoaGsuiteVO>(0);
		HashMap<Integer, PessoaEmailInstitucionalVO> hashtablePessoaEmailInstitucional = new HashMap<Integer, PessoaEmailInstitucionalVO>(0);

		do {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
			formacaoAcademicaVO.setCodigo(dadosSQL.getInt("formacaoAcademica.codigo"));
			formacaoAcademicaVO.setInstituicao(dadosSQL.getString("formacaoAcademica.instituicao"));
			formacaoAcademicaVO.setEscolaridade(dadosSQL.getString("formacaoAcademica.escolaridade"));
			formacaoAcademicaVO.setModalidade(dadosSQL.getString("formacaoAcademica.modalidade"));
			formacaoAcademicaVO.setCurso(dadosSQL.getString("formacaoAcademica.curso"));
			formacaoAcademicaVO.setSituacao(dadosSQL.getString("formacaoAcademica.situacao"));
			formacaoAcademicaVO.setTipoInst(dadosSQL.getString("formacaoAcademica.tipoInst"));
			formacaoAcademicaVO.setDataInicio(dadosSQL.getDate("formacaoAcademica.dataInicio"));
			formacaoAcademicaVO.setDataFim(dadosSQL.getDate("formacaoAcademica.dataFim"));
			formacaoAcademicaVO.setAnoDataFim(dadosSQL.getString("formacaoAcademica.anoDataFim"));
			formacaoAcademicaVO.setSemestreDataFim(dadosSQL.getString("formacaoAcademica.semestreDataFim"));
			formacaoAcademicaVO.setPessoa(dadosSQL.getInt("formacaoAcademica.pessoa"));
			formacaoAcademicaVO.getAreaConhecimento().setCodigo(dadosSQL.getInt("formacaoAcademica.areaConhecimento.codigo"));
			formacaoAcademicaVO.getAreaConhecimento().setNome(dadosSQL.getString("formacaoAcademica.areaConhecimento.nome"));
			formacaoAcademicaVO.getCidade().setCodigo(dadosSQL.getInt("formacaoAcademica.cidade.codigo"));
			formacaoAcademicaVO.getCidade().setNome(dadosSQL.getString("formacaoAcademica.cidade.nome"));
			formacaoAcademicaVO.getCidade().getEstado().setCodigo(dadosSQL.getInt("formacaoAcademica.estado.codigo"));
			formacaoAcademicaVO.getCidade().getEstado().setNome(dadosSQL.getString("formacaoAcademica.estado.nome"));
			formacaoAcademicaVO.getCidade().getEstado().setSigla(dadosSQL.getString("formacaoAcademica.estado.sigla"));
			formacaoAcademicaVO.getCidade().getEstado().getPaiz().setCodigo(dadosSQL.getInt("formacaoAcademica.estado.paiz"));
			formacaoAcademicaVO.setTitulo(dadosSQL.getString("formacaoAcademica.titulo"));
			formacaoAcademicaVO.setFuncionario(funcionario);
			if (!hashtableFormacaoAcademica.containsKey(formacaoAcademicaVO.getCodigo()) && formacaoAcademicaVO.getCodigo() != 0) {
				obj.getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
			}
			hashtableFormacaoAcademica.put(formacaoAcademicaVO.getCodigo(), formacaoAcademicaVO);
			filiacaoVO = new FiliacaoVO();
			filiacaoVO.setCodigo(dadosSQL.getInt("filiacao.codigo"));
			filiacaoVO.setTipo(dadosSQL.getString("filiacao.tipo"));
			filiacaoVO.setAluno(dadosSQL.getInt("filiacao.aluno"));
			filiacaoVO.setResponsavelFinanceiro(dadosSQL.getBoolean("filiacao.responsavelFinanceiro"));
			filiacaoVO.setResponsavelLegal(dadosSQL.getBoolean("filiacao.responsavelLegal"));

			pais = new PessoaVO();
			pais.setCodigo(dadosSQL.getInt("pais.codigo"));
			pais.setNome(dadosSQL.getString("pais.nome"));
			pais.setNomeBatismo(dadosSQL.getString("pais.nomeBatismo"));
			pais.setCPF(dadosSQL.getString("pais.CPF"));
			pais.setRG(dadosSQL.getString("pais.RG"));
			pais.setDataNasc(dadosSQL.getDate("pais.dataNasc"));
			pais.setOrgaoEmissor(dadosSQL.getString("pais.orgaoEmissor"));
			pais.setEndereco(dadosSQL.getString("pais.endereco"));
			pais.setCEP(dadosSQL.getString("pais.cep"));
			pais.setNumero(dadosSQL.getString("pais.numero"));
			pais.setComplemento(dadosSQL.getString("pais.complemento"));
			pais.setSetor(dadosSQL.getString("pais.setor"));
			pais.setTelefoneComer(dadosSQL.getString("pais.telefoneComer"));
			pais.setTelefoneRes(dadosSQL.getString("pais.telefoneRes"));
			pais.setTelefoneRecado(dadosSQL.getString("pais.telefoneRecado"));
			pais.setCelular(dadosSQL.getString("pais.celular"));
			pais.setEmail(dadosSQL.getString("pais.email"));
			pais.getNacionalidade().setCodigo(dadosSQL.getInt("pais.nacionalidade"));
			pais.setEstadoCivil(dadosSQL.getString("pais.estadoCivil"));
			pais.setPossuiAcessoVisaoPais(dadosSQL.getBoolean("pais.possuiAcessoVisaoPais"));
			pais.setGrauParentesco(dadosSQL.getString("pais.grauParentesco"));
			cidadePais = new CidadeVO();
			cidadePais.setNome(dadosSQL.getString("cidadePais.nome"));
			cidadePais.setCodigo(dadosSQL.getInt("cidadePais.codigo"));
			cidadePais.getEstado().setCodigo(dadosSQL.getInt("cidadePais.estado"));
			cidadePais.getEstado().setNome(dadosSQL.getString("estadoPais.nome"));
			cidadePais.getEstado().setSigla(dadosSQL.getString("estadoPais.sigla"));
			pais.setCidade(cidadePais);
			pais.setProfessor(dadosSQL.getBoolean("pais.professor"));
			filiacaoVO.setPais(pais);

			if (!hashtableFiliacao.containsKey(filiacaoVO.getCodigo()) && filiacaoVO.getCodigo() != 0) {
				obj.getFiliacaoVOs().add(filiacaoVO);
			}
			hashtableFiliacao.put(filiacaoVO.getCodigo(), filiacaoVO);

//			dadosComerciaisVO = new DadosComerciaisVO();
//			dadosComerciaisVO.setCodigo(dadosSQL.getInt("dadosComerciais.codigo"));
//			dadosComerciaisVO.setNomeEmpresa(dadosSQL.getString("dadosComerciais.nomeEmpresa"));
//			dadosComerciaisVO.setEnderecoEmpresa(dadosSQL.getString("dadosComerciais.enderecoEmpresa"));
//			dadosComerciaisVO.setCargoPessoaEmpresa(dadosSQL.getString("dadosComerciais.cargoPessoaEmpresa"));
//			dadosComerciaisVO.setCepEmpresa(dadosSQL.getString("dadosComerciais.cepEmpresa"));
//			dadosComerciaisVO.setComplementoEmpresa(dadosSQL.getString("dadosComerciais.complementoEmpresa"));
//			dadosComerciaisVO.setSetorEmpresa(dadosSQL.getString("dadosComerciais.setorEmpresa"));
//			dadosComerciaisVO.setTelefoneComer(dadosSQL.getString("dadosComerciais.telefoneComer"));
//			dadosComerciaisVO.setEmpregoAtual(dadosSQL.getBoolean("dadosComerciais.empregoAtual"));
//			dadosComerciaisVO.setPrincipaisAtividades(dadosSQL.getString("dadosComerciais.principaisAtividades"));
//			dadosComerciaisVO.setSalario(dadosSQL.getString("dadosComerciais.salario"));
//			dadosComerciaisVO.setMotivoDesligamento(dadosSQL.getString("dadosComerciais.motivoDesligamento"));
//			dadosComerciaisVO.setDataAdmissao(dadosSQL.getDate("dadosComerciais.dataAdmissao"));
//			dadosComerciaisVO.setDataDemissao(dadosSQL.getDate("dadosComerciais.dataDemissao"));
//			dadosComerciaisVO.getCidadeEmpresa().setCodigo(dadosSQL.getInt("dadosComerciais.codigoCidadeEmpresa"));
//			dadosComerciaisVO.getCidadeEmpresa().setNome(dadosSQL.getString("dadosComerciais.nomeCidadeEmpresa"));
//			dadosComerciaisVO.getPessoa().setCodigo(dadosSQL.getInt("dadosComerciais.pessoa"));
//			dadosComerciaisVO.getPessoa().setNome(dadosSQL.getString("nome"));
//
//			if (!hashtableDadosComerciais.containsKey(dadosComerciaisVO.getCodigo()) && dadosComerciaisVO.getCodigo() != 0) {
//				obj.getDadosComerciaisVOs().add(dadosComerciaisVO);
//			}
//			hashtableDadosComerciais.put(dadosComerciaisVO.getCodigo(), dadosComerciaisVO);
//
//			areaProfissionalInteresseContratacaoVO = new AreaProfissionalInteresseContratacaoVO();
//			areaProfissionalInteresseContratacaoVO.setCodigo(dadosSQL.getInt("areaProfissionalInteresseContratacao.codigo"));
//			areaProfissionalInteresseContratacaoVO.getPessoa().setCodigo(dadosSQL.getInt("areaProfissionalInteresseContratacao.pessoa"));
//			areaProfissionalInteresseContratacaoVO.getPessoa().setNome(dadosSQL.getString("nome"));
//			areaProfissionalInteresseContratacaoVO.getAreaProfissional().setCodigo(dadosSQL.getInt("areaProfissional.codigo"));
//			areaProfissionalInteresseContratacaoVO.getAreaProfissional().setDescricaoAreaProfissional(dadosSQL.getString("areaProfissional.descricaoAreaProfissional"));
//
//			if (!hashtableAreaProfissionalInteresseContratacao.containsKey(areaProfissionalInteresseContratacaoVO.getCodigo()) && areaProfissionalInteresseContratacaoVO.getCodigo() != 0) {
//				obj.getAreaProfissionalInteresseContratacaoVOs().add(areaProfissionalInteresseContratacaoVO);
//			}
//			hashtableAreaProfissionalInteresseContratacao.put(areaProfissionalInteresseContratacaoVO.getCodigo(), areaProfissionalInteresseContratacaoVO);
//
//			formacaoExtraCurricularVO = new FormacaoExtraCurricularVO();
//			formacaoExtraCurricularVO.setCodigo(dadosSQL.getInt("formacaoExtraCurricular.codigo"));
//			formacaoExtraCurricularVO.setInstituicao(dadosSQL.getString("formacaoExtraCurricular.instituicao"));
//			formacaoExtraCurricularVO.setCurso(dadosSQL.getString("formacaoExtraCurricular.curso"));
//			formacaoExtraCurricularVO.setAnoRealizacaoConclusao(dadosSQL.getInt("formacaoExtraCurricular.anoRealizacaoConclusao"));
//			formacaoExtraCurricularVO.setCargaHoraria(dadosSQL.getString("formacaoExtraCurricular.cargaHoraria"));
//			formacaoExtraCurricularVO.getPessoa().setCodigo(dadosSQL.getInt("formacaoExtraCurricular.pessoa"));
//			formacaoExtraCurricularVO.getPessoa().setNome(dadosSQL.getString("nome"));
//
//			if (!hashtableFormacaoExtraCurricular.containsKey(formacaoExtraCurricularVO.getCodigo()) && formacaoExtraCurricularVO.getCodigo() != 0) {
//				obj.getFormacaoExtraCurricularVOs().add(formacaoExtraCurricularVO);
//			}
//			hashtableFormacaoExtraCurricular.put(formacaoExtraCurricularVO.getCodigo(), formacaoExtraCurricularVO);
//
//			if (trazerDisciplinasInteresse) {}
//
//			tipoMidiaCaptacaoVO = new TipoMidiaCaptacaoVO();
//			tipoMidiaCaptacaoVO.setNomeMidia(dadosSQL.getString("tipomidiacaptacao.nomemidia"));
//			tipoMidiaCaptacaoVO.setCodigo(dadosSQL.getInt("tipomidiacaptacao.codigo"));
//
//			if (!hashtableFormacaoExtraCurricular.containsKey(tipoMidiaCaptacaoVO.getCodigo()) && tipoMidiaCaptacaoVO.getCodigo() != null && tipoMidiaCaptacaoVO.getCodigo() != 0) {
//				obj.setTipoMidiaCaptacao(tipoMidiaCaptacaoVO);
//			}
			
//			pessoaGsuiteVO = new PessoaGsuiteVO();
//			if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoaGsuite.statusAtivoInativoEnum"))) {
//				pessoaGsuiteVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoaGsuite.statusAtivoInativoEnum")));	
//			}
//			pessoaGsuiteVO.setCodigo(dadosSQL.getInt("pessoaGsuite.codigo"));
//			pessoaGsuiteVO.setEmail(dadosSQL.getString("pessoaGsuite.email"));
//			pessoaGsuiteVO.getPessoaVO().setCodigo(obj.getCodigo());
//			
//			if (!hashtablePessoaGsuite.containsKey(pessoaGsuiteVO.getCodigo()) && pessoaGsuiteVO != null && pessoaGsuiteVO.getCodigo() != 0) {
//				obj.getListaPessoaGsuite().add(pessoaGsuiteVO);
//				hashtablePessoaGsuite.put(pessoaGsuiteVO.getCodigo(), pessoaGsuiteVO);
//			}
			
			pessoaEmailInstitucionalVO = new PessoaEmailInstitucionalVO();
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum"))) {
				pessoaEmailInstitucionalVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum")));	
			}
			pessoaEmailInstitucionalVO.setCodigo(dadosSQL.getInt("pessoaEmailInstitucional.codigo"));
			pessoaEmailInstitucionalVO.setEmail(dadosSQL.getString("pessoaEmailInstitucional.email"));
			pessoaEmailInstitucionalVO.getPessoaVO().setCodigo(obj.getCodigo());
			
			if (!hashtablePessoaEmailInstitucional.containsKey(pessoaEmailInstitucionalVO.getCodigo()) && pessoaEmailInstitucionalVO != null && pessoaEmailInstitucionalVO.getCodigo() != 0) {
				obj.getListaPessoaEmailInstitucionalVO().add(pessoaEmailInstitucionalVO);
				hashtablePessoaEmailInstitucional.put(pessoaEmailInstitucionalVO.getCodigo(), pessoaEmailInstitucionalVO);
			}

		} while (dadosSQL.next());

	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Carlos
	 */
	public void consultaRapidaAlunoMatriculado(Integer unidadeEnsino, String tipoPessoa, String situacao, List<PessoaVO> listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(true, false);
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" WHERE pessoa.aluno = 'true' ");
			if (situacao.equals("AT")) {
				sqlStr.append(" AND matriculaPeriodo.situacao = '").append(situacao).append("' ");
			}
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" WHERE pessoa.professor = 'true'");
		}
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" AND matricula.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}

		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		listaAluno = montarDadosConsultaRapida(tabelaResultado);
		adicionarPessoaListaDestinatario(listaAluno, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	/**
	 * Mï¿½todo Responsável por retornar a consulta bï¿½sica do aluno apontando para
	 * a situaï¿½ï¿½o da matricula do mesmo.
	 *
	 * @param unidadeEnsino
	 * @param situacao
	 * @param listaAluno
	 * @param comunicacaoInterna
	 * @param comunicadoInternoDestinatario
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void consultaRapidaAlunoMatriculadoSituacaoMatricula(Integer unidadeEnsino, String situacao, List<PessoaVO> listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer(getSQLPadraoConsultaBasica(true, false));
		sqlStr.append(" WHERE matricula.situacao = '").append(situacao).append("' ");
		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo = '").append(situacao).append("' ");
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" AND matricula.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}	
		if(!comunicacaoInterna.getComunicadoInternoDestinatarioVOs().isEmpty()) {
			sqlStr.append(" and pessoa.codigo not in (0 ");
			for(ComunicadoInternoDestinatarioVO c: comunicacaoInterna.getComunicadoInternoDestinatarioVOs()) {
				sqlStr.append(",").append(c.getDestinatario().getCodigo());
			}
			sqlStr.append(" ) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		listaAluno = montarDadosConsultaRapida(tabelaResultado);
		adicionarPessoaListaDestinatario(listaAluno, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Carlos
	 */
	public void consultaRapidaTodosProfessoresNivelEducacional(Integer unidadeEnsino, String nivelEducacional, String tipoPessoa, List<PessoaVO> listaProfessor, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" WHERE pessoa.professor = 'true'");
		sqlStr.append(" AND exists(select horarioturmadiaitem.codigo from horarioturmadiaitem ");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso ");
		sqlStr.append(" where horarioturmadiaitem.professor = pessoa.codigo ");
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino.intValue());
		}
		if (!nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" limit 1) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		listaProfessor = montarDadosConsultaRapida(tabelaResultado);
		adicionarPessoaListaDestinatario(listaProfessor, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	@Override
	public void consultaRapidaTodosCoordenadores(Integer unidadeEnsino, String tipoPessoa, List<PessoaVO> listaCoordenador, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" inner join cursoCoordenador on cursoCoordenador.funcionario = funcionario.codigo ");
		sqlStr.append(" WHERE pessoa.coordenador = 'true'");
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND cursoCoordenador.unidadeensino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		listaCoordenador = montarDadosConsultaRapida(tabelaResultado);
		adicionarPessoaListaDestinatario(listaCoordenador, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	@Override
	public void consultaRapidaTodosFuncionarios(Integer unidadeEnsino, String tipoPessoa, List<PessoaVO> listaFuncionario, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLPadraoConsultaBasica(false, false));
		sqlStr.append(" INNER JOIN funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" LEFT JOIN funcionariocargo on funcionariocargo.funcionario = funcionario.codigo");
		sqlStr.append(" WHERE pessoa.funcionario = 'true'");
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND funcionariocargo.unidadeensino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		listaFuncionario = montarDadosConsultaRapida(tabelaResultado);
		adicionarPessoaListaDestinatario(listaFuncionario, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	public List<PessoaVO> consultaRapidaPessoaGrupoDestinatario(Integer codGrupo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" inner join funcionariogrupodestinatarios on funcionario.codigo = funcionariogrupodestinatarios.funcionario");
		sqlStr.append(" where pessoa.ativo and grupodestinatarios = ").append(codGrupo.intValue());
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Mï¿½todo responsavel por invocar uma consulta rï¿½pida(Bï¿½sica) e padrï¿½o que
	 * buscarï¿½ apenas campos necessï¿½rios para visualizaï¿½ï¿½o do cliente na tela.
	 * Estï¿½ consulta ï¿½ considerada Padrï¿½o pelo motivo de todos os mï¿½todos chamar
	 * a mesma consulta(getSQLPadraoConsultaBasica) e apenas adicionar as
	 * clï¿½usulas de condiï¿½ï¿½es e ordenaï¿½ï¿½o.
	 *
	 * @author Carlos
	 */
	public void consultaRapidaTodaComunidade(Integer unidadeEnsino, List<PessoaVO> listaPessoa, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(getSQLPadraoConsultaBasica(true, false));
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" WHERE matricula.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" union ");
		sqlStr.append(getSQLPadraoConsultaBasica(false, false));
		//sqlStr.append(" FROM pessoa ");
		sqlStr.append(" INNER JOIN funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" INNER JOIN funcionarioCargo on funcionarioCargo.funcionario = funcionario.codigo ");
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" where funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		listaPessoa = montarDadosConsultaRapida(tabelaResultado);
		adicionarPessoaListaDestinatario(listaPessoa, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	/**
	 * Mï¿½todo responsavel por adicionar os Alunos Matriculados na lista de
	 * Destinatarios de Comunicaï¿½ï¿½o Interna quando for escolhida a opï¿½ï¿½o Todos
	 * Alunos.
	 *
	 * @param listaAluno
	 * @param comunicacaoInterna
	 * @param comunicadoInternoDestinatario
	 * @throws Exception
	 * @author Carlos
	 */
	public void adicionarPessoaListaDestinatario(List<PessoaVO> listaPessoa, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario) throws Exception {
		List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>(0); 
		comunicadoInternoDestinatarioVOs.addAll(comunicacaoInterna.getComunicadoInternoDestinatarioVOs());
		Integer qtdDestinatarios = 0;
		for (PessoaVO pessoa : listaPessoa) {			
			if(comunicadoInternoDestinatarioVOs.isEmpty() || comunicadoInternoDestinatarioVOs.stream().noneMatch(comunicadoItem -> comunicadoItem.getDestinatario().equals(pessoa))){
				int i = 1;
				comunicadoInternoDestinatario.setDestinatario(pessoa);
				comunicadoInternoDestinatario.setTipoComunicadoInterno(comunicacaoInterna.getTipoComunicadoInterno());
				comunicacaoInterna.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatario);
				comunicadoInternoDestinatario = new ComunicadoInternoDestinatarioVO();
				qtdDestinatarios += i;
			}
		}
		comunicacaoInterna.setTotalizadorDestinatario(qtdDestinatarios);
		// Iterator i = listaPessoa.iterator();
		// while (i.hasNext()) {
		// PessoaVO obj = (PessoaVO) i.next();
		// comunicadoInternoDestinatario.setDestinatario(obj);
		// comunicadoInternoDestinatario.setTipoComunicadoInterno(comunicacaoInterna.getTipoComunicadoInterno());
		// comunicacaoInterna.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatario);
		// comunicadoInternoDestinatario = new
		// ComunicadoInternoDestinatarioVO();
		// }
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarPessoaGsuite(PessoaVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario) {
		try {
			int index = 0;
			Uteis.checkState(!Uteis.isAtributoPreenchido(pessoaGsuite.getEmail()), "O campo E-mail (Google Suite ) deve ser informado. ");
			Uteis.checkState(!Uteis.getValidaEmail(pessoaGsuite.getEmail()), "E-mail Informado não é valido ");
			pessoaGsuite.setPessoaVO(obj);
			pessoaGsuite.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
			for (PessoaGsuiteVO objExistente : obj.getListaPessoaGsuite()) {
				if (objExistente.equalsCampoSelecaoLista(pessoaGsuite)) {
					obj.getListaPessoaGsuite().set(index, pessoaGsuite);
					return;
				}
				index++;
			}
			pessoaGsuite.setCodigo(0);
			obj.getListaPessoaGsuite().add(pessoaGsuite);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)	
	public void removerPessoaGsuite(PessoaVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario) {
		Iterator<PessoaGsuiteVO> i = obj.getListaPessoaGsuite().iterator();
		while (i.hasNext()) {
			PessoaGsuiteVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(pessoaGsuite)) {
				i.remove();
				return;
			}
		}
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarPessoaEmailInstitucionalVO(PessoaVO obj, PessoaEmailInstitucionalVO pessoaEmailInstitucional, UsuarioVO usuario) {
		try {
			int index = 0;
			Uteis.checkState(!Uteis.isAtributoPreenchido(pessoaEmailInstitucional.getEmail()), "O campo E-mail Institucional (Dados Pessoais) deve ser informado. ");
			Uteis.checkState(!Uteis.getValidaEmail(pessoaEmailInstitucional.getEmail()), "O campo E-mail Institucional (Dados Pessoais) informado não é válido. ");
			pessoaEmailInstitucional.setPessoaVO(obj);
			pessoaEmailInstitucional.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
			for (PessoaEmailInstitucionalVO objExistente : obj.getListaPessoaEmailInstitucionalVO()) {
				if (objExistente.equalsCampoSelecaoLista(pessoaEmailInstitucional)) {
					obj.getListaPessoaEmailInstitucionalVO().set(index, pessoaEmailInstitucional);
					return;
				}
				index++;
			}
			pessoaEmailInstitucional.setCodigo(0);
			obj.getListaPessoaEmailInstitucionalVO().add(pessoaEmailInstitucional);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)	
	public void removerPessoaEmailInstitucionalVO(PessoaVO obj, PessoaEmailInstitucionalVO pessoaEmailInstitucional, UsuarioVO usuario) {
		Iterator<PessoaEmailInstitucionalVO> i = obj.getListaPessoaEmailInstitucionalVO().iterator();
		while (i.hasNext()) {
			PessoaEmailInstitucionalVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(pessoaEmailInstitucional)) {
				i.remove();
				return;
			}
		}
	}

	/**
	 * Operaï¿½ï¿½o reponsï¿½vel por retornar o identificador desta classe. Este
	 * identificar ï¿½ utilizado para verificar as permissï¿½es de acesso as
	 * operaï¿½ï¿½es desta classe.
	 */
	public static String getIdEntidade() {
		return Pessoa.idEntidade;
	}

	/**
	 * Operaï¿½ï¿½o reponsï¿½vel por definir um novo valor para o identificador desta
	 * classe. Esta alteraï¿½ï¿½o deve ser possï¿½vel, pois, uma mesma classe de
	 * negï¿½cio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso ï¿½ realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Pessoa.idEntidade = idEntidade;
	}

	public PessoaVO consultarAlunoPeloCodigoOrigemDaConta(String codigoOrigem, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT p.* FROM pessoa p ");
		sqlStr.append("INNER JOIN contareceber cr ON cr.pessoa = p.codigo ");
		sqlStr.append("WHERE codorigem = '").append(codigoOrigem).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		PessoaVO pessoaVO = new PessoaVO();
		if (!tabelaResultado.next()) {
			return pessoaVO;
		}
		return montarDados(tabelaResultado, pessoaVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	}

	public Boolean verificarSeUsuarioIsSomenteAluno(Integer codigoPessoa) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT CASE ALUNO WHEN TRUE AND PROFESSOR = FALSE AND FUNCIONARIO = FALSE THEN TRUE ELSE FALSE END AS aluno FROM PESSOA ");
		sqlStr.append("WHERE CODIGO = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPessoa });
		tabelaResultado.next();
		return tabelaResultado.getBoolean("aluno");
	}

	public Boolean verificarSeUsuarioIsSomenteProfessor(Integer codigoPessoa) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append("CASE WHEN PROFESSOR = TRUE AND ALUNO = FALSE AND FUNCIONARIO = TRUE AND U.TIPOUSUARIO = 'PR' THEN 'professor' END AS professor ");
		sqlStr.append("FROM PESSOA P ");
		sqlStr.append("INNER JOIN USUARIO U ON U.PESSOA = P.CODIGO ");
		sqlStr.append("WHERE P.CODIGO = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPessoa });
		tabelaResultado.next();
		return tabelaResultado.getBoolean("professor");
	}

	public Boolean verificarSeUsuarioIsAluno(Integer codigoPessoa) {
		if (codigoPessoa == 0) {
			return false;
		}
		try {
			StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append("SELECT aluno FROM pessoa WHERE codigo = ?");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPessoa });
			if (tabelaResultado.next()) {
				return tabelaResultado.getBoolean("aluno");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Esse mï¿½todo pega um cï¿½digo de um exemplar qualquer e consulta na base
	 * qual pessoa estï¿½ com ele emprestado
	 *
	 * @param pessoaVO
	 * @param codigoExemplar
	 * @param nivelMontarDados
	 * @return pessoaVO
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public PessoaVO consultarPessoaDoEmprestimoPeloExemplar(PessoaVO pessoaVO, Integer codigoExemplar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT p.*, e.tipopessoa as tipopessoemprestimo, unidadeensino.codigo as codigounidadeensino, unidadeensino.nome as nomeunidadeensino  FROM pessoa p ");
		sqlStr.append("INNER JOIN emprestimo e ON p.codigo = e.pessoa ");
		sqlStr.append("INNER JOIN itememprestimo ie ON e.codigo = ie.emprestimo ");
		sqlStr.append("left JOIN unidadeensino ON e.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("WHERE ie.exemplar = ? AND ie.situacao <> 'DE' ");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoExemplar });
			if (tabelaResultado.next()) {
				PessoaVO pessoaVO2 = montarDados(tabelaResultado, pessoaVO, nivelMontarDados, usuario);
				pessoaVO2.setMembroComunidade(tabelaResultado.getString("tipopessoemprestimo").equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor()));
				pessoaVO2.setAluno(tabelaResultado.getString("tipopessoemprestimo").equals(TipoPessoa.ALUNO.getValor()));
				pessoaVO2.setProfessor(tabelaResultado.getString("tipopessoemprestimo").equals(TipoPessoa.PROFESSOR.getValor()));
				pessoaVO2.setFuncionario(tabelaResultado.getString("tipopessoemprestimo").equals(TipoPessoa.FUNCIONARIO.getValor()));
				pessoaVO2.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("codigounidadeensino"));
				pessoaVO2.getUnidadeEnsinoVO().setNome(tabelaResultado.getString("nomeunidadeensino"));
				return pessoaVO2;
			} else {
				return new PessoaVO();
			}
		} finally {
			sqlStr = null;
		}
	}

	/***
	 * Mï¿½todo Responsável por retornar os professores que ministram aula na
	 * turma passada como parï¿½metro, levando em consideraï¿½ï¿½o o horario da turma
	 * e do professor
	 *
	 * @param codigoTurma
	 * @return List<PessoaVO> - lista de professores que lecionam em uma
	 *         determinada turma
	 */
	public List<PessoaVO> consultarPorTurma(int codigoTurma, Integer disciplina, String ano, String semestre, int nivelMontarDados, boolean trazerTurmaBaseTurmaAgrupada, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select distinct pessoa.* from horarioturma ");
		sqlStr.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" where (horarioturma.turma = ").append(codigoTurma);
		if (trazerTurmaBaseTurmaAgrupada) {
			sqlStr.append(" or  horarioturma.turma IN (SELECT ta.turmaorigem FROM turmaagrupada ta WHERE ta.turma = ").append(codigoTurma).append("))");
		} else {
			sqlStr.append(")");
		}
		if (ano != null && ano.trim().length() == 4) {
			sqlStr.append(" and (horarioturma.anovigente = '").append(ano).append("') ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			sqlStr.append(" AND (horarioturma.semestrevigente = '").append(semestre).append("')");
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			sqlStr.append(" and disciplina.codigo =  ").append(disciplina);
		}
		sqlStr.append(" order by pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/***
	 * Mï¿½todo Responsável por retornar os professores que ministram aula na
	 * turma passada como parï¿½metro, levando em consideraï¿½ï¿½o o horario da turma
	 * e do professor
	 *
	 * @param codigoTurma
	 * @return List<PessoaVO> - lista de professores que lecionam em uma
	 *         determinada turma
	 */
	public List<PessoaVO> consultarPorTurmaTurmaAgrupada(int codigoTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario, Boolean trazerTurmaBaseTurmaAgrupada) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select distinct pessoa.* from horarioturma ");
		sqlStr.append(" inner join turma on horarioturma.turma = turma.codigo ");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" inner join pessoa on horarioturmadiaitem.professor = pessoa.codigo ");
		sqlStr.append(" where (((turma.anual or turma.semestral) and horarioturma.anovigente = '").append(ano).append("') ");
		sqlStr.append(" or (turma.semestral and horarioturma.anovigente = '").append(ano).append("' and horarioturma.semestrevigente = '").append(semestre).append("') ");
		sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
		sqlStr.append(" and (turma.codigo = ").append(codigoTurma);
/*		A regra abaixo foi adicionado pois na tela de registro aula/nota estava permitindo registrar aula nota para a turma agrupada e a turma base,
 * 		gerando registros órfãos na base. Chamado número 16427*/
		if (trazerTurmaBaseTurmaAgrupada) {
			sqlStr.append(" or turma.codigo in ( select turmaorigem from turmaagrupada where turma = " + codigoTurma + " )) ");
		} else {
			sqlStr.append(" )");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<PessoaVO> consultarAlunosTurmaSituacao(Integer curso, Integer turma, String ano, String semestre, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT pessoa.* FROM Matricula");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno ");
		if (curso != 0) {
			sqlStr.append("AND Matricula.curso = ").append(curso).append(" ");
		}
		if (turma != 0) {
			sqlStr.append("AND MatriculaPeriodo.turma = ").append(turma).append(" ");
		}
		if (!ano.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (!situacao.equals("")) {
			sqlStr.append("AND matricula.situacao = '").append(situacao).append("' ");
		}
		sqlStr.append("ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public void validarDadosPessoaisVisaoAluno(PessoaVO obj, ConfiguracaoAtualizacaoCadastralVO config) throws Exception {
		Date hoje = new Date();
		if (config.getCodigo().intValue() == 0) {
			if (obj.getCEP().equals("")) {
				throw new Exception("O campo CEP (Dados Pessoais) deve ser informado.");
			}
			if (obj.getEndereco().equals("")) {
				throw new Exception("O campo ENDEREÇO (Dados Pessoais) deve ser informado.");
			}
			if (obj.getSetor().equals("")) {
				throw new Exception("O campo BAIRRO/SETOR (Dados Pessoais) deve ser informado.");
			}
			if (obj.getEstadoCivil().equals("")) {
				throw new Exception("O campo ESTADO CIVIL (Dados Pessoais) deve ser informado.");
			}
		} else {
			
			 if (!obj.getTelefoneRes().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneRes()).length() < 8) {
		        	throw new Exception ("O campo TEL.RESIDENCIAL deve conter 8 ou mais números informados.");
		        }
		        if (!obj.getTelefoneRecado().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneRecado()).length() < 8) {
		        	throw new Exception ("O campo TEL.RECADO deve conter 8 ou mais números informados.");
		        }
			
			
			if (!obj.getDataExpedicaoTituloEleitoral_Apresentar().equals("") && obj.getDataExpedicaoTituloEleitoral().after(hoje)) {
				throw new Exception("O campo DATA EXPEDIÇÃO TITULO ELEITORAL não pode conter uma data futura.");
			}
			if (!obj.getDataExpedicaoCertificadoMilitar_Apresentar().equals("")  &&  obj.getDataExpedicaoCertificadoMilitar().after(hoje)) {
				throw new Exception("O campo DATA EXPEDIÇÃO CEERTIFICADO MILITAR não pode conter uma data futura.");
			}

			if (config.getApresentarCampoCpf() && config.getPermitirAtualizarCpf()) {
				if (!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(obj.getCPF())).replaceAll(" ", ""))) {
					throw new Exception("O campo CPF (Dados Pessoais) é inválido.");
				}
			}
			if (config.getRgObrigatorio()) {
				if (obj.getRG().equals("")) {
					throw new Exception("O campo RG (Dados Pessoais) deve ser informado.");
				}
				if (obj.getDataEmissaoRG() == null) {
					throw new Exception("O campo DATA EMISSÃO RG (Dados Pessoais) deve ser informado.");
				} else {
					if (obj.getDataEmissaoRG().after(hoje)) {
						throw new Exception("O campo DATA EMISSÃO RG não pode conter uma data futura.");
					}
				}
				if (obj.getOrgaoEmissor().equals("")) {
					throw new Exception("O campo ORGÃO EMISSOR RG (Dados Pessoais) deve ser informado.");
				}
				if (obj.getEstadoEmissaoRG().equals("")) {
					throw new Exception("O campo ESTADO EMISSOR RG (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getCertidaoNascObrigatorio()) {
				if (obj.getCertidaoNascimento().equals("")) {
					throw new Exception("O campo CERTIDÃO NASCIMENTO (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getEnderecoObrigatorio()) {
				if (obj.getCEP().equals("")) {
					throw new Exception("O campo CEP (Dados Pessoais) deve ser informado.");
				}
				if (obj.getEndereco().equals("")) {
					throw new Exception("O campo ENDEREÇO (Dados Pessoais) deve ser informado.");
				}
				if (obj.getSetor().equals("")) {
					throw new Exception("O campo BAIRRO/SETOR (Dados Pessoais) deve ser informado.");
				}
				if (obj.getNumero().equals("")) {
					throw new Exception("O campo NÚMERO (Dados Pessoais) deve ser informado.");
				}
				if (!Uteis.isAtributoPreenchido(obj.getCidade().getNome())) {
					throw new Exception("O campo CIDADE (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getCelularObrigatorio()) {
				if (Uteis.retirarMascaraTelefone(obj.getCelular()).equals("")) {
					throw new Exception("O campo CELULAR (Dados Pessoais) deve ser informado.");
				} else {
					if (!Uteis.validaTelefone(obj.getCelular())) {
						throw new Exception("O campo CELULAR (Dados Pessoais) é inválido.");
					}
				}
			}
			if (config.getUmTelefoneFixoObrigatorio()) {
				if (Uteis.retirarMascaraTelefone(obj.getTelefoneRecado()).equals("") && Uteis.retirarMascaraTelefone(obj.getTelefoneRes()).equals("")) {
					throw new Exception("Deve ser informado pelo menos 1 (um) telefone fixo para contato.");
				} else {
					if (!Uteis.retirarMascaraTelefone(obj.getTelefoneRecado()).equals("") && !Uteis.validaTelefone(obj.getTelefoneRecado())) {
						throw new Exception("O campo TELEFONE RECADO (Dados Pessoais) é inválido.");
					}
					if (!Uteis.retirarMascaraTelefone(obj.getTelefoneRes()).equals("") && !Uteis.validaTelefone(obj.getTelefoneRes())) {
						throw new Exception("O campo TELEFONE RESIDENCIAL (Dados Pessoais) é inválido.");
					}
					 
				}
			}
			if (config.getDataNascimentoObrigatorio()) {
				if (!Uteis.isAtributoPreenchido(obj.getDataNasc())) {					
					throw new Exception("O campo DATA NASCIMENTO (Dados Pessoais) deve ser informado.");
				} else if (UteisData.validarDataInicialMaiorFinal(obj.getDataNasc(), hoje)) { 
					throw new Exception("Data de nascimento informada é maior que a data atual");
				}
			} else if (!config.getDataNascimentoObrigatorio() && Uteis.isAtributoPreenchido(obj.getDataNasc())) {
				if (UteisData.validarDataInicialMaiorFinal(obj.getDataNasc(), hoje)) { 
					throw new Exception("Data de nascimento informada é maior que a data atual");
				}
			}
			if (config.getEmailObrigatorio()) {
				if (obj.getEmail().equals("")) {
					throw new Exception("O campo EMAIL (Dados Pessoais) deve ser informado.");
				} else {
		            if (!Uteis.getValidaEmail(obj.getEmail())) {
		            	throw new ConsistirException("O campo EMAIL (Dados Pessoais) é inválido.");
		            }
				}
			}
			if (config.getDadoEleitoralObrigatorio()) {
				if (obj.getTituloEleitoral().equals("")) {
					throw new Exception("O campo TÍTULO ELEITORAL (Dados Pessoais) deve ser informado.");
				}
				if (obj.getDataExpedicaoTituloEleitoral() == null) {
					throw new Exception("O campo DATA EXPEDIÇÃO TITULO ELEITORAL (Dados Pessoais) deve ser informado.");
				} else {
					if (obj.getDataExpedicaoTituloEleitoral().after(hoje)) {
						throw new Exception("O campo DATA EXPEDIÇÃO TITULO ELEITORAL (Dados Pessoais) não pode conter uma data futura.");
					}
				}
				if (obj.getZonaEleitoral().equals("")) {
					throw new Exception("O campo ZONA ELEITORAL (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getEstadoCivilObrigatorio()) {
				if (obj.getEstadoCivil().equals("")) {
					throw new Exception("O campo ESTADO CIVIL (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getRegistroMilitarObrigatorio() && obj.getSexo().equals("M")) {
				if (obj.getCertificadoMilitar().equals("")) {
					throw new Exception("O campo CERTIFICADO MILITAR (Dados Pessoais) deve ser informado.");
				}
				if (obj.getDataExpedicaoCertificadoMilitar() == null) {
					throw new Exception("O campo DATA EXPEDIÇÃO CERTIFICADO MILITAR (Dados Pessoais) deve ser informado.");
				} else {
					if (obj.getDataExpedicaoCertificadoMilitar().after(hoje)) {
						throw new Exception("O campo DATA EXPEDIÇÃO CERTIFICADO MILITAR (Dados Pessoais) não pode conter uma data futura.");
					}
				}
				if (obj.getOrgaoExpedidorCertificadoMilitar().equals("")) {
					throw new Exception("O campo ORGÃO EXPEDIDOR CERTIFICADO MILITAR (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getCorRacaObrigatorio()) {
				if (obj.getCorRaca().equals("")) {
					throw new Exception("O campo COR/RAÇA (Dados Pessoais) deve ser informado.");
				}
			}
			if (config.getNaturalidadeNacionalidadeObrigatorio()) {
				if (obj.getNaturalidade().getCodigo().intValue() == 0) {
					throw new Exception("O campo NATURALIDADE (Dados Pessoais) deve ser informado.");
				}
				if (obj.getNacionalidade().getCodigo().intValue() == 0) {
					throw new Exception("O campo NACIONALIDADE (Dados Pessoais) deve ser informado.");
				}
			}
//			if (config.getRegistroMilitarObrigatorio()) {
//			}
		}
	}


	public void realizarVerificacaoFiltroFiliacaoParaPessoa(StringBuffer sql, String tipoPessoa) {
		if (tipoPessoa != null && !tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("RL")) {
				sql.append(" inner join filiacao on pessoa.codigo = filiacao.pais ");
			}
		}
	}

	public List<BuscaCandidatoVagaVO> consultaRapidaBuscaCandidatoVaga(BuscaCandidatoVagaVO obj) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT pessoa.codigo, pessoa.nome AS pessoa, cidade.nome AS cidade, ");
		sqlStr.append("pessoa.divulgarmeusdados, curso.nome AS curso , 0 as candidatosvagas, false as existeArquivoAdicional FROM pessoa ");
		sqlStr.append("INNER JOIN matricula ON matricula.aluno = pessoa.codigo  ");
		sqlStr.append("LEFT JOIN AreaProfissionalInteresseContratacao ON AreaProfissionalInteresseContratacao.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN AreaProfissional ON AreaProfissionalInteresseContratacao.AreaProfissional = AreaProfissional.codigo ");
		sqlStr.append("LEFT JOIN formacaoAcademica ON formacaoAcademica.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN formacaoExtraCurricular ON formacaoExtraCurricular.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN cidade ON pessoa.cidade = cidade.codigo ");
		sqlStr.append("LEFT JOIN estado ON cidade.estado = estado.codigo ");
		sqlStr.append("LEFT JOIN curso ON matricula.curso = curso.codigo  ");

		sqlStr.append(" WHERE pessoa.participaBancoCurriculum = true ");
		sqlStr.append(" AND matricula.situacao = 'AT' ");
		sqlStr.append(" AND pessoa.divulgarmeusdados = true ");

		if (obj.getCidade() != null && !obj.getCidade().getCodigo().equals(0)) {
			sqlStr.append(" AND cidade.codigo =").append(obj.getCidade().getCodigo());
		}
		if (obj.getCidade().getEstado() != null && !obj.getCidade().getEstado().getCodigo().equals(0)) {
			sqlStr.append(" AND estado.codigo =").append(obj.getCidade().getEstado().getCodigo());
		}
		if (obj.getCurso() != null && !obj.getCurso().getCodigo().equals(0)) {
			sqlStr.append(" AND curso.codigo =").append(obj.getCurso().getCodigo());
		}
		if (!obj.getSexo().equals("")) {
			sqlStr.append(" AND sexo ='").append(obj.getSexo()).append("' ");
		}
		if (obj.getIngles()) {
			sqlStr.append(" AND ingles =").append(obj.getIngles());
			if (!obj.getInglesNivel().equals("")) {
				sqlStr.append(" AND inglesNivel ='").append(obj.getInglesNivel()).append("' ");
			}
		}
		if (obj.getEspanhol()) {
			sqlStr.append(" AND espanhol =").append(obj.getEspanhol());
			if (!obj.getEspanholNivel().equals("")) {
				sqlStr.append(" AND espanholNivel ='").append(obj.getEspanholNivel()).append("' ");
			}
		}
		if (obj.getFrances()) {
			sqlStr.append(" AND frances =").append(obj.getFrances());
			if (!obj.getFrancesNivel().equals("")) {
				sqlStr.append(" AND francesNivel ='").append(obj.getFrancesNivel()).append("' ");
			}
		}
		if (!obj.getOutrosIdiomas().equals("")) {
			sqlStr.append(" AND outrosIdiomas =").append(obj.getOutrosIdiomas());
			if (!obj.getOutrosIdiomasNivel().equals("")) {
				sqlStr.append(" AND outrosIdiomasNivel ='").append(obj.getOutrosIdiomasNivel()).append("' ");
			}
		}

		if (obj.getAreaProfissional().getCodigo() != 0) {
			sqlStr.append(" AND areaProfissional.codigo = ").append(obj.getAreaProfissional().getCodigo()).append(" ");
		}
		if (!obj.getListaFormacaoAcademica().isEmpty()) {
			sqlStr.append(" AND (");
			String or = "OR ";
			int tamanhoLista = obj.getListaFormacaoAcademica().size();
			int aux = 1;
			for (FormacaoAcademicaVO fa : obj.getListaFormacaoAcademica()) {
				if (aux == tamanhoLista) {
					or = "";
				}
				sqlStr.append(" formacaoAcademica.curso ilike'%").append(fa.getCurso()).append("%' ");
				sqlStr.append(" AND formacaoAcademica.escolaridade ='").append(fa.getEscolaridade()).append("' ").append(or);
				aux++;
			}
			sqlStr.append(") ");
		}
		if (!obj.getListaFormacaoExtraCurricular().isEmpty()) {
			sqlStr.append(" AND (");
			for (FormacaoExtraCurricularVO fec : obj.getListaFormacaoExtraCurricular()) {
				sqlStr.append(" formacaoExtraCurricular.curso ilike'%").append(fec.getCurso()).append("%' ");
			}
			sqlStr.append(") ");
		}
		if (!obj.getTrabalha().equals("") && !obj.getTrabalha().equals("naoImportante")) {
			// caso o aluno esteja trabalhando
			if (obj.getTrabalha().equals("sim")) {
				sqlStr.append(" AND 1 = (select count(dadosComerciais.codigo) from dadosComerciais where dadosComerciais.pessoa = pessoa.codigo and  dadosComerciais.empregoAtual = true limit 1) ");
			} else if (obj.getTrabalha().equals("nao")) {
				// caso o aluno Não esteja trabalhando
				sqlStr.append(" AND 1 <> (select count(dadosComerciais.codigo) from dadosComerciais where dadosComerciais.pessoa = pessoa.codigo and  dadosComerciais.empregoAtual = true limit 1) ");
			}
		}
		sqlStr.append("  group by pessoa.codigo, pessoa.nome, cidade.nome, pessoa.divulgarmeusdados, curso.nome ");
		sqlStr.append(" ORDER BY pessoa ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBuscaCandidatoVaga(tabelaResultado);
	}

	public List<BuscaCandidatoVagaVO> consultaRapidaBuscaCandidatoVagaPorVaga(VagasVO obj) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT pessoa.codigo, pessoa.nome AS pessoa, cidade.nome AS cidade, ");
		sqlStr.append("pessoa.divulgarmeusdados, curso.nome AS curso, candidatosvagas.codigo as candidatosvagas, ");
		sqlStr.append(" case when (SELECT COUNT(CurriculumPessoa.codigo) FROM CurriculumPessoa WHERE CurriculumPessoa.pessoa = pessoa.codigo ) > 0 then true else false end as existeArquivoAdicional FROM pessoa ");
		sqlStr.append("INNER JOIN matricula ON matricula.aluno = pessoa.codigo  ");
		sqlStr.append("INNER JOIN candidatosvagas ON candidatosvagas.pessoa = pessoa.codigo ");
		sqlStr.append("INNER JOIN vagas ON candidatosvagas.vaga = vagas.codigo ");
		sqlStr.append("LEFT JOIN AreaProfissionalInteresseContratacao ON AreaProfissionalInteresseContratacao.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN AreaProfissional ON AreaProfissionalInteresseContratacao.AreaProfissional = AreaProfissional.codigo ");
		sqlStr.append("LEFT JOIN formacaoAcademica ON formacaoAcademica.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN formacaoExtraCurricular ON formacaoExtraCurricular.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN cidade ON pessoa.cidade = cidade.codigo ");
		sqlStr.append("LEFT JOIN curso ON matricula.curso = curso.codigo  ");

		sqlStr.append(" WHERE participaBancoCurriculum = 'TRUE' AND vagas.codigo = ").append(obj.getCodigo());

		sqlStr.append("  group by pessoa.codigo, pessoa.nome, cidade.nome, pessoa.divulgarmeusdados, curso.nome, candidatosvagas.codigo ");
		sqlStr.append(" ORDER BY pessoa ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBuscaCandidatoVaga(tabelaResultado);
	}

	public List<BuscaCandidatoVagaVO> montarDadosConsultaBuscaCandidatoVaga(SqlRowSet tabelaResultado) throws Exception {
		List<BuscaCandidatoVagaVO> vetResultado = new ArrayList<BuscaCandidatoVagaVO>(0);
		while (tabelaResultado.next()) {
			BuscaCandidatoVagaVO obj = new BuscaCandidatoVagaVO();
			montarDadosBasicoConsultaBuscaCandidatoVaga(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasicoConsultaBuscaCandidatoVaga(BuscaCandidatoVagaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do VO BuscaCandidatoVaga
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa"));
		obj.getCurso().setNome(dadosSQL.getString("curso"));
		obj.getCidade().setNome(dadosSQL.getString("cidade"));
		obj.getPessoa().setDivulgarMeusDados(dadosSQL.getBoolean("divulgarmeusdados"));
		obj.getCandidatosVagas().setCodigo(dadosSQL.getInt("candidatosvagas"));
		obj.getCandidatosVagas().setExisteArquivoAdicional(dadosSQL.getBoolean("existeArquivoAdicional"));
	}

	public void executarGeracaoNumeroCPF(PessoaVO obj) throws Exception {
		String novoCPF = Uteis.preencherComZerosPosicoesVagas(obj.getCodigo().toString(), 10);
		novoCPF = Uteis.adicionarMascaraCPF("T" + novoCPF);
		obj.setCPF(novoCPF);
		alterarCPFPorCodigo(novoCPF, obj.getCodigo());
	}

	@Override
	public void executarAtualizacaoDadosPessoaRenovacaoAutomatica(final PessoaVO obj,  final UsuarioVO usuario) throws Exception {
		alterar(obj, "pessoa", new AtributoPersistencia()
				.add("renovacaoAutomatica", obj.getRenovacaoAutomatica())
				,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}
	@Override
	public void executarAtualizacaoDadosPessoaPossuiAcessoVisaoPais(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Pessoa set  possuiAcessoVisaoPais=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, true);
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void executarAtualizacaoSenhaCertificadoParaDocumento(final PessoaVO obj, final UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Pessoa set  senhaCertificadoParaDocumento=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getSenhaCertificadoParaDocumento());
				sqlAlterar.setInt(2, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	public void alterarCPFPorCodigo(final String cpf, final Integer pessoa) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set cpf=? WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, cpf);
					sqlAlterar.setInt(++i, pessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDadosFiliacaoResponsavelLegal(PessoaVO obj, FiliacaoVO filiacao) throws ConsistirException {
		if (!obj.getFiliacaoVOs().isEmpty()) {
			Iterator i = obj.getFiliacaoVOs().iterator();
			while (i.hasNext()) {
				FiliacaoVO filiacaoVO = (FiliacaoVO) i.next();
				if (!filiacaoVO.getPais().getNome().equals(filiacao.getPais().getNome())) {
					if (filiacaoVO.getResponsavelLegal()) {
						filiacao.setResponsavelLegal(false);
						throw new ConsistirException("Já existe um Responsável Legal para este Aluno!");
					}
				}
			}
		}
	}

	// @Override
	// public void incluirPais(final PessoaVO obj, boolean verificarAcesso,
	// final UsuarioVO usuario) throws Exception {
	//
	// try {
	// Pessoa.incluir(getIdEntidade(), verificarAcesso, usuario);
	// PessoaVO.validarDados(obj,
	// getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false,
	// usuario));
	//
	// validarCPFUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
	//
	// if (!obj.getPossuiFilho()) {
	// obj.setQtdFilhos(null);
	// }
	// final String sql =
	// "INSERT INTO Pessoa( cpf, nome, rg, orgaoEmissor, endereco, setor, cidade, telefoneComer, telefoneRes, telefoneRecado, celular, email, ativo ) "
	// + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
	//
	// obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new
	// PreparedStatementCreator() {
	//
	// public PreparedStatement createPreparedStatement(Connection arg0) throws
	// SQLException {
	// PreparedStatement sqlInserir = arg0.prepareStatement(sql);
	// sqlInserir.setString(1, obj.getCPF());
	// sqlInserir.setString(2, obj.getNome().trim());
	// sqlInserir.setString(3, obj.getRG());
	// sqlInserir.setString(4, obj.getOrgaoEmissor());
	// sqlInserir.setString(5, obj.getEndereco());
	// sqlInserir.setString(6, obj.getSetor());
	// if (obj.getCidade().getCodigo().intValue() != 0) {
	// sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
	// } else {
	// sqlInserir.setNull(7, 0);
	// }
	// sqlInserir.setString(8, obj.getTelefoneComer());
	// sqlInserir.setString(9, obj.getTelefoneRes());
	// sqlInserir.setString(10, obj.getTelefoneRecado());
	// sqlInserir.setString(11, obj.getCelular());
	// sqlInserir.setString(12, obj.getEmail());
	// sqlInserir.setBoolean(13, obj.getAtivo());
	// return sqlInserir;
	// }
	// }, new ResultSetExtractor() {
	//
	// public Object extractData(ResultSet arg0) throws SQLException,
	// DataAccessException {
	// if (arg0.next()) {
	// obj.setNovoObj(Boolean.FALSE);
	// return arg0.getInt("codigo");
	// }
	// return null;
	// }
	// }));
	//
	// obj.setNovoObj(Boolean.FALSE);
	//
	// if (obj.getGerarNumeroCPF()) {
	// executarGeracaoNumeroCPF(obj);
	// }
	// } catch (Exception e) {
	// obj.setNovoObj(true);
	// obj.setCodigo(0);
	// throw e;
	// }
	// }
	// @Override
	// public void alterarPais(final PessoaVO obj, boolean verificarAcesso,
	// final UsuarioVO usuario) throws Exception {
	// PessoaVO.validarDados(obj,
	// getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false,
	// usuario));
	// final String sql =
	// "UPDATE Pessoa set  cpf=?, nome=?, rg=?, orgaoEmissor=?, endereco=?, setor=?, cidade=?, telefoneComer=?, telefoneRes=?, telefoneRecado=?, celular=?, email=?, ativo=?  "
	// +
	// " WHERE ((codigo = ?))";
	// getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
	//
	// public PreparedStatement createPreparedStatement(Connection arg0) throws
	// SQLException {
	// PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
	// sqlAlterar.setString(1, obj.getCPF());
	// sqlAlterar.setString(2, obj.getNome().trim());
	// sqlAlterar.setString(3, obj.getRG());
	// sqlAlterar.setString(4, obj.getOrgaoEmissor());
	// sqlAlterar.setString(5, obj.getEndereco());
	// sqlAlterar.setString(6, obj.getSetor());
	// if (obj.getCidade().getCodigo().intValue() != 0) {
	// sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
	// } else {
	// sqlAlterar.setNull(7, 0);
	// }
	// sqlAlterar.setString(8, obj.getTelefoneComer());
	// sqlAlterar.setString(9, obj.getTelefoneRes());
	// sqlAlterar.setString(10, obj.getTelefoneRecado());
	// sqlAlterar.setString(11, obj.getCelular());
	// sqlAlterar.setString(12, obj.getEmail());
	// sqlAlterar.setBoolean(13, obj.getAtivo());
	// sqlAlterar.setInt(14, obj.getCodigo().intValue());
	//
	// return sqlAlterar;
	// }
	// });
	//
	//
	// }
	@Override
	public PessoaVO consultarResponsavelFinanceiroAluno(Integer codigoAluno, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		PessoaVO responsavelFinanceiro = null;
		try {
			sb.append(getSQLPadraoConsultaBasica(false, false));
			sb.append(" inner join filiacao on filiacao.pais = pessoa.codigo ");
			sb.append(" where filiacao.responsavelFinanceiro = true and filiacao.aluno = ").append(codigoAluno).append(" limit 1");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (tabelaResultado.next()) {
				responsavelFinanceiro = new PessoaVO();
				montarDadosBasico(responsavelFinanceiro, tabelaResultado);
				responsavelFinanceiro.setNovoObj(false);
			}
			return responsavelFinanceiro;
		} catch (Exception e) {
			throw e;
		} finally {
			sb = null;
		}
	}

	@Override
	public List<PessoaVO> consultarAlunoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro, UsuarioVO usuarioLogado) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		try {
			sqlStr.append(" INNER JOIN Filiacao on filiacao.aluno = pessoa.codigo and filiacao.responsavelFinanceiro = true ");
			sqlStr.append(" WHERE filiacao.pais = (?) ");
			sqlStr.append(" ORDER BY Pessoa.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoResponsavelFinanceiro });
			return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}
	}

	@Override
	public List<PessoaVO> consultaRapidaPorNomeResponsavelFinanceiro(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on filiacao.pais = pessoa.codigo ");
		sqlStr.append(" WHERE sem_acentos(lower(pessoa.nome)) like(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sqlStr.append(" AND filiacao.aluno in (SELECT aluno from matricula where unidadeEnsino = ").append(unidadeEnsino).append(" )");
		}
		sqlStr.append(" and exists (select codigo from contareceber where contareceber.responsavelfinanceiro = filiacao.pais limit 1)");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}


	public List<PessoaVO> consultaRapidaPorNomeResponsavelFinanceiroListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on filiacao.pais = pessoa.codigo and filiacao.responsavelFinanceiro = true ");
		sqlStr.append(" WHERE sem_acentos(lower(pessoa.nome)) like(sem_acentos(?))");

		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" AND filiacao.aluno in (SELECT aluno from matricula where unidadeEnsino in(");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0)) ");
		}

		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}

	@Override
	public List<PessoaVO> consultaRapidaPorCpfResponsavelFinanceiro(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on filiacao.pais = pessoa.codigo ");
		sqlStr.append(" WHERE (UPPER(replace(replace((pessoa.cpf),'.',''),'-','')) LIKE UPPER('").append(Uteis.retirarMascaraCPF(valorConsulta)).append("%') OR UPPER(replace(replace((pessoa.cpf),'.',''),'-','')) LIKE UPPER('").append(Uteis.retirarMascaraCPF(valorConsulta)).append("%')) ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sqlStr.append(" AND filiacao.aluno in (SELECT aluno from matricula where unidadeEnsino = ").append(unidadeEnsino).append(" )");
		}
		sqlStr.append(" and exists (select codigo from contareceber where contareceber.responsavelfinanceiro = filiacao.pais limit 1)");
		sqlStr.append(" ORDER BY Pessoa.cpf ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}

	public List<PessoaVO> consultaRapidaPorCpfResponsavelFinanceiroListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on filiacao.pais = pessoa.codigo and filiacao.responsavelFinanceiro = true ");
		sqlStr.append(" WHERE (UPPER(replace(replace((pessoa.cpf),'.',''),'-','')) LIKE UPPER('").append(Uteis.retirarMascaraCPF(valorConsulta)).append("%') OR UPPER(replace(replace((filiacao.cpf),'.',''),'-','')) LIKE UPPER('").append(Uteis.retirarMascaraCPF(valorConsulta)).append("%')) ");
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" AND filiacao.aluno in (SELECT aluno from matricula where unidadeEnsino in(");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0)) ");
		}
		sqlStr.append(" ORDER BY Pessoa.cpf ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}

	@Override
	public List<PessoaVO> consultaRapidaPorNomeAlunoResponsavelFinanceiro(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on filiacao.pais = pessoa.codigo ");
		sqlStr.append(" INNER JOIN Pessoa as aluno on filiacao.aluno = aluno.codigo ");
		sqlStr.append(" WHERE sem_acentos(UPPER(aluno.nome)) LIKE sem_acentos(UPPER(?)) ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sqlStr.append(" AND filiacao.aluno in (SELECT aluno from matricula where unidadeEnsino = ").append(unidadeEnsino).append(" )");
		}
		sqlStr.append(" and exists (select codigo from contareceber where contareceber.responsavelfinanceiro = filiacao.pais limit 1)");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}

	public List<PessoaVO> consultaRapidaPorNomeAlunoResponsavelFinanceiroListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on filiacao.pais = pessoa.codigo and filiacao.responsavelFinanceiro = true ");
		sqlStr.append(" INNER JOIN Pessoa as aluno on filiacao.aluno = aluno.codigo ");
		sqlStr.append(" WHERE sem_acentos(UPPER(aluno.nome)) LIKE sem_acentos(UPPER(?)) ");
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" AND filiacao.aluno in (SELECT aluno from matricula where unidadeEnsino in(");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0)) ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return (montarDadosConsultaRapidaVisaoAluno(tabelaResultado));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public Boolean consultaSePessoaAluno(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select aluno from  pessoa  WHERE codigo = ");
		sqlStr.append(valorConsulta.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("aluno");
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOcultarDadosCRM(Integer pessoa, boolean ocultar) {
		getConexao().getJdbcTemplate().update("UPDATE pessoa set ocultarDadosCRM = ? where codigo = ? ", ocultar, pessoa);
	}

	@Override
	public List<PessoaVO> consultarPessoaInteresseAreaProfissinal(List<AreaProfissionalVO> areaProfissional, List<CidadeVO> cidade, List<EstadoVO> estado, CursoVO curso) {
		StringBuilder sql = new StringBuilder("select distinct pessoa.codigo, pessoa.nome, pessoa.email, cidade.nome as cidade, estado.nome as estado from matricula");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno inner join cidade on pessoa.cidade = cidade.codigo ");
		sql.append(" left join estado on estado.codigo = cidade.estado ");
		sql.append(" left join areaprofissionalinteressecontratacao on areaprofissionalinteressecontratacao.codigo = pessoa.codigo ");
		sql.append(" where pessoa.participabancocurriculum = true  ");
		sql.append(" and matricula.situacao = 'AT' ");
		boolean entrou = false;
		if (cidade != null && !cidade.isEmpty()) {
			sql.append(" and (pessoa.cidade in (");
			Iterator j = cidade.iterator();
			while (j.hasNext()) {
				CidadeVO cid = (CidadeVO) j.next();
				sql.append(cid.getCodigo().intValue()).append(", ");
			}
			sql.append("0 ) ");
			entrou = true;
		}
		if (estado != null && !estado.isEmpty()) {
			if (!entrou) {
				sql.append(" and ");
			} else {
				sql.append(" or ");
			}
			sql.append(" cidade.estado in ( ");
			Iterator i = estado.iterator();
			while (i.hasNext()) {
				EstadoVO est = (EstadoVO) i.next();
				sql.append(est.getCodigo().intValue()).append(", ");
			}
			sql.append("0 ) ");
		}
		if (entrou) {
			sql.append(" ) ");
		}
		entrou = false;
		if (areaProfissional != null && !areaProfissional.isEmpty()) {
			if (!entrou) {
				sql.append(" and ");
			} else {
				sql.append(" or ");
			}
			sql.append(" areaprofissionalinteressecontratacao.areaprofissional in ( ");
			Iterator i = areaProfissional.iterator();
			while (i.hasNext()) {
				AreaProfissionalVO area = (AreaProfissionalVO) i.next();
				sql.append(area.getCodigo().intValue()).append(", ");
			}
			sql.append("0 ) ");
		}
		if (curso != null && curso.getCodigo() != null && curso.getCodigo() > 0) {
			sql.append(" and matricula.curso = ").append(curso.getCodigo());
		}
		sql.append(" order by nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
		PessoaVO pessoaVO = null;
		while (rs.next()) {
			pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(rs.getInt("codigo"));
			pessoaVO.setNome(rs.getString("nome"));
			pessoaVO.setEmail(rs.getString("email"));
			pessoaVO.getCidade().setNome(rs.getString("cidade"));
			pessoaVO.getCidade().getEstado().setNome(rs.getString("estado"));
			pessoaVOs.add(pessoaVO);
		}
		return pessoaVOs;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void uploadArquivoCurriculum(FileUploadEvent upload, PessoaVO pessoa, CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		if (!upload.getUploadedFile().getName().contains(".pdf")) {
			throw new Exception("O arquivo deve ter extensão pdf.");
		}
		String nomeArquivo = pessoa.getCodigo() + "_" + new Date().getTime() + ".pdf";
		curriculumPessoaVO.getPessoa().setCodigo(pessoa.getCodigo());
		curriculumPessoaVO.setNomeApresentacaoArquivo(upload.getUploadedFile().getName().substring(0, upload.getUploadedFile().getName().lastIndexOf(".pdf")));
		curriculumPessoaVO.setNomeRealArquivo(nomeArquivo);
		ArquivoHelper.salvarArquivoNaPastaTemp(upload, nomeArquivo, PastaBaseArquivoEnum.CURRICULUM_TMP.getValue(), configuracaoGeralSistemaVO, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarArquivoCurriculum(PessoaVO pessoa, CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		// getFacadeFactory().getCurriculumPessoaFacade().incluir(curriculumPessoaVO,
		// configuracaoGeralSistemaVO, usuario);
		getFacadeFactory().getCurriculumPessoaFacade().validarDados(curriculumPessoaVO);
		pessoa.getCurriculumPessoaVOs().add(0, curriculumPessoaVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deletarArquivoCurriculum(PessoaVO pessoaVO, CurriculumPessoaVO curriculumPessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		// getFacadeFactory().getCurriculumPessoaFacade().excluir(curriculumPessoaVO,
		// configuracaoGeralSistemaVO, usuario);
		int index = 0;
		for (CurriculumPessoaVO curriculumPessoaVO2 : pessoaVO.getCurriculumPessoaVOs()) {
			if (curriculumPessoaVO2.getCodigo().equals(curriculumPessoaVO.getCodigo())) {
				pessoaVO.getCurriculumPessoaVOs().remove(index);
				if (!curriculumPessoaVO2.isNovoObj()) {
					pessoaVO.getCurriculumPessoaExcluir().add(curriculumPessoaVO2);
				}
				return;
			}
			index++;
		}
	}

	@Override
	public List<PessoaVO> consultaAlunosAtivosParaReposicaoDisciplinaEnvioEmail(Integer codigoTurma, String ano, String semestre, List<Integer> listaCodigoDisciplinas, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
		sqlStr.append(" select p.codigo, p.email, p.nome, array_to_string(array( ");
		sqlStr.append(" SELECT disciplina.nome as disciplina		  ");
		sqlStr.append(" FROM pessoa  ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso  ");
		sqlStr.append("  INNER JOIN disciplina ON disciplina.codigo = mptd.disciplina ");
		sqlStr.append(" where matricula.situacao = 'AT' AND matriculaperiodo.turma != mptd.turma ");
		sqlStr.append(" AND mptd.turma = ").append(codigoTurma);
		sqlStr.append(" AND matriculaPeriodo.situacaomatriculaperiodo in ('CO', 'AT') ");
		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = ''))  ");
		sqlStr.append(" and p.codigo = pessoa.codigo ");
		sqlStr.append(" group by  pessoa.codigo, disciplina.nome ");
		sqlStr.append(" ) , ', ') as disciplinas from pessoa as p ");
		sqlStr.append(" where p.codigo in ( ");
		sqlStr.append(" SELECT pessoa.codigo ");
		sqlStr.append(" FROM pessoa  ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso  ");
		sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = mptd.disciplina ");
		sqlStr.append(" where matricula.situacao = 'AT' AND matriculaperiodo.turma != mptd.turma ");
		sqlStr.append(" AND mptd.turma = ").append(codigoTurma);
		sqlStr.append(" AND matriculaPeriodo.situacaomatriculaperiodo in ('CO', 'AT') ");
		if (!listaCodigoDisciplinas.isEmpty()) {
			sqlStr.append(" AND disciplina.codigo in(0 ");
			for (Integer codigo : listaCodigoDisciplinas) {
				sqlStr.append(", ").append(codigo);
			}
			sqlStr.append(")");
		}
		sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) 		 ");
		sqlStr.append(" group by  pessoa.codigo, disciplina.nome ");
		sqlStr.append(" )  ");

		// sqlStr.append(" SELECT distinct pessoa.codigo,pessoa.email, pessoa.email2, pessoa.nome,  pessoa.cpf, pessoa.rg, pessoa.certidaoNascimento, ");
		// sqlStr.append(" pessoa.participabancocurriculum, pessoa.informacoesverdadeiras, pessoa.informacoesAdicionais, pessoa.divulgarmeusdados, pessoa.telefoneRes, ");
		// sqlStr.append(" pessoa.celular, pessoa.email, pessoa.email2, pessoa.dataUltimaAlteracao,  pessoa.informacoesverdadeiras, pessoa.divulgarmeusdados, ");
		// sqlStr.append(" arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, turma.identificadorturma as turma, disciplina. FROM pessoa ");
		// sqlStr.append(" LEFT JOIN matricula ON matricula.aluno = pessoa.codigo ");
		// sqlStr.append(" LEFT JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		// sqlStr.append(" LEFT JOIN Turma on turma.codigo = matriculaPeriodo.turma ");
		// sqlStr.append(" LEFT JOIN UnidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		// sqlStr.append(" LEFT JOIN Arquivo on arquivo.codigo = pessoa.arquivoimagem ");
		// sqlStr.append(" INNER JOIN matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaperiodo.codigo ");
		// sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		// sqlStr.append(" INNER JOIN disciplina ON disciplina.codigo = mptd.disciplina ");
		// sqlStr.append("WHERE mptd.turma = ").append(codigoTurma);
		// sqlStr.append(" AND matriculaperiodo.turma != mptd.turma");
		// if (!listaCodigoDisciplinas.isEmpty()) {
		// sqlStr.append(" AND disciplina.codigo in(0 ");
		// for (Integer codigo : listaCodigoDisciplinas) {
		// sqlStr.append(", ").append(codigo);
		// }
		// sqlStr.append(")");
		// }
		// sqlStr.append(" AND matricula.situacao = 'AT' ");
		// sqlStr.append(" AND matriculaPeriodo.situacaomatriculaperiodo in ('CO', 'AT') ");
		// sqlStr.append(" AND ((curso.periodicidade = 'IN' AND matriculaperiodo.ano = '' AND matriculaperiodo.semestre = '' ) OR");
		// sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '").append(semestre).append("') OR");
		// sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodo.ano = '").append(ano).append("' AND matriculaperiodo.semestre = '')) ");
		// sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			PessoaVO pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(tabelaResultado.getInt("codigo"));
			pessoaVO.setNome(tabelaResultado.getString("nome"));
			pessoaVO.setEmail(tabelaResultado.getString("email"));
			pessoaVO.setDisciplinas(tabelaResultado.getString("disciplinas"));
			pessoaVOs.add(pessoaVO);
		}
		return pessoaVOs;
	}

	public SqlRowSet consultarAniversariantesDia(boolean professor, boolean funcionario, boolean aluno, boolean exaluno) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.email as \"Pessoa.email\" ");
		if (aluno || exaluno) {
			sql.append(" from matricula ");
			sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		} else {
			sql.append(" from pessoa ");
		}
		sql.append(" where DATE_PART('DAY', datanasc ) = DATE_PART('DAY', current_date ) and DATE_PART('MONTH', datanasc ) = DATE_PART('MONTH', current_date ) ");
		if (professor) {
			sql.append(" and pessoa.professor = true");
		} else if (funcionario) {
			sql.append(" and pessoa.funcionario = true and (pessoa.professor = false or pessoa.professor is null)");
		} else if (aluno) {
			sql.append(" and pessoa.aluno = true and (pessoa.funcionario = false or pessoa.funcionario is null) and (pessoa.professor = false or pessoa.professor is null) and matricula.situacao <> 'TR' and matricula.situacao <> 'CA' and matricula.situacao <> 'FO' and matricula.situacao <> 'CF'");
		} else if (exaluno) {
			sql.append(" and pessoa.aluno = true and (pessoa.funcionario = false or pessoa.funcionario is null) and (pessoa.professor = false or pessoa.professor is null) and (matricula.situacao = 'TR' or matricula.situacao = 'CA' or matricula.situacao = 'FO' or matricula.situacao = 'CF')");
		}
		sql.append(" limit 200");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	@Override
	public List consultarPorNomeEProcessoSeletivo(String valorConsulta, int procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN Inscricao on Inscricao.candidato = Pessoa.codigo ");
		sqlStr.append(" INNER JOIN ProcSeletivo on ProcSeletivo.codigo = Inscricao.procSeletivo ");
		sqlStr.append(" WHERE pessoa.nome ilike '%").append(valorConsulta.toLowerCase()).append("%' ");
		sqlStr.append(" AND ProcSeletivo.codigo = ").append(procSeletivo);
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasico(obj, tabelaResultado);
			if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_COMBOBOX) {
				obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, false, usuario));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List consultarPorCPFEProcessoSeletivo(String valorConsulta, int procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN Inscricao on Inscricao.candidato = Pessoa.codigo ");
		sqlStr.append(" INNER JOIN ProcSeletivo on ProcSeletivo.codigo = Inscricao.procSeletivo ");
		sqlStr.append(" WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE('").append(Uteis.retirarMascaraCPF(valorConsulta)).append("%')");
		sqlStr.append(" AND ProcSeletivo.codigo = ").append(procSeletivo);
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasico(obj, tabelaResultado);
			if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_COMBOBOX) {
				obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, false, usuario));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List consultarPorRGEProcessoSeletivo(String valorConsulta, int procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" INNER JOIN Inscricao on Inscricao.candidato = Pessoa.codigo ");
		sqlStr.append(" INNER JOIN ProcSeletivo on ProcSeletivo.codigo = Inscricao.procSeletivo ");
		sqlStr.append(" WHERE LOWER (pessoa.rg) LIKE('").append(valorConsulta.toLowerCase()).append("%')");
		sqlStr.append(" AND ProcSeletivo.codigo = ").append(procSeletivo);
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			montarDadosBasico(obj, tabelaResultado);
			if (nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSBASICOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_COMBOBOX) {
				obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, false, usuario));
			}
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public PessoaVO consultarDadosGerarNotaFiscalSaida(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT codigo, nome, cpf, email, email2, endereco, setor, numero, cep, complemento, cidade FROM Pessoa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { pessoa.intValue() });
		if (tabelaResultado.next()) {
			return (montarDadosGerarNotaFiscalSaida(tabelaResultado, usuario));
		}
		return new PessoaVO();
	}

	public PessoaVO montarDadosGerarNotaFiscalSaida(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception {
		PessoaVO obj = new PessoaVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setNome(rs.getString("nome"));
		obj.setEmail(rs.getString("email"));
		obj.setEmail2(rs.getString("email2"));
		obj.setEndereco(rs.getString("endereco"));
		obj.setSetor(rs.getString("setor"));
		obj.setNumero(rs.getString("numero"));
		obj.setCEP(rs.getString("CEP"));
		obj.setComplemento(rs.getString("complemento"));
		obj.setCPF(rs.getString("cpf"));
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(rs.getInt("cidade"), false, usuarioVO));
		obj.setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(obj.getCodigo(), false, usuarioVO));
		return obj;
	}

	public PessoaVO consultarPorChavePrimariaTipoPessoa(Integer codigo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, aluno, professor, funcionario, candidato, PossuiAcessoVisaoPais from Pessoa where codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PessoaVO obj = new PessoaVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setAluno(tabelaResultado.getBoolean("aluno"));
			obj.setProfessor(tabelaResultado.getBoolean("professor"));
			obj.setFuncionario(tabelaResultado.getBoolean("funcionario"));
			obj.setCandidato(tabelaResultado.getBoolean("candidato"));
			obj.setPossuiAcessoVisaoPais(tabelaResultado.getBoolean("PossuiAcessoVisaoPais"));
		}
		return obj;
	}

	public List<PessoaVO> consultarCoordenadorCursoAluno(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = new StringBuffer("");
		sqlStr.append(" SELECT DISTINCT pessoa.*, matriculaperiodo.turma FROM matricula ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = ( ");
		sqlStr.append(" SELECT codigo FROM matriculaPeriodo WHERE MatriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" ORDER BY (MatriculaPeriodo.ano ||'/' || MatriculaPeriodo.semestre) DESC LIMIT 1) ");
		sqlStr.append(" INNER JOIN cursocoordenador ON cursocoordenador.unidadeensino = matricula.unidadeensino AND cursocoordenador.curso = matricula.curso ");
		sqlStr.append(" AND CASE WHEN cursocoordenador.turma != 0 THEN cursocoordenador.turma = matriculaperiodo.turma ELSE true END ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(rs);
	}

	public List<PessoaVO> consultarCoordenadorCursoUnidadeEnsino(Integer unidadeEnsino, Integer pessoa, Boolean ativo,  UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = new StringBuffer("");
		sqlStr.append(" SELECT DISTINCT pessoa.*, 0 as turma FROM cursocoordenador ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" WHERE 1=1 ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sqlStr.append(" and unidadeensino = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(pessoa)){
			sqlStr.append(" AND pessoa.codigo != ").append(pessoa);
		}
		if(Uteis.isAtributoPreenchido(ativo)){
			sqlStr.append(" AND pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaComunicadoInterno(rs);
	}

	@Override
	public List<PessoaVO> consultarCoordenadorCursoTurmaUnidadeEnsino(Integer curso, Integer turma, Integer unidadeEnsino,  UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDocumentoAssinado();
		sqlStr.append(" INNER JOIN funcionario ON pessoa.codigo = funcionario.pessoa  ");
		sqlStr.append(" INNER JOIN cursocoordenador ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" WHERE pessoa.ativo and cursocoordenador.tipocoordenadorcurso != 'ESTAGIO'  ");
		 if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			 sqlStr.append(" AND  cursocoordenador.unidadeEnsino = ").append(unidadeEnsino);
		 }
		 if(Uteis.isAtributoPreenchido(curso)){
			 sqlStr.append(" AND  cursocoordenador.curso = ").append(curso);
		 }
		 if(Uteis.isAtributoPreenchido(turma)){
			 sqlStr.append(" AND  (cursocoordenador.turma = ").append(turma);
			 sqlStr.append(" or (cursocoordenador.turma is null ");
			 sqlStr.append(" and  not exists (select cc.codigo from cursocoordenador as cc where cc.unidadeEnsino = cursocoordenador.unidadeensino ");
			 sqlStr.append(" and  cc.curso = cursocoordenador.curso ");
			 sqlStr.append(" and  cc.turma is not null and cc.turma = ").append(turma).append(") ");
			 sqlStr.append(" and  (cursocoordenador.curso = (select curso from turma where codigo = ").append(turma).append(" and curso is not null) ");
			 sqlStr.append(" or  cursocoordenador.curso in (select distinct turma.curso from turmaagrupada inner join turma on turma.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = ").append(turma).append(" and turma.curso is not null)) ");
			 sqlStr.append(" ))");
		 }
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosParaDocumentoAssinado(rs);
	}

	/**
	 * @param matricula
	 * @return PessoaVO
	 * @author Leonardo Riciolle
	 * Metodo responsavel por retorna os responsaveis
	 * de um aluno e possuam email para serem notificados.
	 */
	@Override
	public List<PessoaVO> consultarResponsavelLegalAluno(Integer codigoAluno, UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = new StringBuffer("");
		sqlStr.append(" SELECT pessoa.codigo, pessoa.nome, pessoa.email from filiacao ");
		sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = filiacao.pais");
		sqlStr.append(" WHERE filiacao.aluno = ").append(codigoAluno);
		sqlStr.append(" AND (filiacao.tipo in ('PA', 'MA') OR filiacao.responsavellegal = true)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
		PessoaVO obj = null;
		while (rs.next()) {
			obj = new PessoaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNome(rs.getString("nome"));
			obj.setEmail(rs.getString("email"));
			obj.setNovoObj(false);
			pessoaVOs.add(obj);
			obj = null;
		}
		return pessoaVOs;
	}

	/**
	 * Método responsável por retornar o código do professor referente ao horário detalhado da turma no ano e semestre vigente.
	 */
	@Override
	public Integer consultarCodigoProfessorPorHorarioTurmaDetalhadoTurmaDisciplinaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, boolean consultarDisciplinaEquivalente) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct pessoa.codigo as codigopessoa from horarioturma");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sqlStr.append(" inner join turma on turma.codigo = 	horarioturma.turma ");
		sqlStr.append(" where anovigente = '").append(ano+"'").append(" and semestrevigente = '").append(semestre+"'");
		sqlStr.append(" and (horarioturma.turma = ").append(codigoTurma).append(" or horarioturma.turma in (select turmaorigem from turmaagrupada  where  turma = ").append(codigoTurma).append("))");
		if (consultarDisciplinaEquivalente) {
			sqlStr.append(" and (disciplina.codigo = ").append(codigoDisciplina)
			.append(" or (exists (select 1 from turmaagrupada where turma = ").append(codigoTurma).append(") ")
			.append(" and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina)
			.append(" union ")
			.append(" select disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina)
			.append(" )) ");
			sqlStr.append(" or (turma.subturma  and turma.turmaagrupada	and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina);
			sqlStr.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(codigoDisciplina);
			sqlStr.append(" ))) ");
		} else {
			sqlStr.append(" and disciplina.codigo = ").append(codigoDisciplina);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()) {
			return rs.getInt("codigopessoa");
		} else {
			return 0;
		}
	}

	/**
	 * @author Victor Hugo 10/12/2014
	 */
	public PessoaVO consultarPorMatricula(String matricula, Integer nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select * from pessoa");
		sqlStr.append(" inner join matricula on matricula.aluno = pessoa.codigo");
		sqlStr.append(" where matricula = '").append(matricula+"'");


		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		PessoaVO pessoaVO = new PessoaVO();

		if(rs.next()) {
			pessoaVO = montarDados(rs, pessoaVO, Uteis.isAtributoPreenchido(nivelMontarDados) ? nivelMontarDados : Uteis.NIVELMONTARDADOS_TODOS, null);
		}
		return pessoaVO;
	}
	
	public PessoaVO consultarPorMatricula(String matricula) throws Exception {
		return consultarPorMatricula(matricula, null);
	}
	
	public List<PessoaVO> consultarFiliacaoPorPessoa(Integer pessoa, UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = new StringBuffer("");
		sqlStr.append(" SELECT * FROM  ( ");
		sqlStr.append(" 	SELECT * FROM pessoa WHERE codigo = ").append(pessoa);
		sqlStr.append(" 	UNION ");
		sqlStr.append(" 	SELECT pais.* FROM filiacao ");
		sqlStr.append(" 	LEFT JOIN pessoa on pessoa.codigo = filiacao.aluno ");
		sqlStr.append(" 	LEFT JOIN pessoa as pais on pais.codigo = filiacao.pais ");
		sqlStr.append(" 	WHERE pessoa.codigo = ").append(pessoa);
		sqlStr.append(" ) as t ");
		sqlStr.append(" ORDER BY t.nome ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
		PessoaVO pessoaVO = null;
		while (dadosSQL.next()) {
			pessoaVOs.add(montarDados(dadosSQL, pessoaVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		return pessoaVOs;
	}

	@Override
	public List<PessoaVO> consultaRapidaResumidaPorNomePai(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" inner join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'PA' ");
		sqlStr.append(" inner join Pessoa pai on filiacao.pais = pai.codigo ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (sem_acentos(pai.nome) ilike sem_acentos('"+ Uteis.removerAcentuacao(valorConsulta.toLowerCase()) +"%')) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}

		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override
	public Integer consultaTotalRegistroRapidaResumidaPorNomePai(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT COUNT(distinct pessoa.codigo) FROM pessoa ");
		sqlStr.append(" inner join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'PA' ");
		sqlStr.append(" inner join Pessoa pai on filiacao.pais = pai.codigo ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append(" WHERE (sem_acentos(pai.nome) ilike sem_acentos('" + Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%')) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;

	}

	@Override
	public List<PessoaVO> consultaRapidaResumidaPorResponsavelLegal(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(false, false);
		sqlStr.append(" inner join filiacao on filiacao.aluno = pessoa.codigo  ");
		sqlStr.append(" inner join Pessoa responsavel on filiacao.pais = responsavel.codigo ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE (sem_acentos(responsavel.nome) ilike sem_acentos('" + Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%')) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		sqlStr.append(" and (filiacao.tipo = 'RL' or filiacao.responsavellegal) ");

		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override
	public Integer consultaTotalRegistroRapidaResumidaPorResponsavelLegal(String valorConsulta, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT COUNT(distinct pessoa.codigo) FROM pessoa ");
		sqlStr.append(" inner join filiacao on filiacao.aluno = pessoa.codigo   ");
		sqlStr.append(" inner join Pessoa responsavel on filiacao.pais = responsavel.codigo ");
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append(" WHERE (sem_acentos(responsavel.nome) ilike sem_acentos('" + Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%')) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		sqlStr.append(" and (filiacao.tipo = 'RL' or filiacao.responsavellegal) ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	@Override
	public List<PessoaVO> consultar(String campoConsulta, String valorConsulta, TipoPessoa tipoPessoa, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		if (valorConsulta.length() < 2) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		if (campoConsulta.equals("nome")) {
			return getFacadeFactory().getPessoaFacade().consultarPorNome(valorConsulta, tipoPessoa.getValor(), false, nivelMontarDados, usuarioVO);
		}
		if (campoConsulta.equals("cpf")) {
			return getFacadeFactory().getPessoaFacade().consultarPorCPF(valorConsulta, tipoPessoa.getValor(), false, nivelMontarDados, usuarioVO);
		}
		return new ArrayList<PessoaVO>();
	}


	/**
	 * Cria prospect da pessoa de acordo com a regra abaixo:
	 * Busca uma lista de possíveis propects validando por CPF e depois por Nome e Email
	 * Caso não retorne nenhuma possibilidade um prospect novo é criado,
	 * caso retorne um prospect e este não está vinculado a uma pessoa diferente então o mesmo vinculado a pessoa em questão
	 * caso retorne mais de uma possibilidade de prospect não é realizado nenhum vincula e o prospect não é criado.
	 *
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProspectsVO realizarVinculoPessoaProspect(PessoaVO obj, UsuarioVO usuario) throws Exception {
		List<ProspectsVO> prospectsVOs = new ArrayList<ProspectsVO>();
	    boolean localizadoPeloCpf = false;
	    if(Uteis.isAtributoPreenchido(obj.getCodigo())) {
	    	ProspectsVO prospectVO = getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
	    	if(Uteis.isAtributoPreenchido(prospectVO)) {
	    	return prospectVO;	    	
	    	}
	    }
	    
	    if (prospectsVOs.isEmpty() && Uteis.isAtributoPreenchido(obj.getCPF())) {
	    	prospectsVOs.addAll(getFacadeFactory().getProspectsFacade().consultarPorCpf(obj.getCPF(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuario, null));
	    }
		if (prospectsVOs.isEmpty()) {
			prospectsVOs.addAll(getFacadeFactory().getProspectsFacade().consultarPossivelProspectVincularPessoa(obj.getCodigo(), obj.getNome(), obj.getEmail(), obj.getCPF(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}else {
			localizadoPeloCpf = true;
		}
		ProspectsVO pro = null;
		if (prospectsVOs.isEmpty()) {
			pro = new ProspectsVO();
			pro.setTipoProspect(TipoProspectEnum.FISICO);
			pro.setCpf(obj.getCPF());
			pro.setInativo(obj.getGerarProspectInativo());
			pro = getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(obj.getCPF(), pro, usuario);
			if (!pro.getEmailPrincipal().equals("")) {
				getFacadeFactory().getProspectsFacade().incluirSemValidarDados(pro, false, usuario, null);
			}
		} else if (!prospectsVOs.isEmpty()) {
			/** Criado validacao para se a lista estiver preenchida para garantir que o usuario consiga fazer a inscricao da matricula externa e nao criando o prospect para o usuario caso encontre um prospect ja existente.
			 */
			pro = prospectsVOs.get(0);
			if(localizadoPeloCpf) {
				List<ProspectsVO> prospectsVOs2 = getFacadeFactory().getProspectsFacade().consultarPossivelProspectVincularPessoa(obj.getCodigo(), obj.getNome(), obj.getEmail(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if(Uteis.isAtributoPreenchido(prospectsVOs2)) {
					for (Iterator iterator = prospectsVOs2.iterator(); iterator.hasNext();) {
						ProspectsVO prospectsVO = (ProspectsVO) iterator.next();
						if(!prospectsVO.getCodigo().equals(pro.getCodigo())) {
							getFacadeFactory().getProspectsFacade().unificarProspects(pro.getCodigo(), prospectsVO.getCodigo(), usuario);
						}											
					}
				}
			}
			if((!Uteis.isAtributoPreenchido(pro.getPessoa().getCodigo()) || (Uteis.isAtributoPreenchido(pro.getPessoa().getCodigo()) && pro.getPessoa().getCodigo().equals(obj.getCodigo())))){
				if (!Uteis.isAtributoPreenchido(pro.getPessoa())) {
					pro.setPessoa(obj);
					getFacadeFactory().getProspectsFacade().alterarPessoaProspect(pro, usuario);
				}
				if (!obj.getEmail().equals("")) {
					getFacadeFactory().getProspectsFacade().alterarProspectConformePessoa(obj, false, usuario);
				}
			}
		}else{
			pro = new ProspectsVO();
			pro.setTipoProspect(TipoProspectEnum.FISICO);
			pro.setCpf(obj.getCPF());
			pro.setInativo(obj.getGerarProspectInativo());
			pro = getFacadeFactory().getProspectsFacade().realizarPreenchimentoProspectPorPessoa(obj.getCPF(), pro, usuario);
			if (!pro.getEmailPrincipal().equals("")) {
				getFacadeFactory().getProspectsFacade().incluirSemValidarDados(pro, false, usuario, null);
			}
		}
		return pro;
	}

	@Override
	public PessoaVO consultarPessoaContaReceber(Integer codigoContaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT pessoa.* FROM pessoa ");
		sql.append("INNER JOIN contareceber ON contareceber.pessoa = pessoa.codigo");
		sql.append(" WHERE contareceber.codigo = " + codigoContaReceber);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		PessoaVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PessoaVO> consultaRapidaAlunoPorDisciplinaTurmaAnoSemestreUnidadeEnsino(Integer disciplina, Integer turma, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT pessoa.codigo AS pessoa_codigo, pessoa.nome AS pessoa_nome");
		sql.append(" FROM matricula ");
		sql.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append(" INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo ");
		sql.append(" INNER JOIN turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append(" INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append(" and Pessoa.codigo = Matricula.aluno ");
		sql.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		sql.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma);
		sql.append(" AND ((matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE')");
		sql.append("  or matriculaperiodoturmadisciplina.modalidadedisciplina != 'ON_LINE')");
		sql.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		sql.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("'");
		sql.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PessoaVO> lista = new ArrayList<PessoaVO>();
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			obj.setNome(tabelaResultado.getString("pessoa_nome"));
			lista.add(obj);
		}
		return lista;
	}

	@Override
	public void consultaRapidaAlunoPorCursoTurmaAnoSemestreSitacaoAcademica(Integer unidadeEnsino, List<PessoaVO> listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,FiltroRelatorioAcademicoVO filtroRelatorioAcademico, String nivelEducacional, String identificadorSalaAulaBlackboard, Double percentualIntegralizacaoInicial, Double percentualIntegralizacaoFinal, ProgramacaoFormaturaVO programacaoFormaturaVO, BimestreEnum bimestreEnum, TipoDeficiencia tipoDeficienciaEnum, DiaSemana diaSemanaEnum, DisciplinaVO disciplinaVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select t.codigo, t.nome, t.email, t.email2, t.celular, t.pessoaemailinstitucional, t.emailInstitucional");
		if(percentualIntegralizacaoInicial != null || percentualIntegralizacaoFinal != null) {
			sqlStr.append(", matriculaintegralizada.percentualIntegralizado as percentualIntegralizado");
		}		
		sqlStr.append(" from (select distinct matricula.matricula, pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2, pessoa.celular, pessoaemailinstitucional.codigo as pessoaemailinstitucional, pessoaemailinstitucional.email as emailInstitucional");
		sqlStr.append(" from pessoa");
		sqlStr.append(" inner join matricula on matricula.aluno = pessoa.codigo");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = ( select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by mp.ano || '/' || mp.semestre desc limit 1)");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		if (Uteis.isAtributoPreenchido(comunicacaoInterna.getTurma().getCodigo()) || Uteis.isAtributoPreenchido(disciplinaVO) || Uteis.isAtributoPreenchido(identificadorSalaAulaBlackboard) || Uteis.isAtributoPreenchido(bimestreEnum)) {
			sqlStr.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" inner join disciplina on disciplina.codigo = mptd.disciplina ");
			sqlStr.append(" left  join gradedisciplina ON gradedisciplina.codigo = mptd.gradedisciplina ");
		}
		if (Uteis.isAtributoPreenchido(comunicacaoInterna.getTurma().getCodigo())) {
			if (comunicacaoInterna.getTurma().getSubturma()) {
				if (comunicacaoInterna.getTurma().getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sqlStr.append(" inner join turma  on mptd.turmaTeorica = turma.codigo ");
				} else if (comunicacaoInterna.getTurma().getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sqlStr.append(" inner join turma on mptd.turmaPratica = turma.codigo  ");
				} else {
					sqlStr.append(" inner join turma on mptd.turma = turma.codigo and mptd.turmapratica is null and mptd.turmateorica is null ");
				}
			} else {
				if (!comunicacaoInterna.getTurma().getTurmaAgrupada()) {
					sqlStr.append(" inner join turma on mptd.turma = turma.codigo and mptd.turmapratica is null and mptd.turmateorica is null ");
				} else {
					sqlStr.append(" inner join turma on (mptd.turma = turma.codigo or mptd.turma in (select turma from turmaAgrupada where turmaOrigem = ").append(comunicacaoInterna.getTurma().getCodigo()).append(")");
					sqlStr.append(" or (mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(comunicacaoInterna.getTurma().getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
					sqlStr.append(" or (mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(comunicacaoInterna.getTurma().getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
					sqlStr.append(") ");
				}
			}
		}
		sqlStr.append(" left join pessoaemailinstitucional on pessoa.codigo = pessoaemailinstitucional.pessoa");
		sqlStr.append(" and pessoaemailinstitucional.statusativoinativoenum = 'ATIVO'");
		sqlStr.append(" and pessoaemailinstitucional.codigo = (select pe.codigo from pessoaemailinstitucional pe where pe.pessoa = pessoa.codigo and pe.statusativoinativoenum = 'ATIVO' order by pe.codigo desc limit 1)");
		if (Uteis.isAtributoPreenchido(programacaoFormaturaVO)) {
			sqlStr.append(" left join programacaoformaturaaluno on matricula.matricula = programacaoformaturaaluno.matricula ");
			sqlStr.append(" left join programacaoformatura on programacaoformatura.codigo = programacaoformaturaaluno.programacaoformatura ");
		}
		sqlStr.append(" where 1=1 ");
		if(Uteis.isAtributoPreenchido(comunicacaoInterna.getAno())){
			sqlStr.append(" and matriculaperiodo.ano = '").append(comunicacaoInterna.getAno()).append("'");
		}
		if (Uteis.isAtributoPreenchido(comunicacaoInterna.getSemestre())) {
			sqlStr.append(" and  matriculaperiodo.semestre = '").append(comunicacaoInterna.getSemestre()).append("'");
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademico, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademico, "matriculaperiodo"));
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(comunicacaoInterna.getCurso().getCodigo())) {
			sqlStr.append(" and matricula.curso = ").append(comunicacaoInterna.getCurso().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(comunicacaoInterna.getTurma().getCodigo())) {
			sqlStr.append(" and turma.codigo = ").append(comunicacaoInterna.getTurma().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(disciplinaVO)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplinaVO.getCodigo());
		}
		if(!comunicacaoInterna.getComunicadoInternoDestinatarioVOs().isEmpty()) {
			sqlStr.append(" and pessoa.codigo not in (0 ");
			comunicacaoInterna.getComunicadoInternoDestinatarioVOs().forEach(p -> sqlStr.append(", ").append(p.getDestinatario().getCodigo()));
			sqlStr.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" and curso.nivelEducacional = '").append(nivelEducacional).append("'");	
		}
		
		if(Uteis.isAtributoPreenchido(programacaoFormaturaVO)) {
			sqlStr.append(" and programacaoformatura.codigo = ").append(programacaoFormaturaVO.getCodigo());
		}
		
		if(Uteis.isAtributoPreenchido(tipoDeficienciaEnum)) {
			sqlStr.append(" and pessoa.deficiencia = '").append(tipoDeficienciaEnum.getValor()).append("'");
		}
		
		if(Uteis.isAtributoPreenchido(diaSemanaEnum)) {
			sqlStr.append(" and matricula.diasemanaaula = '").append(diaSemanaEnum).append("'");
		}
		if(Uteis.isAtributoPreenchido(bimestreEnum)) {
			sqlStr.append(" and gradedisciplina.bimestre = ").append(bimestreEnum.getOrdemApresentar());
		}
		if(Uteis.isAtributoPreenchido(identificadorSalaAulaBlackboard)) {
			sqlStr.append(" and exists (select from salaaulablackboardpessoa sabp inner join salaaulablackboard sab on sabp.salaaulablackboard = sab.codigo where sabp.matricula = matricula.matricula");
			sqlStr.append(" and sab.idsalaaulablackboard = '").append(identificadorSalaAulaBlackboard).append("' )");
		}
		sqlStr.append(" ) t");
		if(percentualIntegralizacaoInicial != null || percentualIntegralizacaoFinal != null) {
			sqlStr.append(" inner join matriculaIntegralizacaoCurricular(t.matricula) as matriculaIntegralizada on percentualIntegralizado is not null");
			
			if(percentualIntegralizacaoInicial != null && percentualIntegralizacaoFinal != null) {
				sqlStr.append(" and percentualIntegralizado between ").append(percentualIntegralizacaoInicial).append(" and ").append(percentualIntegralizacaoFinal);
			} else if(percentualIntegralizacaoInicial != null && percentualIntegralizacaoFinal == null) {
				sqlStr.append(" and percentualIntegralizado >= ").append(percentualIntegralizacaoInicial);
			} else if(percentualIntegralizacaoInicial == null && percentualIntegralizacaoFinal != null) {
				sqlStr.append(" and percentualIntegralizado <= ").append(percentualIntegralizacaoFinal);
			}
		}
		sqlStr.append(" order by t.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Integer qtdDestinatarios = 0;
		while(tabelaResultado.next()) {
			Integer i = 1;
			ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO =  new ComunicadoInternoDestinatarioVO();
			comunicadoInternoDestinatarioVO.getDestinatario().setCodigo(tabelaResultado.getInt("codigo"));
			comunicadoInternoDestinatarioVO.getDestinatario().setNome(tabelaResultado.getString("nome"));
			comunicadoInternoDestinatarioVO.getDestinatario().setEmail(tabelaResultado.getString("email"));
			comunicadoInternoDestinatarioVO.getDestinatario().setEmail2(tabelaResultado.getString("email2"));
			comunicadoInternoDestinatarioVO.getDestinatario().setCelular(tabelaResultado.getString("celular"));
			if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("pessoaemailinstitucional"))) {
				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = new PessoaEmailInstitucionalVO();
				pessoaEmailInstitucionalVO.setCodigo(tabelaResultado.getInt("pessoaemailinstitucional"));
				pessoaEmailInstitucionalVO.setEmail(tabelaResultado.getString("emailInstitucional"));
				comunicadoInternoDestinatarioVO.getDestinatario().getListaPessoaEmailInstitucionalVO().add(pessoaEmailInstitucionalVO);
			}
			comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(comunicacaoInterna.getTipoComunicadoInterno());
			comunicacaoInterna.getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
			comunicadoInternoDestinatario = new ComunicadoInternoDestinatarioVO();
			qtdDestinatarios += i;
		}
		comunicacaoInterna.setTotalizadorDestinatario(qtdDestinatarios);
		if (!Uteis.isAtributoPreenchido(comunicacaoInterna.getComunicadoInternoDestinatarioVOs())) {			
			throw new Exception("Nenhum destinatário foi localizado! Com os filtros utilizados.");
		}
		
//		adicionarPessoaListaDestinatario(listaAluno, comunicacaoInterna, comunicadoInternoDestinatario);
	}

	@Override
	public Boolean realizarVerificacaoPessoaTipoAluno(Integer codigoPessoa) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT pessoa.aluno FROM pessoa WHERE  pessoa.codigo = ").append(codigoPessoa).append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("aluno");
		}
		return false;
	}

	@Override
	public AlunoAutoAtendimentoRSVO consultarAlunoAutoAtendimentoPorCPF(String cpf) throws Exception{
		StringBuilder sql  = new StringBuilder("select matricula.matricula, curso.nome as curso, turno.nome as turno,  pessoa.nome as aluno, pessoa.email, pessoa.celular, ");
		sql.append("pessoa.telefoneres, pessoa.telefonerecado, pessoa.cpf, pessoa.endereco, pessoa.setor, pessoa.numero, pessoa.complemento, pessoa.cep, cidade.nome as cidade, estado.sigla as estado ");
		sql.append(" from pessoa ");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join turno on matricula.turno = turno.codigo ");
		sql.append(" left join cidade on pessoa.cidade = cidade.codigo ");
		sql.append(" left join estado on estado.codigo = cidade.estado ");
		sql.append(" where replace(replace(pessoa.cpf, '.', ''), '-', '') = replace(replace(?, '.', ''), '-', '') ");
		sql.append(" order by matricula.situacao, case curso.niveleducacional when 'SU' then 1 when 'GT' then 2  when 'PO' then 3 when 'ME' then 4 when 'BA' then 5 when 'IN' then 6 else 7 end, matricula.data desc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), cpf);
		AlunoAutoAtendimentoRSVO alunoAutoAtendimentoRSVO = new AlunoAutoAtendimentoRSVO();
		if (rs.next()) {
			alunoAutoAtendimentoRSVO.setMatricula(rs.getString("matricula"));
			alunoAutoAtendimentoRSVO.setCurso(rs.getString("curso"));
			alunoAutoAtendimentoRSVO.setTurno(rs.getString("turno"));
			alunoAutoAtendimentoRSVO.setAluno(rs.getString("aluno"));
			alunoAutoAtendimentoRSVO.setEmail(rs.getString("email"));
			alunoAutoAtendimentoRSVO.setEndereco(rs.getString("endereco"));
			alunoAutoAtendimentoRSVO.setSetor(rs.getString("setor"));
			alunoAutoAtendimentoRSVO.setNumero(rs.getString("numero"));
			alunoAutoAtendimentoRSVO.setCidade(rs.getString("cidade"));
			alunoAutoAtendimentoRSVO.setEstado(rs.getString("estado"));
			alunoAutoAtendimentoRSVO.setTelefoneResidencial(rs.getString("telefoneres"));
			alunoAutoAtendimentoRSVO.setCelular(rs.getString("celular"));
			alunoAutoAtendimentoRSVO.setTelefoneRecado(rs.getString("telefonerecado"));
			alunoAutoAtendimentoRSVO.setCpf(rs.getString("cpf"));
			alunoAutoAtendimentoRSVO.setCep(rs.getString("cep"));
			alunoAutoAtendimentoRSVO.setComplemento(rs.getString("complemento"));
		}
		return alunoAutoAtendimentoRSVO;
	}

	@Override
	public String inicializarDadosFotoUsuario(PessoaVO pessoaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		pessoaVO.getArquivoImagem().setCpfRequerimento(pessoaVO.getCPF());
		String caminhoFotoUsuario = (getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(pessoaVO.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoPastaWeb(), "foto_usuario.png", false));
		return caminhoFotoUsuario;
	}

	@Override
	public List<PessoaVO> consultarFilhosPorResponsavelFinanceiro(Integer responsavelFinanceiro, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select pessoa.codigo, pessoa.nome, pessoa.cpf, pessoa.celular, pessoa.email from pessoa ");
		sb.append(" inner join filiacao on filiacao.aluno = pessoa.codigo ");
		sb.append(" where filiacao.pais = ").append(responsavelFinanceiro);
		sb.append(" order by pessoa.nome ");
		List<PessoaVO> listaFilhosVOs = new ArrayList<PessoaVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setCPF(tabelaResultado.getString("cpf"));
			obj.setCelular(tabelaResultado.getString("celular"));
			obj.setEmail(tabelaResultado.getString("email"));
			listaFilhosVOs.add(obj);
		}
		return listaFilhosVOs;
	}

	/**
	 * Realiza a consulta das pessoas e/ou usuarios que tiveram alteracao em seus registros nas ultimas horas @param
	 * @param horas
	 * @return
	 */
	@Override
	public SqlRowSet consultarPessoasQueSofreramAlteracao(Integer horas) {

		StringBuilder sb = new StringBuilder();
		sb.append("select pessoa.codigo, pessoa.cpf, pessoa.nome, pessoa.email, usuario.senha from pessoa ")
		.append(" inner join usuario on usuario.pessoa = pessoa.codigo  ")
		.append(" where ((pessoa.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)) ")
		.append(" or (usuario.dataultimaalteracao >= (now() - '").append(horas).append(" hour'::interval)))  ")
		.append(" and pessoa.email <> '' ")
		.append(" and pessoa.cpf not ilike '%T%' ")
		.append(" and pessoa.email is not null ")
		.append(" and (pessoa.aluno = true or pessoa.professor = true or pessoa.coordenador ) ")
		.append(" order by pessoa.nome ");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	public void alterarPessoasSetandoDataAlteracaoInicial() {
		boolean repetir = true;
		while (repetir) {
			try {
				repetir = false;
				StringBuilder sb = new StringBuilder();
				sb.append("select pessoa.codigo from pessoa ")
				.append(" inner join usuario on usuario.pessoa = pessoa.codigo  ")
				.append(" where pessoa.dataultimaalteracao < current_date ")
				.append(" order by pessoa.codigo desc limit 1000 ");

				String codigosAlterar = "";
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
				while (tabelaResultado.next()) {
					repetir = true;
					codigosAlterar += tabelaResultado.getInt("codigo") + ", ";
				}
				if (repetir) {
					alterarPessoasSetandoDataAlteracaoInicial(codigosAlterar);
					Thread.sleep(300000);
				}
			} catch (Exception e) {
				repetir = false;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarPessoasSetandoDataAlteracaoInicial(String codigosAlterar) {
		String sql = "UPDATE Pessoa set dataultimaalteracao=now() where codigo in (" + codigosAlterar + " 0);";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				return sqlAlterar;

			}
		});
	}

	/**
	 * Altera as informacoes da Pessoa pela teste de Follow Up - Prospect
	 */
	public void alterarPessoaPelaTelaDeFollowUpProspect(ProspectsVO obj, UsuarioVO usuarioVO) {
		final String sqls = "UPDATE Pessoa set nome=?, email=?, dataNasc=?, sexo=?, telefoneRes=?, celular=?, telefoneComer=?, telefoneRecado=?, reponsavelUltimaAlteracao = ?, dataultimaalteracao = ?, nomeBatismo = ?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqls);
				sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
				sqlAlterar.setString(2, obj.getEmailPrincipal().trim());
				sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataNascimento()));
				if (!SexoEnum.NENHUM.name().equals(obj.getSexo())) {
					sqlAlterar.setString(4, obj.getSexo());
				} else {
					sqlAlterar.setNull(4, 0);
				}
				sqlAlterar.setString(5, obj.getTelefoneResidencial());
				sqlAlterar.setString(6, obj.getCelular());
				sqlAlterar.setString(7, obj.getTelefoneComercial());
				sqlAlterar.setString(8, obj.getTelefoneRecado());
				sqlAlterar.setInt(9, usuarioVO.getCodigo());
				sqlAlterar.setTimestamp(10, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setString(11, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
				sqlAlterar.setInt(12, obj.getPessoa().getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	public PessoaVO consultarPorMatriculaUnicaTipoPessoa(String matricula, Boolean aluno, Boolean professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT pessoa.* FROM Pessoa ");
		if (aluno != null && aluno) {
			sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
			sqlStr.append(" WHERE matricula.matricula = ? ");
			sqlStr.append(" AND Pessoa.aluno = ").append(aluno);
			if (aluno && unidadeEnsino != 0) {
				sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino);
			}
		} else if (professor != null && professor) {
			sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
			sqlStr.append(" INNER JOIN funcionariocargo ON funcionariocargo.funcionario = funcionario.codigo");
			sqlStr.append(" WHERE funcionario.matricula = ? ");
			sqlStr.append(" AND Pessoa.professor = ").append(professor);
			if (professor && unidadeEnsino != 0) {
				sqlStr.append(" AND funcionariocargo.unidadeensino = ").append(unidadeEnsino);
			}
		}
		sqlStr.append(" ORDER BY pessoa.nome LIMIT 1;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		PessoaVO pessoaVO = null;
		if(tabelaResultado.next()) {
			pessoaVO = montarDados(tabelaResultado, pessoaVO, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
		} else {
			throw new ConsistirException("Dados não encontrados para a matrícula informada.");
		}
		return pessoaVO;
	}

	@Override
	public Boolean consultarPessoaPossuiMatriculaPorCodigo(Integer codigo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select pessoa.aluno from pessoa ");
		sb.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sb.append(" where pessoa.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), codigo);
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("aluno");
		}
		return false;
	}

	@Override
	public void inicializarDadosPessoaBaseadoProspect(ProspectsVO obj, UsuarioVO usuarioVO) {
		PessoaVO pessoaVO = obj.getPessoa();
		pessoaVO.setNome(Uteis.removeCaractersEspeciais2(obj.getNome()));
		pessoaVO.setNomeBatismo(Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
		pessoaVO.setEmail(obj.getEmailPrincipal().trim());
		pessoaVO.setDataNasc(Uteis.getDataJDBC(obj.getDataNascimento()));
		pessoaVO.setSexo(obj.getSexo());
		pessoaVO.setTelefoneRes(obj.getTelefoneResidencial());
		pessoaVO.setCelular(obj.getCelular());
		pessoaVO.setTelefoneComer(obj.getTelefoneComercial());
		pessoaVO.setTelefoneRecado(obj.getTelefoneRecado());
		pessoaVO.setDataUltimaAlteracao(Uteis.getDataJDBC(new Date()));
	}

	@Override
	public void validarDadosPessoaCasoPossuaMatricula(PessoaVO obj, Boolean origemPreInscricao, UsuarioVO usuario) throws Exception {
		Boolean possuiMatricula = getFacadeFactory().getPessoaFacade().consultarPessoaPossuiMatriculaPorCodigo(obj.getCodigo(), usuario);
		if (possuiMatricula) {
			if (origemPreInscricao != null && origemPreInscricao) {
				if (obj.getNome().trim().equals("")) {
		            throw new ConsistirException("O campo NOME (Dados Pessoais) deve ser informado.");
		        }
			} else {
				if (obj.getNome().trim().equals("")) {
		            throw new ConsistirException("O campo NOME (Dados Pessoais) deve ser informado.");
		        }
				if (obj.getDataNasc() == null) {
		            throw new ConsistirException("O campo DATA NASCIMENTO (Dados Pessoais) deve ser informado.");
		        }
				if(Uteis.isAtributoPreenchido(obj.getDataNasc()) && (String.valueOf(Uteis.getAnoData(obj.getDataNasc())).length() != 4)) {
		        	throw new ConsistirException("O campo DATA NASCIMENTO (Dados Pessoais) foi informado com valor errado.");
		        }
				if (obj.getTelefoneComer().length() > 15) {
		        	throw new ConsistirException("O campo TEL.COMERCIAL não pode ter mais do que 15 caracteres.");
		        }
		        if (obj.getTelefoneRes().length() > 15) {
		        	throw new ConsistirException("O campo TEL.RESIDENCIAL não pode ter mais do que 15 caracteres.");
		        }
		        if (obj.getTelefoneRecado().length() > 15) {
		        	throw new ConsistirException("O campo TEL.RECADO não pode ter mais do que 15 caracteres.");
		        }
			}
		}
	}

	@Override
	public List<PessoaVO> consultaCoordenadorPorNome(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append( "SELECT distinct pessoa.* FROM Pessoa");
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" inner join cursoCoordenador on cursoCoordenador.funcionario = funcionario.codigo ");
		sqlStr.append(" WHERE pessoa.coordenador");
		sqlStr.append(" and (sem_acentos(pessoa.nome)) ILIKE (upper(sem_acentos('%"+valorConsulta.toLowerCase()+"%'))) ");
		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlStr.append("AND cursoCoordenador.unidadeEnsino IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlStr.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<PessoaVO> consultarAprovadorLiberacaoMatricula(MatriculaPeriodoVO matriculaPeriodoVO, Boolean solicitarLiberacaoFinanceira, Boolean solicitarLiberacaoMatriculaAposInicioXModulos, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
		sb.append("select distinct pessoa.codigo, pessoa.nome, pessoa.email from usuario ");
		sb.append("inner join pessoa on pessoa.codigo = usuario.pessoa ");
		sb.append("inner join funcionario on pessoa.codigo = funcionario.pessoa ");
		sb.append("where pessoa.ativo and pessoa.email is not null ");
		sb.append("and pessoa.email ilike ('%@%.%') ");
		sb.append("and exists (");
		sb.append("select usuarioperfilacesso.codigo from usuarioperfilacesso ");
		sb.append("inner join perfilacesso on perfilacesso.codigo = usuarioperfilacesso.perfilacesso ");
		sb.append("inner join permissao on permissao.codperfilacesso = perfilacesso.codigo ");
		sb.append("where (unidadeensino = ").append(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino()).append(" or 	unidadeensino is null) ");
		sb.append("and usuarioperfilacesso.usuario = usuario.codigo ");
		sb.append("and permissao.nomeentidade = 'MapaSuspensaoMatricula' ");
		sb.append(") ");
		sb.append("and exists ( ");
		sb.append("select usuarioperfilacesso.codigo from usuarioperfilacesso ");
		sb.append("inner join perfilacesso on perfilacesso.codigo = usuarioperfilacesso.perfilacesso ");
		sb.append("inner join permissao on permissao.codperfilacesso = perfilacesso.codigo ");
		sb.append("where (unidadeensino = ").append(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino()).append(" or unidadeensino is null) ");
		sb.append("and usuarioperfilacesso.usuario = usuario.codigo ");
		if(solicitarLiberacaoFinanceira && solicitarLiberacaoMatriculaAposInicioXModulos) {
			sb.append("and permissao.nomeentidade = 'MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira' or permissao.nomeentidade = 'MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica'");
		}
		else if(solicitarLiberacaoFinanceira) {
			sb.append("and permissao.nomeentidade = 'MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira' ");
		}
		else if(solicitarLiberacaoMatriculaAposInicioXModulos) {
			sb.append("and permissao.nomeentidade = 'MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica' ");
		}
		sb.append(") ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	    List<PessoaVO> listDestinatario = new ArrayList<PessoaVO>();
	    while (tabelaResultado.next()) {
			PessoaVO pessoa = new PessoaVO();
			pessoa.setCodigo(tabelaResultado.getInt("codigo"));
			pessoa.setNome(tabelaResultado.getString("nome"));
			pessoa.setEmail(tabelaResultado.getString("email"));
			listDestinatario.add(pessoa);
        }
		return listDestinatario;
	}

	@Override
	public boolean consultarSePessoaAtiva(Integer pessoa) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select ")
			.append("(select (ativo and (professor or coordenador or funcionario)) from pessoa where codigo = ?) ")
			.append("or ")
			.append("(select coalesce(bool_or(situacao = 'AT'), false) from matricula where aluno = ?) ativo");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {pessoa, pessoa});
		if (dadosSQL.next()) {
			return dadosSQL.getBoolean("ativo");
		}
		return false;
	}

	public static void montarDadosTipoMidiaCaptacao(PessoaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTipoMidiaCaptacao().getCodigo().intValue() == 0) {
			obj.setTipoMidiaCaptacao(new TipoMidiaCaptacaoVO());
			return;
		}
		obj.setTipoMidiaCaptacao(getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorChavePrimaria(obj.getTipoMidiaCaptacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	public void validarDadosAlunoUnificar(String campoUnificar, String valorUnificar, UsuarioVO usuarioVO) throws Exception {
		if (campoUnificar.equals("NOME")) {
			if (valorUnificar.equals("")) {
				throw new Exception("O campo NOME deve ser informado!");
			}
		}
		if (campoUnificar.equals("MATRICULA")) {
			if (valorUnificar.equals("")) {
				throw new Exception("O campo MATRÍCULA deve ser informado!");
			}
		}
		if (campoUnificar.equals("CPF")) {
			if (valorUnificar.equals("")) {
				throw new Exception("O campo CPF deve ser informado!");
			}
		}
	}
	
	@Override
	public List<PessoaVO> consultarPessoaUnificacao(PessoaVO pessoaManterVO, String campoUnificar, String valorUnificar, Boolean unificarMesmoNome, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(pessoaManterVO.getCodigo())) {
			throw new Exception("O campo PESSOA PERMANECER deve ser informado!");
		}
		validarDadosAlunoUnificar(campoUnificar, valorUnificar, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("select DISTINCT pessoa.codigo, pessoa.nome, pessoa.cpf,  pessoa.aluno, pessoa.funcionario, ");
		sb.append("(select count(distinct contareceber.codigo) from contareceber where contareceber.pessoa = pessoa.codigo ) as qtdeContaPessoa, ");
		sb.append("(select count(distinct contareceber.codigo) from contareceber where contareceber.responsavelfinanceiro = pessoa.codigo) as qtdeContaResponsavelFinanceiro ");
		sb.append(" from pessoa ");
		sb.append(" left join matricula on matricula.aluno = pessoa.codigo ");
		sb.append(" left join funcionario on funcionario.pessoa = pessoa.codigo ");
		sb.append(" where (pessoa.cpf) <> '").append(pessoaManterVO.getCPF().trim()).append("' and pessoa.codigo != ").append(pessoaManterVO.getCodigo());

		if (campoUnificar.equals("NOME")) {
			if (unificarMesmoNome) {
				sb.append(" and upper(sem_acentos('"+valorUnificar.toLowerCase()+"')) like (upper(sem_acentos('"+pessoaManterVO.getNome().toLowerCase()+"'))) ");
			} else {
				sb.append(" and (sem_acentos(pessoa.nome)) ILIKE upper(sem_acentos('"+valorUnificar +"%')) ");
			}
			
		}
		if (campoUnificar.equals("CPF")) {
			sb.append(" and pessoa.cpf like '").append(valorUnificar).append("%' ");
		}
		if (campoUnificar.equals("MATRICULA")) {
			sb.append(" and matricula.matricula = '").append(valorUnificar).append("' ");
		}
		sb.append("order by pessoa.nome, pessoa.cpf ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		List<PessoaVO> listaPessoaUnificarVOs = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setCPF(tabelaResultado.getString("cpf"));
			obj.setAluno(tabelaResultado.getBoolean("aluno"));
			obj.setFuncionario(tabelaResultado.getBoolean("funcionario"));
			obj.setQuantidadeContaReceberPessoaComoAluno(tabelaResultado.getInt("qtdeContaPessoa"));
			obj.setQuantidadeContaReceberPessoaComoResponsavelFinanceiro(tabelaResultado.getInt("qtdeContaResponsavelFinanceiro"));
			listaPessoaUnificarVOs.add(obj);
		}
		return listaPessoaUnificarVOs;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarUnificacaoPessoa(List<PessoaVO> listaPessoaUnificarVOs, PessoaVO pessoaVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(pessoaVO.getCodigo())) {
			throw new Exception("O campo PESSOA PERMANECER deve ser informado!");
		}
		if (listaPessoaUnificarVOs.isEmpty()) {
			throw new Exception("Não foi encontrado Pessoa para Unificação, realize a consulta novamente.");
		}
		for (PessoaVO pessoaUnificarVO : listaPessoaUnificarVOs) {
			if (pessoaUnificarVO.getSelecionado()) {
				StringBuilder sb = new StringBuilder();
				sb.append("select fn_removerduplicidadepessoa('").append(pessoaUnificarVO.getCPF()).append("','").append(pessoaVO.getCPF()).append("' )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			}
		}
	}
	
	@Override
	public void consultarInformacoesBasicasPessoaUnificar(PessoaVO pessoaVO, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select pessoa.aluno, pessoa.funcionario,  ");
		sb.append("(select count(distinct contareceber.codigo) from contareceber where contareceber.pessoa = pessoa.codigo ) as qtdeContaPessoa, ");
		sb.append("(select count(distinct contareceber.codigo) from contareceber where contareceber.responsavelfinanceiro = pessoa.codigo) as qtdeContaResponsavelFinanceiro ");
		sb.append(" from pessoa ");
		sb.append(" where pessoa.codigo = ").append(pessoaVO.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			pessoaVO.setQuantidadeContaReceberPessoaComoAluno(tabelaResultado.getInt("qtdeContaPessoa"));
			pessoaVO.setQuantidadeContaReceberPessoaComoResponsavelFinanceiro(tabelaResultado.getInt("qtdeContaResponsavelFinanceiro"));
			pessoaVO.setAluno(tabelaResultado.getBoolean("aluno"));
			pessoaVO.setFuncionario(tabelaResultado.getBoolean("funcionario"));
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarDadosBasicosFiliacao(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean deveValidarCPF) throws Exception {
		try {
		Pessoa.alterar(getIdEntidade(), verificarAcesso, usuario);
		boolean validarCPF = false;
		if (deveValidarCPF) {
			validarCPF = getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, usuario);
		}
		validarCPFUnico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (!obj.getPossuiFilho()) {
			obj.setQtdFilhos(0);
		}
		if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
			obj.setNome(obj.getNome().toUpperCase());
		}
		final StringBuilder sql = new StringBuilder(); 
		sql.append("UPDATE Pessoa set nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, cidade=?, estadoCivil=?, telefoneComer=?,"); //9
		sql.append(" telefoneRes=?, telefoneRecado=?, celular=?, email=?, dataNasc=?, nacionalidade=?, CPF=?, RG=?, orgaoEmissor=?,"); //18
		sql.append(" possuiAcessoVisaoPais=?, grauParentesco=?, nomeBatismo=? "); //21
		sql.append(" WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setString(1, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNome()));
				sqlAlterar.setString(2, obj.getEndereco());
				sqlAlterar.setString(3, obj.getSetor());
				sqlAlterar.setString(4, obj.getNumero());
				if(Uteis.validarMascaraCEP(obj.getCEP())) {
					sqlAlterar.setString(5, obj.getCEP());
				} else {
					sqlAlterar.setString(5, Uteis.formataMascaraCEP(obj.getCEP()));
				}
				sqlAlterar.setString(6, obj.getComplemento());
				if (obj.getCidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(7, 0);
				}
				sqlAlterar.setString(8, obj.getEstadoCivil());
				sqlAlterar.setString(9, obj.getTelefoneComer());
				sqlAlterar.setString(10, obj.getTelefoneRes());
				sqlAlterar.setString(11, obj.getTelefoneRecado());
				sqlAlterar.setString(12, obj.getCelular());
				sqlAlterar.setString(13, obj.getEmail().trim());
				sqlAlterar.setDate(14, Uteis.getDataJDBC(obj.getDataNasc()));
				if (obj.getNacionalidade().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(15, obj.getNacionalidade().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(15, 0);
				}
				sqlAlterar.setString(16, obj.getCPF());
				sqlAlterar.setString(17, obj.getRG());
				sqlAlterar.setString(18, obj.getOrgaoEmissor());
				sqlAlterar.setBoolean(19, obj.getPossuiAcessoVisaoPais());
				sqlAlterar.setString(20, obj.getGrauParentesco());
				if(Uteis.isAtributoPreenchido(obj.getNomeBatismo())) {
					sqlAlterar.setString(21, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNomeBatismo()));
				}else {
					sqlAlterar.setString(21, Uteis.removeCaractersEspeciaisParaNomePessoa(obj.getNome()));
				}
				
				sqlAlterar.setInt(22, obj.getCodigo().intValue());

				return sqlAlterar;
			}
		}) <= 0) {
			return;
		}
		}catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().toLowerCase().contains(MensagensRetornoErroEnum.TG_PESSOA_UNICIDADE_EMAIL.getConstraint())) {								
				throw new Exception("A pessoa "+obj.getNome()+" que tem o e-mail "+obj.getEmail()+" está em duplicidade com outra pessoa no sistema. Verifique se o e-mail desta pessoa não está igual o da sua filiação ou de outra pessoa no sistema.");
			}
			throw e;
		}
	}

	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirMembroComunidade(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {
		incluir(obj, verificarAcesso, usuario, configuracaoGeralSistema, editadoPorAluno, true, false, false, false);
	}
	
	public PessoaVO consultaRapidaPorCodigoPessoaCenso(Integer codPessoa, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select pessoa.codigo AS \"pessoa.codigo\", pessoa.nome, pessoa.cpf, ");
		sb.append(" pessoa.dataNasc, pessoa.sexo, pessoa.corRaca, pessoa.nacionalidade, paiz.nome AS \"paiz.nome\", estado.sigla AS UFNascimento, naturalidade.codigoibge, pessoa.email AS \"pessoa.email\"  ");
		sb.append("  from pessoa  ");
		sb.append(" inner join paiz on paiz.codigo = pessoa.nacionalidade ");
		sb.append(" left join cidade naturalidade on naturalidade.codigo = pessoa.naturalidade ");
		sb.append(" left join estado on estado.codigo = naturalidade.estado ");
		sb.append(" where pessoa.codigo = ").append(codPessoa);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		PessoaVO obj = new PessoaVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setCPF(tabelaResultado.getString("cpf"));
			obj.setSexo(tabelaResultado.getString("sexo"));
			obj.setCorRaca(tabelaResultado.getString("corRaca"));
			obj.setDataNasc(tabelaResultado.getDate("dataNasc"));
			obj.getNacionalidade().setCodigo(tabelaResultado.getInt("nacionalidade"));
			obj.getNacionalidade().setNome(tabelaResultado.getString("paiz.nome"));
			obj.getNaturalidade().getEstado().setSigla(tabelaResultado.getString("UFNascimento"));
			obj.getNaturalidade().setCodigoIBGE(tabelaResultado.getString("codigoibge"));
			obj.setEmail(tabelaResultado.getString("pessoa.email"));
			obj.setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarPorCodigoPessoaTipo(obj.getCodigo(), "", false, usuario));
			return obj;
		}
		return obj;
	}
	
	@Override
	public void carregarDadosPessoaProspect(PessoaVO obj , UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
		montarDadosPessoaProspect((PessoaVO) obj, resultado);
	}
	
	public void montarDadosPessoaProspect(PessoaVO obj, SqlRowSet dadosSQL) throws Exception {
		
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.getNomeOriginal();
		obj.setCPF(dadosSQL.getString("cpf"));
		obj.setRG(dadosSQL.getString("rg"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.getEmailOriginal();
		obj.setEmail2(dadosSQL.getString("email2"));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setCEP(dadosSQL.getString("cep"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setEstadoCivil(dadosSQL.getString("estadoCivil"));
		obj.setDataNasc(dadosSQL.getDate("dataNasc"));
		obj.getNaturalidade().setCodigo(dadosSQL.getInt("naturalidade"));
		obj.getNacionalidade().setCodigo(dadosSQL.getInt("nacionalidade"));
		obj.setDataEmissaoRG(dadosSQL.getDate("dataEmissaoRG"));
		obj.setOrgaoEmissor(dadosSQL.getString("orgaoEmissor"));
		obj.getCidade().setCodigo(dadosSQL.getInt("cidadeCodigo"));
		obj.getCidade().setNome(dadosSQL.getString("cidadeNome"));
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("estadoCodigo"));
		obj.getCidade().getEstado().setNome(dadosSQL.getString("estadoNome"));
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.setRegistroAcademico(dadosSQL.getString("registroAcademico"));
		
	}
	
	public void carregarUrlFotoAluno(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PessoaVO pessoaVO) {
		if (configuracaoGeralSistemaVO != null) {
			pessoaVO.getArquivoImagem().setCpfRequerimento(pessoaVO.getCPF());
			pessoaVO.setUrlFotoAluno(getFacadeFactory().getArquivoHelper()
					.renderizarFotoUsuario(pessoaVO.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), configuracaoGeralSistemaVO, getCaminhoPastaWeb(), "foto_usuario.png", false));
		} else {
			pessoaVO.setUrlFotoAluno(getCaminhoPastaWeb() + "/" + "resources" + "/" + "imagens" + "/" + "foto_usuario.png");
		}
	}
	
	@Override
	public void consultaAlunosNaoRenovaramMatriculaUltimoSemestre(PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, Integer unidadeEnsino, Integer curso, Integer turma, Boolean desconsiderarAlunoPreMatriculado, List listaAluno, ComunicacaoInternaVO comunicacaoInterna, ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario, String nivelEducacional, String identificadorSalaAulaBlackboard, Double percentualIntegralizacaoInicial, Double percentualIntegralizacaoFinal, ProgramacaoFormaturaVO programacaoFormaturaVO, BimestreEnum bimestreEnum, TipoDeficiencia tipoDeficienciaEnum, DiaSemana diaSemanaEnum, DisciplinaVO disciplinaVO) throws SQLException, Exception {
		StringBuilder sqlStr = new StringBuilder("");
		String semestreAnterior = "";
		String anoAnterior = "";
		Boolean filtrarPercentualIntegralizacao = percentualIntegralizacaoInicial != null || percentualIntegralizacaoFinal != null;
		if (semestre.equals("1")) {
			semestreAnterior = "2";
			anoAnterior = String.valueOf(Integer.parseInt(ano) - 1);
		} else if (semestre.equals("2")) {
			semestreAnterior = "1";
			anoAnterior = ano;
		} else {
			anoAnterior = String.valueOf(Integer.parseInt(ano) - 1);
		}
		sqlStr.append("SELECT distinct pes.codigo,pes.email, pes.email2, pes.nome,  pes.cpf, pes.rg, pes.certidaoNascimento, pes.participabancocurriculum, pes.informacoesverdadeiras, pes.informacoesAdicionais, pes.divulgarmeusdados, pes.telefoneRes, pes.celular, pes.email, pes.email2, pes.dataUltimaAlteracao,  pes.informacoesverdadeiras, pes.divulgarmeusdados, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, pes.funcionario, pes.coordenador, pes.professor,  pes.nomeBatismo, ");
		sqlStr.append(" pes.aluno as pessoa_aluno, pes.candidato, pes.membrocomunidade, pes.possuiAcessoVisaoPais, pes.requisitante, ");
		sqlStr.append(" pes.cep, pes.endereco, pes.complemento, pes.setor, pes.numero, cidade.codigo AS cidadeCodigo, cidade.nome AS cidadeNome, estado.codigo AS estadoCodigo, estado.nome AS estadoNome, estado.sigla as estadoSigla, pes.contacorrente, pes.agencia, pes.banco  , pes.registroAcademico, pes.sabatista, pes.tipoAssinaturaDocumentoEnum ");
		if(filtrarPercentualIntegralizacao) {
			sqlStr.append(" , matriculaintegralizada.percentualIntegralizado as percentualIntegralizado");
		}
		sqlStr.append(" FROM matricula mat1	");
		sqlStr.append(" INNER JOIN matriculaperiodo matrip ON matrip.matricula = mat1.matricula	");
		if(filtrarPercentualIntegralizacao) {
			sqlStr.append(" INNER JOIN matriculaIntegralizacaoCurricular(mat1.matricula) as matriculaIntegralizada ON percentualIntegralizado is not null ");
		}		
		if(Uteis.isAtributoPreenchido(disciplinaVO) || Uteis.isAtributoPreenchido(identificadorSalaAulaBlackboard) || Uteis.isAtributoPreenchido(bimestreEnum)) {
			sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matrip.codigo ");
			sqlStr.append("INNER JOIN disciplina on disciplina.codigo = mptd.disciplina "); 
			sqlStr.append("LEFT  JOIN gradedisciplina ON gradedisciplina.codigo = mptd.gradedisciplina ");
		}
		sqlStr.append(" INNER JOIN pessoa pes ON pes.codigo = mat1.aluno ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = mat1.curso ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matrip.turma ");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = turma.turno ");
		sqlStr.append(" LEFT JOIN cidade on pes.cidade = cidade.codigo ");
		sqlStr.append(" LEFT JOIN estado on estado.codigo = cidade.estado ");
		sqlStr.append(" LEFT JOIN Arquivo on arquivo.codigo = pes.arquivoimagem ");
		if(Uteis.isAtributoPreenchido(programacaoFormaturaVO)) {
			sqlStr.append(" LEFT JOIN programacaoformaturaaluno on mat1.matricula = programacaoformaturaaluno.matricula ");
			sqlStr.append(" LEFT JOIN programacaoformatura on programacaoformatura.codigo = programacaoformaturaaluno.programacaoformatura ");
		}
		sqlStr.append(" WHERE ");
		sqlStr.append(" matrip.situacaoMatriculaPeriodo in ('FI', 'AT') ");
		if(periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)){
			sqlStr.append(" AND curso.periodicidade  = 'AN' and matrip.ano = '").append(anoAnterior).append("'");
		}else{
			sqlStr.append(" and curso.periodicidade = 'SE' and matrip.ano = '").append(anoAnterior).append("'").append(" AND matrip.semestre = '").append(semestreAnterior).append("'");
		}		
		sqlStr.append(" AND not exists ( SELECT mtp2.matricula FROM matriculaperiodo mtp2 ");
		sqlStr.append("  WHERE  (mtp2.ano||'/'||mtp2.semestre) >  (matrip.ano||'/'||matrip.semestre)  ");
		if (!desconsiderarAlunoPreMatriculado) {
			sqlStr.append(" and mtp2.situacaoMatriculaPeriodo not in ('PC', 'PR')  ");
		}
		sqlStr.append(" AND matrip.matricula = mtp2.matricula limit 1 )  ");
		if(Uteis.isAtributoPreenchido(curso)){
			sqlStr.append(" AND curso.codigo = ").append(curso).append(" ");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND turma.codigo = ").append(turma).append(" ");
		}
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND mat1.unidadeensino = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("'");
		}
		if(Uteis.isAtributoPreenchido(disciplinaVO)) {
			sqlStr.append(" AND disciplina.codigo = ").append(disciplinaVO.getCodigo());
		}
		if(Uteis.isAtributoPreenchido(programacaoFormaturaVO)) {
			sqlStr.append(" and programacaoformatura.codigo = ").append(programacaoFormaturaVO.getCodigo());
		}		
		if(Uteis.isAtributoPreenchido(tipoDeficienciaEnum)) {
			sqlStr.append(" and pes.deficiencia = '").append(tipoDeficienciaEnum.getValor()).append("'");
		}		
		if(Uteis.isAtributoPreenchido(diaSemanaEnum)) {
			sqlStr.append(" and mat1.diasemanaaula = '").append(diaSemanaEnum).append("'");
		}
		if(Uteis.isAtributoPreenchido(bimestreEnum)) {
			sqlStr.append(" and gradedisciplina.bimestre = ").append(bimestreEnum.getOrdemApresentar());
		}
		if(Uteis.isAtributoPreenchido(identificadorSalaAulaBlackboard)) {
			sqlStr.append(" and exists (select from salaaulablackboardpessoa sabp inner join salaaulablackboard sab on sabp.salaaulablackboard = sab.codigo where sabp.matricula = mat1.matricula");
			sqlStr.append(" and sab.idsalaaulablackboard = '").append(identificadorSalaAulaBlackboard).append("' )");
			
		}
		if(filtrarPercentualIntegralizacao) {			
			if(percentualIntegralizacaoInicial != null && percentualIntegralizacaoFinal != null) {
				sqlStr.append(" and percentualIntegralizado between ").append(percentualIntegralizacaoInicial).append(" and ").append(percentualIntegralizacaoFinal);
			} else if(percentualIntegralizacaoInicial != null && percentualIntegralizacaoFinal == null) {
				sqlStr.append(" and percentualIntegralizado >= ").append(percentualIntegralizacaoInicial);
			} else if(percentualIntegralizacaoInicial == null && percentualIntegralizacaoFinal != null) {
				sqlStr.append(" and percentualIntegralizado <= ").append(percentualIntegralizacaoFinal);
			}
		}
		sqlStr.append(" GROUP BY pes.codigo, arquivo.codigo, cidade.codigo,	estado.codigo, turma.identificadorturma, turno.nome,curso.nome,pes.nome, mat1.matricula, pes.email, pes.celular, pes.telefoneres, matrip.situacaomatriculaperiodo ");
		if(filtrarPercentualIntegralizacao) {
			sqlStr.append(" ,matriculaintegralizada.percentualIntegralizado");
		}
		try {
			SqlRowSet tabelaResultados = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			listaAluno = montarDadosConsultaRapida(tabelaResultados);
			adicionarPessoaListaDestinatario(listaAluno, comunicacaoInterna, comunicadoInternoDestinatario);
			if (!Uteis.isAtributoPreenchido(comunicacaoInterna.getComunicadoInternoDestinatarioVOs())) {			
				throw new Exception("Nenhum destinatário foi localizado! Com os filtros utilizados.");
			} 
		} finally {
			sqlStr = null;
			semestreAnterior = null;
		}
	}
	
	
	
	
	@Override
	public List<PessoaVO> consultarCoordenadoresCursoTurmaNotificacaoCronogramaAula(Integer turma , UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct	coordenador.codigo , coordenador.nome , coordenador.email , coordenador.email2   from turma ");
		sb.append("inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) ");
		sb.append(" or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo) )) ");
		sb.append("inner join cursocoordenador on (cursocoordenador.turma is null and cursocoordenador.curso = curso.codigo) or ");
		sb.append("(cursocoordenador.turma is not null and ( ");
		sb.append("(turma.turmaagrupada = false and cursocoordenador.turma = turma.codigo) ");
		sb.append("or (turma.turmaagrupada = false and turma.subturma and turma.turmaprincipal = cursocoordenador.turma) ");
		sb.append("or (turma.turmaagrupada and cursocoordenador.turma in (select t.codigo from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo)))) ");
		sb.append("inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sb.append("inner join pessoa as coordenador on funcionario.pessoa = coordenador.codigo ");
		sb.append("where turma.codigo =  ? ");
		sb.append(" order by coordenador.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), turma);
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);		
		while (tabelaResultado.next()) {
		    PessoaVO obj = new PessoaVO();		
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setEmail(tabelaResultado.getString("email"));
			obj.setEmail2(tabelaResultado.getString("email2"));							
		    vetResultado.add(obj);
		}
		return vetResultado;
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PessoaVO inicializarDadosCandidatoImportacaoCandidatoInscricaoProcessoSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO) throws Exception {
		
		PessoaVO candidatoVO = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(importarCandidatoVO.getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);				
		FormacaoAcademicaVO formacao = new FormacaoAcademicaVO();
		CidadeVO cidade = getFacadeFactory().getCidadeFacade().consultaCidadeRapidaPorNome(importarCandidatoVO.getFormacaoAcademicaCidade(), false, usuarioVO);
		List<FormacaoAcademicaVO> listaFormacaoAcademicaVOs = new ArrayList<FormacaoAcademicaVO>(0);
		
		if (Uteis.isAtributoPreenchido(CorRaca.getEnum(importarCandidatoVO.getCorRaca()) == null ? "" : CorRaca.getEnum(importarCandidatoVO.getCorRaca().trim().toUpperCase()).getValor())) {
			candidatoVO.setCorRaca(CorRaca.getEnum(importarCandidatoVO.getCorRaca()) == null ? "" : CorRaca.getEnum(importarCandidatoVO.getCorRaca().trim().toUpperCase()).getValor());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getTituloEleitoral())) {
			candidatoVO.setTituloEleitoral(importarCandidatoVO.getTituloEleitoral());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getOrgaoEmissor())) {
			candidatoVO.setOrgaoEmissor(importarCandidatoVO.getOrgaoEmissor());
		}
		if (importarCandidatoVO.getEstadoEmissaoRG().trim().length() == 2) {
			candidatoVO.setEstadoEmissaoRG(importarCandidatoVO.getEstadoEmissaoRG().trim());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getDataEmissaoRG())) {
			candidatoVO.setDataEmissaoRG(importarCandidatoVO.getDataEmissaoRG());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getCertificadoMilitar())) {
			candidatoVO.setCertificadoMilitar(importarCandidatoVO.getCertificadoMilitar());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getRG())) {
			candidatoVO.setRG(importarCandidatoVO.getRG());
		}
		if(!Uteis.isAtributoPreenchido(candidatoVO)) {
			candidatoVO.setCPF(importarCandidatoVO.getCPF());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getNacionalidadeOriginal().getCodigo())) {
			candidatoVO.setNacionalidade(importarCandidatoVO.getNacionalidadeOriginal());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getNaturalidadeOriginal().getCodigo())) {
			candidatoVO.setNaturalidade(importarCandidatoVO.getNaturalidadeOriginal());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getCidadeOriginal().getCodigo())) {
			candidatoVO.setCidade(importarCandidatoVO.getCidadeOriginal());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getDataNasc())) {
			candidatoVO.setDataNasc(importarCandidatoVO.getDataNasc());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getEmail())) {
			candidatoVO.setEmail(importarCandidatoVO.getEmail().trim());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getCelular())) {
			candidatoVO.setCelular(importarCandidatoVO.getCelular());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getTelefoneRecado())) {
			candidatoVO.setTelefoneRecado(importarCandidatoVO.getTelefoneRecado());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getTelefoneRes())) {
			candidatoVO.setTelefoneRes(importarCandidatoVO.getTelefoneRes());
		}
		if (importarCandidatoVO.getEstadoCivil().equals("S") || importarCandidatoVO.getEstadoCivil().equals("C") || importarCandidatoVO.getEstadoCivil().equals("V")
				|| importarCandidatoVO.getEstadoCivil().equals("D") || importarCandidatoVO.getEstadoCivil().equals("A")
				|| importarCandidatoVO.getEstadoCivil().equals("U") || importarCandidatoVO.getEstadoCivil().equals("E")
				|| importarCandidatoVO.getEstadoCivil().equals("Q")
				) {
			candidatoVO.setEstadoCivil(importarCandidatoVO.getEstadoCivil());
		}
		
		if (SexoEnum.getExisteValor(importarCandidatoVO.getSexo())) {
			candidatoVO.setSexo(importarCandidatoVO.getSexo());
		}
		
		if (TipoDeficiencia.getExisteValor(importarCandidatoVO.getDeficiencia())) {
			candidatoVO.setDeficiencia(importarCandidatoVO.getDeficiencia());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getNome())) {
			candidatoVO.setNome(importarCandidatoVO.getNome());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getNomeSocial())) {
			candidatoVO.setNomeBatismo(importarCandidatoVO.getNomeSocial());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getEndereco())) {
			candidatoVO.setComplemento(importarCandidatoVO.getComplemento());		
			candidatoVO.setCEP(importarCandidatoVO.getCep());		
			candidatoVO.setNumero(importarCandidatoVO.getNumero());		
			candidatoVO.setSetor(importarCandidatoVO.getSetor());
			candidatoVO.setEndereco(importarCandidatoVO.getEndereco());
			candidatoVO.setZonaEleitoral(importarCandidatoVO.getZonaEleitoral());
			candidatoVO.setDataExpedicaoCertificadoMilitar(importarCandidatoVO.getDataExpedicaoCertificadoMilitar());
			candidatoVO.setOrgaoExpedidorCertificadoMilitar(importarCandidatoVO.getOrgaoExpedidorCertificadoMilitar());
		}
		candidatoVO.setCandidato(true);
		if (SituacaoMilitarEnum.getExisteValor(importarCandidatoVO.getSituacaoMilitar())) {
			candidatoVO.setSituacaoMilitar(SituacaoMilitarEnum.valueOf(importarCandidatoVO.getSituacaoMilitar()));
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getBanco())) {
			candidatoVO.setBanco(importarCandidatoVO.getBanco());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getAgencia())) {
			candidatoVO.setAgencia(importarCandidatoVO.getAgencia());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getContaCorrente())) {
			candidatoVO.setContaCorrente(importarCandidatoVO.getContaCorrente());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getUniversidadeParceira())) {
			candidatoVO.setUniversidadeParceira(importarCandidatoVO.getUniversidadeParceira());
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getModalidadeBolsa())) {
			candidatoVO.setModalidadeBolsa(ModalidadeBolsaEnum.getEnumPorValor(importarCandidatoVO.getModalidadeBolsa()));
		}
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getValorBolsa())) {
			candidatoVO.setValorBolsa(importarCandidatoVO.getValorBolsa());
		}
		if(Uteis.isAtributoPreenchido(importarCandidatoVO.getFormacaoAcademicaCurso()) && Uteis.isAtributoPreenchido(importarCandidatoVO.getFormacaoAcademicaEscolaridade())) {
			if(candidatoVO.getFormacaoAcademicaVOs().stream().noneMatch(c -> c.getCurso().equals(importarCandidatoVO.getFormacaoAcademicaCurso()))){
				formacao.setCurso(importarCandidatoVO.getFormacaoAcademicaCurso());
				formacao.setEscolaridade(importarCandidatoVO.getFormacaoAcademicaEscolaridade());
				formacao.setInstituicao(importarCandidatoVO.getFormacaoAcademicaIes());
				formacao.setTipoInst(importarCandidatoVO.getFormacaoAcademicaTipoIes());
				formacao.setAnoDataFim(importarCandidatoVO.getFormacaoAcademicaAno());
				formacao.setSemestreDataFim(importarCandidatoVO.getFormacaoAcademicaSemestre());
				formacao.setCidade(cidade);
				listaFormacaoAcademicaVOs.add(formacao);
				candidatoVO.setFormacaoAcademicaVOs(listaFormacaoAcademicaVOs);
			} else {				
				return candidatoVO;
			}
		}
		if(Uteis.isAtributoPreenchido(candidatoVO)) {			
			alterarSemValidarDados(candidatoVO, false, usuarioVO, configuracaoGeralSistemaVO, false);
		}else {
			incluirSemValidarDados(candidatoVO, false, usuarioVO, configuracaoGeralSistemaVO, false, false);
		}
		
		
		return candidatoVO;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void persistirPessoaComEmailInstitucional(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno, boolean deveValidarCPF, boolean validarCandidato, boolean validarDadosCensoEnade, boolean validarEndereco) throws Exception {
		Boolean novo = obj.isNovoObj();
		if(obj.isNovoObj()) {
			incluir(obj, verificarAcesso, usuario, configuracaoGeralSistema, editadoPorAluno, deveValidarCPF, validarCandidato, validarDadosCensoEnade, validarEndereco);
		}else {
			alterar(obj, verificarAcesso, usuario, configuracaoGeralSistema, editadoPorAluno, deveValidarCPF, validarCandidato, validarDadosCensoEnade, validarEndereco);
		}
		if(!obj.getListaPessoaGsuite().isEmpty()) {
			getFacadeFactory().getPessoaGsuiteFacade().persistir(obj, false, usuario);
		}

		getFacadeFactory().getPessoaEmailInstitucionalFacade().persistir(obj, false, usuario);
		if(!novo) {
			realizarAtualizacaoCadastralAlunoLDAPeBlackboard(obj, usuario);
		}
	}
	
	public void realizarAtualizacaoCadastralAlunoLDAPeBlackboard(PessoaVO obj, UsuarioVO usuario) {
		if(!obj.getNome().equals(obj.getNomeOriginal()) || !obj.getEmail().equals(obj.getEmailOriginal())){
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
				try {
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtualizacaoCadastralPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuario);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			getFacadeFactory().getLdapFacade().realizarAlterarDadosCadastraisPessoaLDAP(obj, pessoaEmailInstitucionalVO, null, usuario);
			obj.setNomeOriginal(obj.getNome());
			obj.setEmailOriginal(obj.getEmail());
		}
	}

	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarRegistroAcademico(final Integer codigoPessoa, final String registroAcademico) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set registroAcademico=? WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, registroAcademico);
					sqlAlterar.setInt(2, codigoPessoa.intValue());
					return sqlAlterar;
				}
		
	
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List consultaRapidaPessoaMatriculadoPorMatricula(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append(" WHERE (Matricula.matricula like('"+valorConsulta.toLowerCase().toUpperCase()+"%' ) ) ");	
		
		if (unidadeEnsino != null && Uteis.isAtributoPreenchido(unidadeEnsino.intValue())) {
			sqlStr.append(" AND matricula.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND pessoa.aluno = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor = 'true'");
		}
		sqlStr.append(" ORDER BY pessoa.cpf");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override
	public List consultaRapidaPessoaPorRegistroAcademico(String valorConsulta, Integer unidadeEnsino, String tipoPessoa, boolean filtrarMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPessoaPorRegistroAcademico(valorConsulta, unidadeEnsinoVOs, tipoPessoa, filtrarMatricula, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List consultaRapidaPessoaPorRegistroAcademico(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoPessoa, boolean filtrarMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(filtrarMatricula, false);	
		realizarVerificacaoFiltroFiliacaoParaPessoa(sqlStr, tipoPessoa);
		sqlStr.append(" WHERE  pessoa.registroAcademico ilike ('"+valorConsulta.toLowerCase().toUpperCase()+"%' ) ");		
		if (filtrarMatricula && Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND matricula.unidadeEnsino IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			if (tipoPessoa.equals("AL")) {
				sqlStr.append(" AND pessoa.aluno = 'true'");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" AND pessoa.professor = 'true'");
			}
		}
		sqlStr.append(" ORDER BY pessoa.cpf");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarDadosBancariosAluno(final String banco, final String agencia , final String contaCorrente, final Integer pessoa) throws Exception {
		try {
			final String sql = "UPDATE Pessoa set banco=? , agencia=? , contaCorrente=?  WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, banco);
					sqlAlterar.setString(++i, agencia);
					sqlAlterar.setString(++i, contaCorrente);
					sqlAlterar.setInt(++i, pessoa.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override                                         
	public List<PessoaVO> consultaRapidaResumidaPorEmailInstitucional(String valorConsulta, String tipoPessoa, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados,DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);		
		SqlRowSet tabelaResultado = null ;	
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sqlStr.append(getSQLPadraoConsultaBasica(false, false));		
		
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE exists (select codigo from pessoaemailinstitucional where pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.email ilike  ? ) ");
		}
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" AND (pessoa.aluno or pessoa.codigo in (select pais from filiacao where filiacao.pais = pessoa.codigo)) ");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" AND pessoa.professor ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" AND exists (select funcionario.codigo from funcionario where funcionario.pessoa = pessoa.codigo ) ");
		}
		sqlStr.append(" ) AS t ");		
	
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		 dataModelo.setTotalRegistrosEncontrados(0);
		if (valorConsulta.replace("%", "").trim().isEmpty()) {			
			 tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			}
			tabelaResultado.beforeFirst();	
			
		} else {
			 tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.removerAcentuacao(valorConsulta.toLowerCase()) + "%" });
			 if (tabelaResultado.next()) {
					dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
				}
			 tabelaResultado.beforeFirst();	
		}
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSemValidarDados(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean editadoPorAluno) throws Exception {

		try {

			if (obj.getArquivoImagem().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoImagem(), false, usuario, configuracaoGeralSistema);
			}
			if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
				obj.setNome(obj.getNome().toUpperCase());
			}
			if (!obj.getPossuiFilho()) {
				obj.setQtdFilhos(null);
			}
			final String sql = "UPDATE Pessoa SET nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, "
					+ "cidade=?, sexo=?, estadoCivil=?, telefoneComer=?, telefoneRes=?, " // 1-11
					+ "telefoneRecado=?, celular=?, email=?, dataNasc=?, naturalidade=?, "
					+ "nacionalidade=?, CPF=?, RG=?, certificadoMilitar=?, dataEmissaoRG=?, "//
					+ "estadoEmissaoRG=?, orgaoEmissor=?, tituloEleitoral=?, necessidadesEspeciais=?, " 
					+ "funcionario=?, professor=?, aluno=?, candidato=?, membroComunidade=?, " 
					+ "paginaPessoal=?, valorCssTopoLogo=?, valorCssBackground=?, valorCssMenu=?, perfilEconomico=?, atuaComoDocente=?, " 
					+ "ativo=?, idalunoinep=?, passaporte=?, corraca=?, deficiencia=?, nomeFiador=?, enderecoFiador=?, telefoneFiador=?, cpfFiador=?, tipoNecessidadesEspeciais=?, " 
					+ "celularFiador=?, arquivoImagem=?, email2=?, reponsavelUltimaAlteracao=?, dataUltimaAlteracao=?, coordenador=?, " 
					+ "ingles=?, espanhol=?, frances=?, inglesNivel=?, espanholNivel=?, francesNivel=?, outrosIdiomas=?, outrosIdiomasNivel=?, " 
					+ "windows=?, word=?, excel=?, access=?, powerPoint=?, internet=?, sap=?, corelDraw=?, autoCad=?, photoshop=?, microsiga=?, outrosSoftwares=?, qtdFilhos=?, participabancocurriculum=?, informacoesverdadeiras=?, divulgarmeusdados=?, "
					+ "certidaoNascimento=?, informacoesAdicionais=?, gerenciaPreInscricao=?, curriculoAtualizado=?,  pispasep=?, complementofiador=?, numeroendfiador=?, setorfiador=?, cepFiador=?, cidadeFiador=? ,  " 
					+ "possuiAcessoVisaoPais=?, codprospect=?, isentarTaxaBoleto=?, gravida=?, canhoto=?, portadorNecessidadeEspecial=?, situacaoMilitar=?, nomeBatismo=?, banco=?, agencia=?, contacorrente=?, "
					+ "universidadeparceira=?, modalidadebolsa=?, valorbolsa=?, tipoAssinaturaDocumentoEnum=?, transtornosNeurodivergentes=?  WHERE ((codigo = ?)) ";/*+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario)*/

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome().trim()));
					sqlAlterar.setString(2, obj.getEndereco());
					sqlAlterar.setString(3, obj.getSetor());
					sqlAlterar.setString(4, obj.getNumero());
					sqlAlterar.setString(5, obj.getCEP());
					sqlAlterar.setString(6, obj.getComplemento());
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getSexo().equals("NENHUM")) {
						sqlAlterar.setString(8, "");
					} else {
						sqlAlterar.setString(8, obj.getSexo());
					}
					sqlAlterar.setString(9, obj.getEstadoCivil());
					sqlAlterar.setString(10, obj.getTelefoneComer());
					sqlAlterar.setString(11, obj.getTelefoneRes());
					sqlAlterar.setString(12, obj.getTelefoneRecado());
					sqlAlterar.setString(13, obj.getCelular());
					sqlAlterar.setString(14, obj.getEmail().trim());
					sqlAlterar.setDate(15, Uteis.getDataJDBC(obj.getDataNasc()));
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(16, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(17, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(17, 0);
					}
					sqlAlterar.setString(18, obj.getCPF());
					sqlAlterar.setString(19, obj.getRG());
					sqlAlterar.setString(20, obj.getCertificadoMilitar());
					sqlAlterar.setDate(21, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
					sqlAlterar.setString(22, obj.getEstadoEmissaoRG());
					sqlAlterar.setString(23, obj.getOrgaoEmissor());
					sqlAlterar.setString(24, obj.getTituloEleitoral());
					sqlAlterar.setString(25, obj.getNecessidadesEspeciais());
					sqlAlterar.setBoolean(26, obj.getFuncionario().booleanValue());
					sqlAlterar.setBoolean(27, obj.getProfessor().booleanValue());
					sqlAlterar.setBoolean(28, obj.getAluno().booleanValue());
					sqlAlterar.setBoolean(29, obj.getCandidato().booleanValue());
					sqlAlterar.setBoolean(30, obj.getMembroComunidade().booleanValue());
					sqlAlterar.setString(31, obj.getPaginaPessoal());
					sqlAlterar.setString(32, obj.getValorCssTopoLogo());
					sqlAlterar.setString(33, obj.getValorCssBackground());
					sqlAlterar.setString(34, obj.getValorCssMenu());

					if (obj.getPerfilEconomico().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(35, obj.getPerfilEconomico().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(35, 0);
					}
					sqlAlterar.setString(36, obj.getAtuaComoDocente());
					sqlAlterar.setBoolean(37, obj.getAtivo().booleanValue());

					if (!obj.getIdAlunoInep().equals("")) {
						sqlAlterar.setString(38, obj.getIdAlunoInep());
					} else {
						sqlAlterar.setNull(38, 0);
					}
					sqlAlterar.setString(39, obj.getPassaporte());
					sqlAlterar.setString(40, obj.getCorRaca());
					sqlAlterar.setString(41, obj.getDeficiencia());
					sqlAlterar.setString(42, obj.getNomeFiador());
					sqlAlterar.setString(43, obj.getEnderecoFiador());
					sqlAlterar.setString(44, obj.getTelefoneFiador());
					sqlAlterar.setString(45, obj.getCpfFiador());
					sqlAlterar.setString(46, obj.getTipoNecessidadesEspeciais());
					sqlAlterar.setString(47, obj.getCelularFiador());
					if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(48, obj.getArquivoImagem().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(48, 0);
					}
					sqlAlterar.setString(49, obj.getEmail2().trim());

					if (usuario != null && usuario.getCodigo() != 0) {
						sqlAlterar.setInt(50, usuario.getCodigo().intValue());
					} else {
						sqlAlterar.setNull(50, 0);
					}
					sqlAlterar.setTimestamp(51, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setBoolean(52, obj.getCoordenador());

					sqlAlterar.setBoolean(53, obj.getIngles());
					sqlAlterar.setBoolean(54, obj.getEspanhol());
					sqlAlterar.setBoolean(55, obj.getFrances());
					sqlAlterar.setString(56, obj.getInglesNivel());
					sqlAlterar.setString(57, obj.getEspanholNivel());
					sqlAlterar.setString(58, obj.getFrancesNivel());
					sqlAlterar.setString(59, obj.getOutrosIdiomas());
					sqlAlterar.setString(60, obj.getOutrosIdiomasNivel());

					sqlAlterar.setBoolean(61, obj.getWindows());
					sqlAlterar.setBoolean(62, obj.getWord());
					sqlAlterar.setBoolean(63, obj.getExcel());
					sqlAlterar.setBoolean(64, obj.getAccess());
					sqlAlterar.setBoolean(65, obj.getPowerPoint());
					sqlAlterar.setBoolean(66, obj.getInternet());
					sqlAlterar.setBoolean(67, obj.getSap());
					sqlAlterar.setBoolean(68, obj.getCorelDraw());
					sqlAlterar.setBoolean(69, obj.getAutoCad());
					sqlAlterar.setBoolean(70, obj.getPhotoshop());
					sqlAlterar.setBoolean(71, obj.getMicrosiga());
					sqlAlterar.setString(72, obj.getOutrosSoftwares());
					sqlAlterar.setInt(73, obj.getQtdFilhos());
					sqlAlterar.setBoolean(74, obj.getParticipaBancoCurriculum().booleanValue());
					sqlAlterar.setBoolean(75, obj.getInformacoesVerdadeiras().booleanValue());
					sqlAlterar.setBoolean(76, obj.getDivulgarMeusDados().booleanValue());
					sqlAlterar.setString(77, obj.getCertidaoNascimento());
					sqlAlterar.setString(78, obj.getInformacoesAdicionais());
					sqlAlterar.setBoolean(79, obj.getGerenciaPreInscricao());
					sqlAlterar.setBoolean(80, obj.getCurriculoAtualizado());
					sqlAlterar.setString(81, obj.getPispasep());
					sqlAlterar.setString(82, obj.getComplementoFiador());
					sqlAlterar.setString(83, obj.getNumeroEndFiador());
					sqlAlterar.setString(84, obj.getSetorFiador());
					sqlAlterar.setString(85, obj.getCepFiador());
					if (obj.getCidadeFiador().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(86, obj.getCidadeFiador().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(86, 0);
					}
					sqlAlterar.setBoolean(87, obj.getPossuiAcessoVisaoPais());
					sqlAlterar.setInt(88, obj.getCodProspect());
					sqlAlterar.setBoolean(89, obj.getIsentarTaxaBoleto());
					sqlAlterar.setBoolean(90, obj.getGravida());
					sqlAlterar.setBoolean(91, obj.getCanhoto());
					sqlAlterar.setBoolean(92, obj.getPortadorNecessidadeEspecial());
					sqlAlterar.setString(93, obj.getSituacaoMilitar().name());
					sqlAlterar.setString(94, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo().trim()));
					sqlAlterar.setString(95, obj.getBanco());
					sqlAlterar.setString(96, obj.getAgencia());
					sqlAlterar.setString(97, obj.getContaCorrente());
					sqlAlterar.setString(98, obj.getUniversidadeParceira());					
					sqlAlterar.setString(99, obj.getModalidadeBolsa().name());
					sqlAlterar.setDouble(100, obj.getValorBolsa());
					sqlAlterar.setString(101, obj.getTipoAssinaturaDocumentoEnum().name());
					sqlAlterar.setString(102, obj.getTranstornosNeurodivergentes());
					sqlAlterar.setInt(103, obj.getCodigo().intValue());
					
					return sqlAlterar;
				}
			});

			if (!editadoPorAluno) {
				getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);

				getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getFormacaoExtraCurricularFacade().alterarFormacaoExtraCurricular(obj, obj.getFormacaoExtraCurricularVOs(), verificarAcesso);

				getFacadeFactory().getDadosComerciaisFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDadosComerciaisFacade().alterarDadosComerciais(obj, obj.getDadosComerciaisVOs(), verificarAcesso);

				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().alterarAreaProfissionalInteresseContratacao(obj.getCodigo(), obj.getAreaProfissionalInteresseContratacaoVOs(), usuario);
			}
			if (obj.getProfessor().equals(Boolean.TRUE)) {
				getFacadeFactory().getDocumetacaoPessoaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDocumetacaoPessoaFacade().alterarDocumetacaoPessoas(obj, obj.getCodigo(), obj.getDocumetacaoPessoaVOs(), usuario, configuracaoGeralSistema);

				getFacadeFactory().getDisciplinasInteresseFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getDisciplinasInteresseFacade().alterarDisciplinasInteresses(obj.getCodigo(), obj.getDisciplinasInteresseVOs(), usuario);

				getFacadeFactory().getHorarioProfessorFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getHorarioProfessorFacade().incluirHorarioProfessor(obj.getCodigo(), obj.getHorarioProfessorVOs(), null, null, usuario);
			}

			if (obj.getGerenciaPreInscricao().equals(Boolean.TRUE)) {
				getFacadeFactory().getPessoaPreInscricaoCursoFacade().alterarPessoaPreInscricaoCursos(obj.getCodigo(), obj.getPessoaPreInscricaoCursoVOs());
			}

//			getFacadeFactory().getFiliacaoFacade().setIdEntidade(this.getIdEntidade());
//			getFacadeFactory().getFiliacaoFacade().alterarFiliacaos(obj, obj.getFiliacaoVOs(), false, configuracaoGeralSistema, usuario);

//			Autorizado pelo Rodrigo na importação da planilha Excel
//			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
			
			if (obj.getGerarNumeroCPF()) {
				executarGeracaoNumeroCPF(obj);
			}
		} catch (Exception e) {

			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}	
	
	
	
	@Override
	public PessoaVO consultarRegistroAcademicoPorPessoa(Integer  pessoa,  boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();		
		sqlStr.append(" SELECT registroAcademico from pessoa where codigo =? ");	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),pessoa);		
		PessoaVO obj = new PessoaVO();
		if (tabelaResultado.next()) {			
	        obj.setRegistroAcademico(tabelaResultado.getString("registroAcademico"));
	    }
		return obj;
	
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void alterarApenasDadosPreenchidos(final ProspectsVO obj, boolean verificarAcesso, final UsuarioVO usuario) throws Exception {
		getFacadeFactory().getPessoaFacade().inicializarDadosPessoaBaseadoProspect(obj, usuario);
		getFacadeFactory().getPessoaFacade().validarDadosPessoaCasoPossuaMatricula(obj.getPessoa(), true, usuario);
		List<Object> lista = new ArrayList<Object>(0);
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE Pessoa SET ");
		sql.append(" nome=? ");
		lista.add(obj.getNome());
		if (Uteis.isAtributoPreenchido(obj.getNomeBatismo())) {
			sql.append(", nomeBatismo=? ");
			lista.add(obj.getNomeBatismo());
		}
		if (Uteis.isAtributoPreenchido(obj.getRg())) {
			sql.append(", rg=? ");
			lista.add(obj.getRg());
		}
		if (Uteis.isAtributoPreenchido(obj.getOrgaoEmissor())) {
			sql.append(", orgaoEmissor=? ");
			lista.add(obj.getOrgaoEmissor());
		}
		if (Uteis.isAtributoPreenchido(obj.getDataExpedicao())) {
			sql.append(", dataexpedicaotituloeleitoral=? ");
			lista.add(Uteis.getDataJDBC(obj.getDataExpedicao()));
		}
		if (Uteis.isAtributoPreenchido(obj.getEmailPrincipal())) {
			sql.append(", email=? ");
			lista.add(obj.getEmailPrincipal());
		}
		if (Uteis.isAtributoPreenchido(obj.getTelefoneResidencial())) {
			sql.append(", telefoneres=? ");
			lista.add(obj.getTelefoneResidencial());
		}
		if (Uteis.isAtributoPreenchido(obj.getTelefoneComercial())) {
			sql.append(", telefonecomer=? ");
			lista.add(obj.getTelefoneComercial());
		}
		if (Uteis.isAtributoPreenchido(obj.getCelular())) {
			sql.append(", celular=? ");
			lista.add(obj.getCelular());
		}
		if (Uteis.isAtributoPreenchido(obj.getSexo())) {
			sql.append(", sexo=? ");
			lista.add(obj.getSexo());
		}
		if (Uteis.isAtributoPreenchido(obj.getEstadoCivil())) {
			sql.append(", estadoCivil=? ");
			lista.add(obj.getEstadoCivil());
		}
		if (Uteis.isAtributoPreenchido(obj.getCEP())) {
			sql.append(", cep=? ");
			lista.add(obj.getCEP());
		}
		if (Uteis.isAtributoPreenchido(obj.getEndereco())) {
			sql.append(", endereco=? ");
			lista.add(obj.getEndereco());
		}
		if (Uteis.isAtributoPreenchido(obj.getSetor())) {
			sql.append(", setor=? ");
			lista.add(obj.getSetor());
		}
		if (Uteis.isAtributoPreenchido(obj.getCidade())) {
			sql.append(", cidade=? ");
			lista.add(obj.getCidade().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getDataNascimento())) {
			sql.append(", datanasc=? ");
			lista.add(Uteis.getDataJDBC(obj.getDataNascimento()));
		}
		if (Uteis.isAtributoPreenchido(obj.getNaturalidade())) {
			sql.append(", naturalidade=? ");
			lista.add(obj.getNaturalidade().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getTipoMidia())) {
			sql.append(", tipomidiacaptacao=? ");
			lista.add(obj.getTipoMidia().getCodigo());
		}
		sql.append(" WHERE codigo=? ");
		lista.add(obj.getPessoa().getCodigo());
		getConexao().getJdbcTemplate().update(sql.toString(), lista.toArray());
		if (Uteis.isAtributoPreenchido(obj.getFormacaoAcademicaVOs())) {
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(obj.getPessoa().getCodigo(), obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);
		}
		getFacadeFactory().getUsuarioFacade().alterarNomeUsuario(obj.getPessoa().getCodigo(), obj.getNome(), usuario);
	}
	
	@Override
	public Boolean verificarSeExistePessoaUtilizandoRegistroAcademico(String registroAcademico , Boolean desconsiderarPessoa , Integer codigoPessoa) {
		StringBuilder sqlStr = new StringBuilder("");	
		sqlStr.append(" select codigo from pessoa where registroacademico ='").append(registroAcademico).append("' ");
		sqlStr.append(" and aluno = true");
		if(desconsiderarPessoa &&  Uteis.isAtributoPreenchido(codigoPessoa)) {
			sqlStr.append(" and  codigo <> ").append(codigoPessoa);
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true ;
		} else {
			return false;
		}
		
	}
	
	@Override
	public List<PessoaVO> consultaRapidaPorRegistroAcademicoAutoComplete(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int limit, boolean contrarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), contrarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" SELECT codigo, registroacademico, nome ");
		sqlStr.append(" FROM pessoa ");
		sqlStr.append(" WHERE pessoa.registroacademico ILIKE (?) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND EXISTS (SELECT FROM matricula ");
			sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
			sqlStr.append(" WHERE matricula.aluno = pessoa.codigo ");
			sqlStr.append(" AND unidadeensino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(")) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome ");
		sqlStr.append(" LIMIT ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		List<PessoaVO> listaPessoa = new ArrayList<PessoaVO>(0);
		while (tabelaResultado.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setRegistroAcademico(tabelaResultado.getString("registroacademico"));
			obj.setNome(tabelaResultado.getString("nome"));
			listaPessoa.add(obj);
		}
		return listaPessoa;
	}
	
	@Override                                         
	public PessoaVO consultarPorRegistroAcademico(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE pessoa.registroAcademico = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
		PessoaVO pessoaVO = new PessoaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(pessoaVO, tabelaResultado);
		}
		return pessoaVO;
	}
	
	@Override
	public List<PessoaVO> consultaRapidaPorUnidadeEnsinoPorCurso(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos((curso.nome)) ILIKE(trim(sem_acentos(?)))");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND unidadeEnsino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	@Override
	public PessoaVO consultarPorEmail(String email, String tipoPessoa, boolean forcarUsoEmailInstitucional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder("SELECT T.* FROM (SELECT pessoa.*, 1 ordem, false is_emailinstitucional FROM pessoa ");
		sqlStr.append(" WHERE unaccent(trim(lower(pessoa.email))) = unaccent(lower(?)) ");
		getSQLFiltroTipoPessoa(tipoPessoa, sqlStr);
		sqlStr.append(" UNION ALL SELECT pessoa.*, 2, true FROM pessoa ");
		sqlStr.append(" INNER JOIN pessoaemailinstitucional ON pessoaemailinstitucional.pessoa = pessoa.codigo ");
		sqlStr.append(" WHERE unaccent(trim(lower(pessoaemailinstitucional.email))) = unaccent(lower(?)) ");
		getSQLFiltroTipoPessoa(tipoPessoa, sqlStr);
		sqlStr.append(" ) T ORDER BY ordem LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), email, email);
		if (tabelaResultado.next()) {
			PessoaVO pessoaVO = montarDados(tabelaResultado, null, nivelMontarDados, usuarioVO);
			if (forcarUsoEmailInstitucional && tabelaResultado.getBoolean("is_emailinstitucional")) {
				PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(pessoaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(emailInstitucional)) {
					pessoaVO.setEmail(emailInstitucional.getEmail());
					pessoaVO.getListaPessoaEmailInstitucionalVO().add(emailInstitucional);
				}
			}
			return pessoaVO;
		}
		return null;
	}

	private void getSQLFiltroTipoPessoa(String tipoPessoa, StringBuilder stringBuilder) {
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			if (tipoPessoa.equals("FU") || tipoPessoa.equals("CO")) {
				stringBuilder.append(" AND (pessoa.funcionario OR pessoa.coordenador) ");
			} else if (tipoPessoa.equals("PR")) {
				stringBuilder.append(" AND pessoa.professor ");
			} else if (tipoPessoa.equals("AL")) {
				stringBuilder.append(" AND pessoa.aluno ");
			}
		}
	}
	
	@Override
	public void consultarDocentesEstagioOtimizado(DataModelo controleConsultaOtimizado) {
		controleConsultaOtimizado.setTotalRegistrosEncontrados(0);
		controleConsultaOtimizado.setListaConsulta(new ArrayList<>());
		controleConsultaOtimizado.setLimitePorPagina(5);
		String CPF = "cpf";
		String NOME = "nome";
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) over() as qtde_total_registros, codigo, nome, cpf FROM pessoa ");
		sql.append("WHERE pessoa.funcionario AND EXISTS (SELECT FROM funcionario f WHERE f.pessoa = pessoa.codigo) ");
		if (controleConsultaOtimizado.getCampoConsulta().equals(CPF)) {
			sql.append("AND (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");
			sql.append("ORDER BY pessoa.cpf ");
		} else {
			sql.append("AND unaccent(pessoa.nome) ILIKE(unaccent(?)) ");
			sql.append("ORDER BY pessoa.nome ");
		}
		sql.append(" LIMIT ").append(controleConsultaOtimizado.getLimitePorPagina());
		sql.append(" OFFSET ").append(controleConsultaOtimizado.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), (controleConsultaOtimizado.getCampoConsulta().equals(NOME) ? controleConsultaOtimizado.getValorConsulta() + PERCENT : controleConsultaOtimizado.getValorConsulta()));
		if (tabelaResultado.next()) {
			controleConsultaOtimizado.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			tabelaResultado.beforeFirst();
			List<PessoaVO> pessoaVOs = new ArrayList<>();
			while (tabelaResultado.next()) {
				PessoaVO docente = new PessoaVO();
				docente.setCodigo(tabelaResultado.getInt("codigo"));
				docente.setNome(tabelaResultado.getString("nome"));
				docente.setCPF(tabelaResultado.getString("cpf"));
				pessoaVOs.add(docente);
			}
			controleConsultaOtimizado.setListaConsulta(pessoaVOs);
		}
	}
	
	@Override
	public List<PessoaVO> consultarDestinatariosMensagemMoodlePorEmail(List<String> emails, String tipoPessoa) throws Exception {
		//aqui
		if (!Uteis.isAtributoPreenchido(emails)) {
			return new ArrayList<>(0);
		}
		StringBuilder sql = new StringBuilder("WITH cte_emails AS ( ");
		sql.append("SELECT unaccent(trim(lower(UNNEST(ARRAY[").append(emails.stream().map(email -> "'" + email + "'").collect(Collectors.joining(", "))).append("])))) email ");
		sql.append(") ");
		sql.append("SELECT pessoa.codigo codigo_pessoa, pessoa.nome nome_pessoa, pessoa.email email_pessoa, pessoa.email2 email2_pessoa, pessoa.cpf cpf_pessoa, pessoa.celular celular_pessoa, pessoa.telefonerecado telefonerecado_pessoa, pessoa.telefoneres telefoneres_pessoa, pessoa.telefonefiador telefonefiador_pessoa, pessoa.telefonecomer telefonecomer_pessoa, pessoa.membrocomunidade membrocomunidade_pessoa, pessoaemailinstitucional.codigo codigo_pessoaemailinstitucional, pessoaemailinstitucional.email email_pessoaemailinstitucional ");
		sql.append("FROM pessoa ");
		sql.append("LEFT JOIN LATERAL ( SELECT pessoaemailinstitucional.codigo, pessoaemailinstitucional.email FROM pessoaemailinstitucional WHERE pessoaemailinstitucional.pessoa = pessoa.codigo AND pessoaemailinstitucional.statusativoinativoenum = 'ATIVO' ORDER BY pessoaemailinstitucional.codigo DESC LIMIT 1) pessoaemailinstitucional ON TRUE ");
		sql.append("WHERE EXISTS (SELECT FROM cte_emails WHERE cte_emails.email = unaccent(trim(lower(pessoa.email))) LIMIT 1) OR EXISTS ( SELECT FROM cte_emails WHERE cte_emails.email = unaccent(trim(lower(pessoaemailinstitucional.email))) LIMIT 1) ");
		getSQLFiltroTipoPessoa(tipoPessoa, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PessoaVO> pessoaVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			PessoaVO pessoaVO = new PessoaVO();
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = new PessoaEmailInstitucionalVO();
			pessoaVO.setCodigo(tabelaResultado.getInt("codigo_pessoa"));
			pessoaVO.setNome(tabelaResultado.getString("nome_pessoa"));
			pessoaVO.setEmail(tabelaResultado.getString("email_pessoa"));
			pessoaVO.setEmail2(tabelaResultado.getString("email2_pessoa"));
			pessoaVO.setCPF(tabelaResultado.getString("cpf_pessoa"));
			pessoaVO.setCelular(tabelaResultado.getString("celular_pessoa"));
			pessoaVO.setTelefoneRecado(tabelaResultado.getString("telefonerecado_pessoa"));
			pessoaVO.setTelefoneRes(tabelaResultado.getString("telefoneres_pessoa"));
			pessoaVO.setTelefoneFiador(tabelaResultado.getString("telefonefiador_pessoa"));
			pessoaVO.setTelefoneComer(tabelaResultado.getString("telefonecomer_pessoa"));
			pessoaVO.setMembroComunidade(tabelaResultado.getBoolean("membrocomunidade_pessoa"));
			pessoaEmailInstitucionalVO.setCodigo(tabelaResultado.getInt("codigo_pessoaemailinstitucional"));
			pessoaEmailInstitucionalVO.setEmail(tabelaResultado.getString("email_pessoaemailinstitucional"));
			pessoaEmailInstitucionalVO.setPessoaVO(pessoaVO);
			if (Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
				pessoaVO.setEmail(pessoaEmailInstitucionalVO.getEmail());
				pessoaVO.getListaPessoaEmailInstitucionalVO().add(pessoaEmailInstitucionalVO);
			}
			int qtdEmail = emails.size();
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("email_pessoaemailinstitucional"))) {
				emails.remove(tabelaResultado.getString("email_pessoaemailinstitucional").toLowerCase().trim());
			}
			if ((qtdEmail == emails.size()) && Uteis.isAtributoPreenchido(tabelaResultado.getString("email_pessoa"))) {
				emails.remove(tabelaResultado.getString("email_pessoa").toLowerCase().trim());
			}
			pessoaVOs.add(pessoaVO);
		}
		return pessoaVOs;
	}
	
	@Override
	public String consultarRegistroAcademicoPorMatricula(String matricula) {
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet("select case when registroacademico != '' then registroacademico else matricula end as registroacademico from matricula inner join pessoa on pessoa.codigo = matricula.aluno where matricula.matricula =  ? ",  matricula);
		if(rs.next()) {
			return rs.getString("registroacademico");
		}
		return "";
	}
}