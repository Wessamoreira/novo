package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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

import jobs.JobEnviarEmailProspectsSelecionadosPainelGestor;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.TipoProspectVO;
import negocio.comuns.crm.enumerador.FormacaoAcademicaProspectEnum;
import negocio.comuns.crm.enumerador.RendaProspectEnum;
import negocio.comuns.crm.enumerador.TipoEmpresaProspectEnum;
import negocio.comuns.crm.enumerador.TipoOrigemCadastroProspectEnum;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboProspectsEnum;
import negocio.comuns.segmentacao.ProspectSegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.crm.ProspectsInterfaceFacade;
import webservice.servicos.AlunoAutoAtendimentoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ProspectsVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ProspectsVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProspectsVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class Prospects extends ControleAcesso implements ProspectsInterfaceFacade {

	protected static String idEntidade;
	private Hashtable cursoInteresses;

	public Prospects() throws Exception {
		super();
		setIdEntidade("Prospects");
		setCursoInteresses(new Hashtable(0));
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProspectsVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProspectsVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirSemValidarDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			realizarUpperCaseDados(obj);
			if (obj.getArquivoFoto().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoFoto(), false, usuario, configuracaoGeralSistema);
			}
			if (obj.getFormacaoAcademica() == null) {
				obj.setFormacaoAcademica(FormacaoAcademicaProspectEnum.NENHUM);
			}
			if (obj.getRenda() == null) {
				obj.setRenda(RendaProspectEnum.NENHUM);
			}
			if (obj.getTipoEmpresa() == null) {
				obj.setTipoEmpresa(TipoEmpresaProspectEnum.NENHUM);
			}
			if (!obj.getEmailPrincipal().equals("")) {
					if (validarUnicidadeEmailProspect(obj.getEmailPrincipal(), obj.getCodigo())) {
						throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailPrincipal());
					}
				}
			if (!obj.getEmailSecundario().trim().equals("")) {
				if (validarUnicidadeEmailProspect(obj.getEmailSecundario(), obj.getCodigo())) {
					throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailSecundario());
			}
			}

			final String sql = "INSERT INTO Prospects( nome, razaoSocial, cpf, rg, dataNascimento, sexo, cnpj, " + "inscricaoEstadual, cep, endereco, complemento, setor, telefoneComercial, telefoneResidencial, " + "telefoneRecado, celular, skype, emailPrincipal, emailSecundario, renda, formacaoAcademica, " + "tipoEmpresa, unidadeEnsino, pessoa, tipoProspect, arquivofoto, cidade, " + "unidadeensinoprospect, parceiro, fornecedor, nomeEmpresa, cargo, telefoneEmpresa, " + "curso, dataCadastro, consultorPadrao, orgaoEmissor, estadoEmissor, numero, estadoCivil, " + "nacionalidade, naturalidade, dataExpedicao, nomePai, nomeMae, participaBancoCurriculum, " + "divulgarMeusDados, responsavelCadastro, tipoOrigemCadastro, inativo, motivoInativacao, responsavelInativacao, tipoMidia, responsavelFinanceiro, sincronizadordstation, nomeBatismo  ) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
					sqlInserir.setString(2, obj.getRazaoSocial());
					sqlInserir.setString(3, obj.getCpf());
					sqlInserir.setString(4, obj.getRg());
					if (obj.getDataNascimento() != null) {
						sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataNascimento()));
					} else {
						sqlInserir.setNull(5, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getSexo()) && !obj.getSexo().equals("NEHNHUM")){
						sqlInserir.setString(6, obj.getSexo());
					}else{
						sqlInserir.setString(6, "");
					}
					sqlInserir.setString(7, obj.getCnpj());
					sqlInserir.setString(8, obj.getInscricaoEstadual());
					sqlInserir.setString(9, obj.getCEP());
					sqlInserir.setString(10, obj.getEndereco());
					sqlInserir.setString(11, obj.getComplemento());
					sqlInserir.setString(12, obj.getSetor());
					sqlInserir.setString(13, obj.getTelefoneComercial());
					sqlInserir.setString(14, obj.getTelefoneResidencial());
					sqlInserir.setString(15, obj.getTelefoneRecado());
					sqlInserir.setString(16, obj.getCelular());
					sqlInserir.setString(17, obj.getSkype());
					sqlInserir.setString(18, obj.getEmailPrincipal().trim());
					sqlInserir.setString(19, obj.getEmailSecundario().trim());
					sqlInserir.setString(20, obj.getRenda().toString());
					sqlInserir.setString(21, obj.getFormacaoAcademica().toString());
					sqlInserir.setString(22, obj.getTipoEmpresa().toString());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(23, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(23, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(24, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(24, 0);
					}
					sqlInserir.setString(25, obj.getTipoProspect().toString());
					if (obj.getArquivoFoto().getCodigo().intValue() != 0) {
						sqlInserir.setInt(26, obj.getArquivoFoto().getCodigo().intValue());
					} else {
						sqlInserir.setNull(26, 0);
					}
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(27, obj.getCidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(27, 0);
					}
					if (obj.getUnidadeEnsinoProspect().getCodigo().intValue() != 0) {
						sqlInserir.setInt(28, obj.getUnidadeEnsinoProspect().getCodigo().intValue());
					} else {
						sqlInserir.setNull(28, 0);
					}
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(29, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(29, 0);
					}
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlInserir.setInt(30, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlInserir.setNull(30, 0);
					}
					sqlInserir.setString(31, obj.getNomeEmpresa());
					sqlInserir.setString(32, obj.getCargo());
					sqlInserir.setString(33, obj.getTelefoneEmpresa());
					sqlInserir.setString(34, obj.getCurso());
					sqlInserir.setTimestamp(35, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					if (obj.getConsultorPadrao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(36, obj.getConsultorPadrao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(36, 0);
					}
					sqlInserir.setString(37, obj.getOrgaoEmissor());
					sqlInserir.setString(38, obj.getEstadoEmissor());

					sqlInserir.setString(39, obj.getNumero());
					sqlInserir.setString(40, obj.getEstadoCivil());
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(41, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(41, 0);
					}
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlInserir.setInt(42, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlInserir.setNull(42, 0);
					}
					if (obj.getDataExpedicao() != null) {
						sqlInserir.setDate(43, Uteis.getDataJDBC(obj.getDataExpedicao()));
					} else {
						sqlInserir.setNull(43, 0);
					}
					sqlInserir.setString(44, obj.getNomePai());
					sqlInserir.setString(45, obj.getNomeMae());
					sqlInserir.setBoolean(46, obj.getParticipaBancoCurriculum());
					sqlInserir.setBoolean(47, obj.getDivulgarMeusDados());

					if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(48, obj.getResponsavelCadastro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(48, 0);
					}
					sqlInserir.setString(49, obj.getTipoOrigemCadastro().toString());
					sqlInserir.setBoolean(50, obj.getInativo());
					sqlInserir.setString(51, obj.getMotivoInativacao());
					if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(52, obj.getResponsavelInativacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(52, 0);
					}
					if (obj.getTipoMidia() != null && obj.getTipoMidia().getCodigo() != null && obj.getTipoMidia().getCodigo().intValue() != 0) {
						sqlInserir.setInt(53, obj.getTipoMidia().getCodigo().intValue());
					} else {
						sqlInserir.setNull(53, 0);
					}
					sqlInserir.setBoolean(54, obj.getResponsavelFinanceiro());
					sqlInserir.setBoolean(55, obj.getSincronizadoRDStation());
					sqlInserir.setString(56, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
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
			getFacadeFactory().getCursoInteresseFacade().incluirCursoInteresses(obj.getCodigo(), obj.getCursoInteresseVOs(), usuario);
			getFacadeFactory().getFormacaoAcademicaFacade().incluirFormacaoAcademicasProspects(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), usuario);
			getFacadeFactory().getProspectSegmentacaoOpcaoFacade().incluirProspectSegmentacaoOpcoes(obj, usuario);
			if ((obj.getConsultorPadrao().getCodigo().intValue() != 0) && (usuario != null)) {
				incluirLogConsultorPadrao(obj.getConsultorPadrao().getCodigo(), usuario.getCodigo(), usuario);
			}
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogConsultorPadrao(final Integer codConsultor, final Integer usuario, UsuarioVO usuarioLogado) throws Exception {
		try {
			final String sql = "INSERT INTO LogConsultorProspects( consultor, data, responsavel ) " + "VALUES (?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			Integer codigo = ((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, codConsultor);
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setInt(3, usuario.intValue());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProspectsVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProspectsVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirComValidacaoDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			Prospects.incluir(getIdEntidade(), verificarAcesso, usuario);
			validarDados(obj);
			incluirSemValidarDados(obj, verificarAcesso, usuario, configuracaoGeralSistema);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirRapidaPorLigacaoReceptia(final ProspectsVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			Prospects.incluir(getIdEntidade(), false, usuario);
			if (obj.getNome().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoNomeDeveSerInformado"));
			}
			if (!obj.getEmailPrincipal().equals("")) {
				if (validarUnicidadeEmailProspect(obj.getEmailPrincipal(), obj.getCodigo())) {
					throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailPrincipal());
				}
			}
			if (!obj.getEmailSecundario().trim().equals("")) {
				if (validarUnicidadeEmailProspect(obj.getEmailSecundario(), obj.getCodigo())) {
					throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailSecundario());
				}				
			}
			validarDados(obj);
			incluirSemValidarDados(obj, false, usuario, configuracaoGeralSistema);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProspectsVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados ( <code>validarDados</code>) do objeto. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProspectsVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarComValidarDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			validarDados(obj);
			Prospects.alterar(getIdEntidade(), verificarAcesso, usuario);
			realizarUpperCaseDados(obj);
			if (obj.getArquivoFoto().getPastaBaseArquivoEnum() != null) {
				if (obj.getArquivoFoto().getCodigo() == 0) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoFoto(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoFoto(), false, usuario, configuracaoGeralSistema);
				}
			}
			// //System.out.print("alterarComValidarDados => " + obj.getCodigo() +
			// " " + obj.getPessoa().getCodigo() + " DATA " +
			// Uteis.getDataJDBC(new Date()));
			if (!obj.getEmailPrincipal().trim().equals("")) {
					if (validarUnicidadeEmailProspect(obj.getEmailPrincipal(), obj.getCodigo())) {
						throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailPrincipal());
					}
				}
			if (!obj.getEmailSecundario().trim().equals("")) {
				if (validarUnicidadeEmailProspect(obj.getEmailSecundario(), obj.getCodigo())) {
					throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailSecundario());
			}
			}
			
			
			verificarProspectComPessoaPreenchidoBaseELimpoObjeto(obj, obj.getPessoa());
			// //// AKI ///////

			final String sql = "UPDATE Prospects set nome=?, razaoSocial=?, cpf=?, rg=?, dataNascimento=?, " + "sexo=?, cnpj=?, inscricaoEstadual=?, cep=?, endereco=?, complemento=?, " + "setor=?, telefoneComercial=?, telefoneResidencial=?, telefoneRecado=?, " + "celular=?, skype=?, emailPrincipal=?, emailSecundario=?, renda=?, " + "formacaoAcademica=?, tipoEmpresa=?, unidadeEnsino=?, pessoa=?, tipoProspect=?, " + "arquivofoto=?, cidade=?, unidadeensinoprospect=?, parceiro=?, fornecedor=?, " + "nomeEmpresa=?, cargo=?, telefoneEmpresa=?, curso=?, consultorPadrao=?, " + "orgaoEmissor=?, estadoEmissor=?, numero=?, estadoCivil=?, nacionalidade=?, " + "naturalidade=?, dataExpedicao=?, nomePai=?, nomeMae=?, participaBancoCurriculum=?, " + "divulgarMeusDados=?, responsavelCadastro=?, tipoOrigemCadastro=?, inativo=?, " + "motivoInativacao=?, responsavelInativacao=?, tipoMidia=? , sincronizadoRdStation=false, nomeBatismo=?" + " WHERE ((codigo = ?)) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			if (!(Boolean) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
					sqlAlterar.setString(2, obj.getRazaoSocial());
					sqlAlterar.setString(3, obj.getCpf());
					sqlAlterar.setString(4, obj.getRg());
					if (obj.getDataNascimento() != null) {
						sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataNascimento()));
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getSexo()) && !obj.getSexo().equals("NEHNHUM")){
						sqlAlterar.setString(6, obj.getSexo());
					}else{
						sqlAlterar.setString(6, "");
					}
					sqlAlterar.setString(7, obj.getCnpj());
					sqlAlterar.setString(8, obj.getInscricaoEstadual());
					sqlAlterar.setString(9, obj.getCEP());
					sqlAlterar.setString(10, obj.getEndereco());
					sqlAlterar.setString(11, obj.getComplemento());
					sqlAlterar.setString(12, obj.getSetor());
					sqlAlterar.setString(13, obj.getTelefoneComercial());
					sqlAlterar.setString(14, obj.getTelefoneResidencial());
					sqlAlterar.setString(15, obj.getTelefoneRecado());
					sqlAlterar.setString(16, obj.getCelular());
					sqlAlterar.setString(17, obj.getSkype());
					sqlAlterar.setString(18, obj.getEmailPrincipal().trim());
					sqlAlterar.setString(19, obj.getEmailSecundario().trim());
					sqlAlterar.setString(20, obj.getRenda().toString());
					sqlAlterar.setString(21, obj.getFormacaoAcademica().toString());
					sqlAlterar.setString(22, obj.getTipoEmpresa().toString());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(23, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(23, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(24, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(24, 0);
					}
					sqlAlterar.setString(25, obj.getTipoProspect().toString());
					if (obj.getArquivoFoto().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(26, obj.getArquivoFoto().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(26, 0);
					}
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(27, obj.getCidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(27, 0);
					}
					if (obj.getUnidadeEnsinoProspect().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(28, obj.getUnidadeEnsinoProspect().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(28, 0);
					}
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(29, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(29, 0);
					}
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(30, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(30, 0);
					}
					sqlAlterar.setString(31, obj.getNomeEmpresa());
					sqlAlterar.setString(32, obj.getCargo());
					sqlAlterar.setString(33, obj.getTelefoneEmpresa());
					sqlAlterar.setString(34, obj.getCurso());
					if (obj.getConsultorPadrao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(35, obj.getConsultorPadrao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(35, 0);
					}
					sqlAlterar.setString(36, obj.getOrgaoEmissor());
					sqlAlterar.setString(37, obj.getEstadoEmissor());

					sqlAlterar.setString(38, obj.getNumero());
					sqlAlterar.setString(39, obj.getEstadoCivil());
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(40, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(40, 0);
					}
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(41, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(41, 0);
					}
					if (obj.getDataExpedicao() != null) {
						sqlAlterar.setDate(42, Uteis.getDataJDBC(obj.getDataExpedicao()));
					} else {
						sqlAlterar.setNull(42, 0);
					}
					sqlAlterar.setString(43, obj.getNomePai());
					sqlAlterar.setString(44, obj.getNomeMae());
					sqlAlterar.setBoolean(45, obj.getParticipaBancoCurriculum());
					sqlAlterar.setBoolean(46, obj.getDivulgarMeusDados());

					if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(47, obj.getResponsavelCadastro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(47, 0);
					}
					sqlAlterar.setString(48, obj.getTipoOrigemCadastro().toString());
					sqlAlterar.setBoolean(49, obj.getInativo());
					sqlAlterar.setString(50, obj.getMotivoInativacao());
					if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(51, obj.getResponsavelInativacao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(51, 0);
					}
					if (obj.getTipoMidia().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(52, obj.getTipoMidia().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(52, 0);
					}
					sqlAlterar.setString(53, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
					sqlAlterar.setInt(54, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					return arg0.next();
				}
			})) {
				incluirComValidacaoDados(obj, verificarAcesso, usuario, configuracaoGeralSistema);
				return;
			}
			getFacadeFactory().getCursoInteresseFacade().alterarCursoInteresses(obj.getCodigo(), obj.getCursoInteresseVOs(), usuario);
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicasProspects(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), false, usuario);
			getFacadeFactory().getPessoaFacade().alterarPessoaConformeProspect(obj, verificarAcesso, usuario, configuracaoGeralSistema, false, false);
			getFacadeFactory().getProspectSegmentacaoOpcaoFacade().alterarProspectsSegmentacaoOpcao(obj.getCodigo(), obj.getProspectSegmentacaoOpcaoVOs(), usuario);
			if (obj.getInativo()) {
				getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluirCompromissoAgendaPessoaHorarioNaoInicializadoProspect(obj.getCodigo(), usuario);
			}
			if (obj.getConsultorPadrao().getCodigo().intValue() != 0) {
				incluirLogConsultorPadrao(obj.getConsultorPadrao().getCodigo(), usuario.getCodigo(), usuario);
			}
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarAlteracaoCompromissoAoAlterarConsultor(obj, usuario);
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSemValidarDados(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean origemPreInscricao) throws Exception {
		try {
			Prospects.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			if (obj.getArquivoFoto().getPastaBaseArquivoEnum() != null) {
				if (obj.getArquivoFoto().getCodigo() == 0) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoFoto(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoFoto(), false, usuario, configuracaoGeralSistema);
				}
			}
			// //System.out.print("alterarSemValidarDados => " + obj.getCodigo() +
			// " " + obj.getPessoa().getCodigo() + " DATA " +
			// Uteis.getDataJDBC(new Date()));
			verificarProspectComPessoaPreenchidoBaseELimpoObjeto(obj, obj.getPessoa());
			// //// AKI ///////
			final String sql = "UPDATE Prospects set nome=?, razaoSocial=?, cpf=?, rg=?, dataNascimento=?, sexo=?, cnpj=?, inscricaoEstadual=?, cep=?, endereco=?, complemento=?, setor=?, telefoneComercial=?, telefoneResidencial=?, telefoneRecado=?, celular=?, skype=?, emailPrincipal=?, emailSecundario=?, renda=?, formacaoAcademica=?, tipoEmpresa=?, unidadeEnsino=?, pessoa=?, tipoProspect=?, arquivofoto=?, cidade=?, unidadeensinoprospect=?, parceiro=?, fornecedor=?, nomeEmpresa=?, cargo=?, telefoneEmpresa=?, curso=?, consultorPadrao=?, orgaoEmissor=?, estadoEmissor=?, numero=?, estadoCivil=?, nacionalidade=?, naturalidade=?, dataExpedicao=?, nomePai=?, nomeMae=?, participaBancoCurriculum=?, divulgarMeusDados=?, tipoMidia=?, responsavelFinanceiro=?, sincronizadoRdStation=?, nomeBatismo=?  WHERE ((codigo = ?)) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			if (!(Boolean) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
					sqlAlterar.setString(2, obj.getRazaoSocial());
					sqlAlterar.setString(3, obj.getCpf());
					sqlAlterar.setString(4, obj.getRg());
					if (obj.getDataNascimento() != null) {
						sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataNascimento()));
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getSexo()) && !obj.getSexo().equals("NEHNHUM")){
						sqlAlterar.setString(6, obj.getSexo());
					}else{
						sqlAlterar.setString(6, "");
					}
					sqlAlterar.setString(7, obj.getCnpj());
					sqlAlterar.setString(8, obj.getInscricaoEstadual());
					sqlAlterar.setString(9, obj.getCEP());
					sqlAlterar.setString(10, obj.getEndereco());
					sqlAlterar.setString(11, obj.getComplemento());
					sqlAlterar.setString(12, obj.getSetor());
					sqlAlterar.setString(13, obj.getTelefoneComercial());
					sqlAlterar.setString(14, obj.getTelefoneResidencial());
					sqlAlterar.setString(15, obj.getTelefoneRecado());
					sqlAlterar.setString(16, obj.getCelular());
					sqlAlterar.setString(17, obj.getSkype());
					sqlAlterar.setString(18, obj.getEmailPrincipal().trim());
					sqlAlterar.setString(19, obj.getEmailSecundario().trim());
					sqlAlterar.setString(20, obj.getRenda().toString());
					sqlAlterar.setString(21, obj.getFormacaoAcademica().toString());
					sqlAlterar.setString(22, obj.getTipoEmpresa().toString());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(23, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(23, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(24, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(24, 0);
					}
					sqlAlterar.setString(25, obj.getTipoProspect().toString());
					if (obj.getArquivoFoto().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(26, obj.getArquivoFoto().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(26, 0);
					}
					if (obj.getCidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(27, obj.getCidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(27, 0);
					}
					if (obj.getUnidadeEnsinoProspect().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(28, obj.getUnidadeEnsinoProspect().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(28, 0);
					}
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(29, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(29, 0);
					}
					if (obj.getFornecedor().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(30, obj.getFornecedor().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(30, 0);
					}
					sqlAlterar.setString(31, obj.getNomeEmpresa());
					sqlAlterar.setString(32, obj.getCargo());
					sqlAlterar.setString(33, obj.getTelefoneEmpresa());
					sqlAlterar.setString(34, obj.getCurso());
					if (obj.getConsultorPadrao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(35, obj.getConsultorPadrao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(35, 0);
					}
					sqlAlterar.setString(36, obj.getOrgaoEmissor());
					sqlAlterar.setString(37, obj.getEstadoEmissor());
					sqlAlterar.setString(38, obj.getNumero());
					sqlAlterar.setString(39, obj.getEstadoCivil());
					if (obj.getNacionalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(40, obj.getNacionalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(40, 0);
					}
					if (obj.getNaturalidade().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(41, obj.getNaturalidade().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(41, 0);
					}
					if (obj.getDataExpedicao() != null) {
						sqlAlterar.setDate(42, Uteis.getDataJDBC(obj.getDataExpedicao()));
					} else {
						sqlAlterar.setNull(42, 0);
					}
					sqlAlterar.setString(43, obj.getNomePai());
					sqlAlterar.setString(44, obj.getNomeMae());
					sqlAlterar.setBoolean(45, obj.getParticipaBancoCurriculum());
					sqlAlterar.setBoolean(46, obj.getDivulgarMeusDados());
					if (obj.getTipoMidia().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(47, obj.getTipoMidia().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(47, 0);
					}
					sqlAlterar.setBoolean(48, obj.getResponsavelFinanceiro());
					sqlAlterar.setBoolean(49, obj.getSincronizadoRDStation());
					sqlAlterar.setString(50, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
					sqlAlterar.setInt(51, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					return arg0.next();
				}
			})) {
				incluirSemValidarDados(obj, verificarAcesso, usuario, configuracaoGeralSistema);
				return;
			}
			getFacadeFactory().getCursoInteresseFacade().alterarCursoInteresses(obj.getCodigo(), obj.getCursoInteresseVOs(), usuario);
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicasProspects(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), false, usuario);
			getFacadeFactory().getPessoaFacade().alterarPessoaConformeProspect(obj, verificarAcesso, usuario, configuracaoGeralSistema, false, origemPreInscricao);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarAlteracaoCompromissoAoAlterarConsultor(obj, usuario);
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@SuppressWarnings("static-access")
	@Override
	public void alterarProspectConformePessoa(final PessoaVO obj, boolean verificarAcesso, final UsuarioVO usuario) throws Exception {
		if (obj != null && obj.getCodigo() != null && obj.getCodigo() > 0) {
			try {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("SELECT codigo from Prospects where pessoa = " + obj.getCodigo());
			if (rs.next()) {
				Integer codigoProspect = rs.getInt("codigo");
				// //// AKI ///////
				final String sql = " UPDATE Prospects set nome=?, endereco=?, setor=?, numero=?, CEP=?, complemento=?, cidade=?, sexo=?, estadoCivil=?, telefoneComercial=?, " + " telefoneResidencial=?, telefoneRecado=?, celular=?, emailPrincipal=?, dataNascimento=?, naturalidade=?, nacionalidade=?, CPF=?, RG=?, " + " dataExpedicao=?, estadoEmissor=?, orgaoEmissor=?, arquivoFoto=?, " + " emailSecundario=?, usuarioAlteracao=?, dataAlteracao=?, nomeEmpresa = ?, cargo = ?, sincronizadoRdStation=false, tipomidia=?, nomeBatismo=? " + " WHERE ((pessoa = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

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
						sqlAlterar.setString(x++, obj.getSexo());
						sqlAlterar.setString(x++, obj.getEstadoCivil());
						sqlAlterar.setString(x++, obj.getTelefoneComer());
						sqlAlterar.setString(x++, obj.getTelefoneRes());
						sqlAlterar.setString(x++, obj.getTelefoneRecado());
						sqlAlterar.setString(x++, obj.getCelular());
						sqlAlterar.setString(x++, obj.getEmail().trim());
						sqlAlterar.setDate(x++, Uteis.getDataJDBC(obj.getDataNasc()));
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
						sqlAlterar.setString(x++, obj.getCPF());
						sqlAlterar.setString(x++, obj.getRG());
						sqlAlterar.setDate(x++, Uteis.getDataJDBC(obj.getDataEmissaoRG()));
						sqlAlterar.setString(x++, obj.getEstadoEmissaoRG());
						sqlAlterar.setString(x++, obj.getOrgaoEmissor());
						if (obj.getArquivoImagem().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(x++, obj.getArquivoImagem().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(x++, 0);
						}
						sqlAlterar.setString(x++, obj.getEmail2().trim());
						if (usuario != null && usuario.getCodigo() != 0) {
							sqlAlterar.setInt(x++, usuario.getCodigo().intValue());
						} else {
							sqlAlterar.setNull(x++, 0);
						}
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
						sqlAlterar.setString(x++, obj.getNomeEmpresa());
						sqlAlterar.setString(x++, obj.getCargoPessoaEmpresa());
						if (obj.getTipoMidiaCaptacao() != null && obj.getTipoMidiaCaptacao().getCodigo() != null && obj.getTipoMidiaCaptacao().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(x++, obj.getTipoMidiaCaptacao().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(x++, 0);
						}
						
						sqlAlterar.setString(x++, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
						sqlAlterar.setInt(x++, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				}) > 0) {
					if (obj.getFormacaoAcademicaVOs().isEmpty()) {
						List listaFormacao = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoProspect(codigoProspect, false, usuario);
						obj.setFormacaoAcademicaVOs(listaFormacao);
					}
					getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicasProspects(codigoProspect, obj.getFormacaoAcademicaVOs(), verificarAcesso, usuario);
				}
			}
			}catch (Exception e) {
				tratarMensagemExcecaoProspects(e, obj.getCPF(), obj.getCodigo());
				throw e;
		}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {
			verificarProspectComPessoaPreenchidoBaseELimpoObjeto(obj, obj.getPessoa());
			if(Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
				if(!Uteis.isAtributoPreenchido(obj.getPessoa().getCPF()) && !Uteis.isAtributoPreenchido(obj.getEmailPrincipal())){
					SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet("select cpf, email from pessoa where codigo = ? ", obj.getPessoa().getCodigo());
					if(rs.next()) {
						obj.getPessoa().setCPF(rs.getString("cpf"));
						obj.getPessoa().setEmail(rs.getString("email"));
					}else {
						obj.setPessoa(new PessoaVO());
					}				
				}
				if(Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {	
					String[] emails = obj.getEmailPrincipal().split(";");
					List<String> emailsPrincipal = new ArrayList<String>();
					for(String email : emails) {
						if(Uteis.isAtributoPreenchido(email) && !emailsPrincipal.contains(email.toLowerCase())) {
							emailsPrincipal.add(email.toLowerCase());
						}
					}
					emails = obj.getEmailSecundario().split(";");
					for(String email : emails) {
						if(Uteis.isAtributoPreenchido(email) && !emailsPrincipal.contains(email.toLowerCase())) {
							emailsPrincipal.add(email.toLowerCase());
						}
					}
					
				if((!Uteis.isAtributoPreenchido(obj.getCpf().replace(".", "").replace("-", "")) && !Uteis.isAtributoPreenchido(obj.getEmailPrincipal()) && !Uteis.isAtributoPreenchido(obj.getEmailSecundario()))
							|| (Uteis.isAtributoPreenchido(obj.getCpf().replace(".", "").replace("-", "")) && !(obj.getCpf().replace(".", "").replace("-", "")).equals((obj.getPessoa().getCPF().replace(".", "").replace("-", ""))))
							|| (Uteis.isAtributoPreenchido(emailsPrincipal) && !Uteis.isAtributoPreenchido(obj.getCpf().replace(".", "").replace("-", "")) && !emailsPrincipal.contains(obj.getPessoa().getEmail().toLowerCase()))
							|| (Uteis.isAtributoPreenchido(emailsPrincipal) && !Uteis.isAtributoPreenchido(obj.getCpf().replace(".", "").replace("-", "")) && !emailsPrincipal.contains(obj.getPessoa().getEmail().toLowerCase()))
							) {
					obj.setPessoa(new PessoaVO());
				}
				}
			}
			final String sql = "UPDATE Prospects set pessoa=?, sincronizadoRdStation=false WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void unificarProspects(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuarioVO) throws Exception {
		try {
			if (verificarProspectMatriculado(codProspectRemover)) {
				ProspectsVO prospectsVO = consultarPorChavePrimaria(codProspectRemover, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				alterarPessoaProspectParaNull(codProspectRemover, usuarioVO);
//				throw new Exception("Foi encontrado um prospect com o mesmo CPF ou E-mail (Prospect: " + prospectsVO.getNome() + " CPF: " + prospectsVO.getCpf() + " E-mail: " + prospectsVO.getEmailPrincipal()  + (Uteis.isAtributoPreenchido(prospectsVO.getEmailSecundario()) ? "/"  + prospectsVO.getEmailSecundario() : "") +") do que este aluno e esse prospect a ser removido possui matrícula vinculada. Entre em contato com suporte para realizar a unificação!");
			}
			getFacadeFactory().getCursoInteresseFacade().alteraCursoInteresse(codProspectManter, codProspectRemover, usuarioVO);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().alteratProspectCompromisso(codProspectManter, codProspectRemover, usuarioVO);
			getFacadeFactory().getInteracaoWorkflowFacade().alteratProspectInteracao(codProspectManter, codProspectRemover, usuarioVO);
			getFacadeFactory().getPreInscricaoFacade().alterarProspectPreInscricao(codProspectManter, codProspectRemover);
			getFacadeFactory().getHistoricoFollowUpFacade().alterarProspectHistoricoFollowUp(codProspectManter, codProspectRemover, usuarioVO);
			getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().alterarProspectCompromissoCampanhaPublicoAlvoProspect(codProspectManter, codProspectRemover, usuarioVO);
			excluirProspectsRegistroEntrada(codProspectRemover, usuarioVO);
			getFacadeFactory().getCursoInteresseFacade().excluirCursoInteresses(codProspectRemover, usuarioVO);
			final String sql = "DELETE from Prospects where codigo = ?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { codProspectRemover });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarConsultorProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {

			final String sql = "UPDATE Prospects set consultorPadrao=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getConsultorPadrao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getConsultorPadrao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			obj.setConsultorAlterado(true);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarAlteracaoCompromissoAoAlterarConsultor(obj, usuario);
			obj.setConsultorAlterado(false);
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarUnidadeEnsinoProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Prospects set unidadeEnsino=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarUnidadeEnsinoEConsultorProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getConsultorPadrao().getCodigo().intValue() != 0 && obj.getUnidadeEnsino().getCodigo() > 0) {
				obj.setConsultorPadrao(getFacadeFactory().getFuncionarioFacade().consultarFuncionarioEConsultorDaUnidade(obj.getUnidadeEnsino().getCodigo(), obj.getConsultorPadrao().getCodigo()));
			}
			if (obj.getUnidadeEnsino().getCodigo() == 0) {
				obj.setConsultorPadrao(null);
			}
			final String sql = "UPDATE Prospects set unidadeEnsino=?, consultorPadrao=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getConsultorPadrao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getConsultorPadrao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarAlteracaoCompromissoAoAlterarConsultor(obj, usuario);

		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProspectTelaFollowUp(final ProspectsVO obj, final UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			// //// AKI ///////
			final String sql = "UPDATE Prospects set nome=?, emailPrincipal=?, dataNascimento=?, sexo=?, telefoneResidencial=?, celular=?, telefoneComercial=?, telefoneRecado=?, dataAlteracao=?, usuarioAlteracao = ?, sincronizadoRdStation=false, nomeBatismo = ?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, Uteis.removeCaractersEspeciais2(obj.getNome()));
					sqlAlterar.setString(2, obj.getEmailPrincipal().trim());
					sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataNascimento()));
					sqlAlterar.setString(4, obj.getSexo());
					sqlAlterar.setString(5, obj.getTelefoneResidencial());
					sqlAlterar.setString(6, obj.getCelular());
					sqlAlterar.setString(7, obj.getTelefoneComercial());
					sqlAlterar.setString(8, obj.getTelefoneRecado());
					sqlAlterar.setDate(9, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(10, usuarioVO.getCodigo());
					sqlAlterar.setString(11, Uteis.removeCaractersEspeciais2(obj.getNomeBatismo()));
					sqlAlterar.setInt(12, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getCursoInteresseFacade().alterarCursoInteresses(obj.getCodigo(), obj.getCursoInteresseVOs(), usuarioVO);
			if (obj.getPessoa() != null && obj.getPessoa().getCodigo() != null && obj.getPessoa().getCodigo() > 0) {
				if (configuracaoGeralSistema.getUtilizarCaixaAltaNomePessoa()) {
					obj.setNome(obj.getNome().toUpperCase());
				}
				getFacadeFactory().getPessoaFacade().inicializarDadosPessoaBaseadoProspect(obj, usuarioVO);
				getFacadeFactory().getPessoaFacade().validarDadosPessoaCasoPossuaMatricula(obj.getPessoa(), false, usuarioVO);
				getFacadeFactory().getPessoaFacade().alterarPessoaPelaTelaDeFollowUpProspect(obj, usuarioVO);
				
			}
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProspectsVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProspectsVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.isNovoObj()) {
				throw new Exception(UteisJSF.internacionalizar("msg_registroNaoGravado"));
			}
			Prospects.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			excluirProspectsRegistroEntrada(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getCursoInteresseFacade().excluirCursoInteresses(obj.getCodigo(), usuarioVO);
			String sql = "DELETE FROM Prospects WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirProspectERegistrosReferenciados(ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			Prospects.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			excluirProspectsRegistroEntrada(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getCursoInteresseFacade().excluirCursoInteresses(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getPessoaFacade().alterarCodProspect(obj.getCodigo(), null, false);
			getFacadeFactory().getInteracaoWorkflowFacade().excluirInteracaoProspects(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluirCompromissoAgendaPessoaHorario(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getCompromissoCampanhaPublicoAlvoProspectFacade().excluirCompromissoCampanhaPublicoAlvoPorCampanhaProspect(0, obj.getCodigo(), usuarioVO);
			getFacadeFactory().getHistoricoFollowUpFacade().excluirHistoricoFollowUps(obj.getCodigo(), usuarioVO);
			String sql = "DELETE FROM Prospects WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param ProspectsVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluirComValidacaoDados(obj, verificarAcesso, usuario, configuracaoGeralSistema);
		} else {
			alterarComValidarDados(obj, verificarAcesso, usuario, configuracaoGeralSistema);
		}
	}

	@Override
	public void realizarCriacaoProspectSegmentacaoOpcao(ProspectsVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs, UsuarioVO usuarioVO) {
		obj.getProspectSegmentacaoOpcaoVOs().clear();
		for (SegmentacaoProspectVO segmentacaoProspectVO : segmentacaoProspectVOs) {
			if (!segmentacaoProspectVO.getSegmentacaoOpcao().getCodigo().equals(0)) {
				ProspectSegmentacaoOpcaoVO prospectSegmentacao = new ProspectSegmentacaoOpcaoVO();
				prospectSegmentacao.setProspect(obj);
				prospectSegmentacao.getSegmentacaoOpcao().setCodigo(segmentacaoProspectVO.getSegmentacaoOpcao().getCodigo());
				obj.getProspectSegmentacaoOpcaoVOs().add(prospectSegmentacao);
			}
		}
	}

	@Override
	public void realizarRecuperacaoProspectSegmentacaoOpcao(ProspectsVO obj, List<SegmentacaoProspectVO> segmentacaoProspectVOs, UsuarioVO usuarioVO) {
		for (SegmentacaoProspectVO segmentacaoProspectVO : segmentacaoProspectVOs) {
			for (SegmentacaoOpcaoVO segmentacaoOpcaoVO : segmentacaoProspectVO.getSegmentacaoOpcaoVOs()) {
				for (ProspectSegmentacaoOpcaoVO prospectSegmentacao : obj.getProspectSegmentacaoOpcaoVOs()) {
					if (prospectSegmentacao.getSegmentacaoOpcao().getCodigo().equals(segmentacaoOpcaoVO.getCodigo())) {
						segmentacaoProspectVO.setSegmentacaoOpcao(segmentacaoOpcaoVO);
						continue;
					}
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirRegistroProspectRapido(ProspectsVO obj, ProspectsVO indicadoPeloProspect, CampanhaVO campanha, UsuarioVO vendedor, Boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		validarDadosRegistroRapido(obj);
		if (obj.getCodigo() == 0) {
			incluirSemValidarDados(obj, verificarAcesso, usuario, configuracaoGeralSistema);
			// RegistroEntradaProspectsVO registroEntradaProspects = new
			// RegistroEntradaProspectsVO();
			// registroEntradaProspects.setProspects(obj);
			// registroEntradaProspects.setIndicadoPeloProspect(indicadoPeloProspect);
			// registroEntradaProspects.setCampanha(campanha);
			// registroEntradaProspects.setVendedor(vendedor);
			// registroEntradaProspects.setDataIndicacaoProspect(new Date());
			// getFacadeFactory().getRegistroEntradaProspectsFacade().incluir(registroEntradaProspects,
			// verificarAcesso, usuario);
		} else {
			alterarSemValidarDados(obj, verificarAcesso, usuario, configuracaoGeralSistema, false);
		}
	}

	public void realizarAtualizacaoTelefonePessoa(ProspectsVO prospect, UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().update("UPDATE pessoa SET telefoneres = '" + prospect.getTelefoneResidencial() + "', telefoneRecado = '" + prospect.getTelefoneRecado() + "', celular = '" + prospect.getCelular() + "', telefoneComer = '" + prospect.getTelefoneComercial() + "' where codigo = " + prospect.getPessoa().getCodigo()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}

	public void realizarAtualizacaoTelefoneUnidadeEnsino(ProspectsVO prospect, UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().update("UPDATE unidadeensino SET telcomercial1 = '" + prospect.getTelefoneComercial() + "' where codigo = " + prospect.getUnidadeEnsinoProspect().getCodigo()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}

	public void realizarAtualizacaoTelefoneParceiro(ProspectsVO prospect, UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().update("UPDATE parceiro SET telcomercial1 = '" + prospect.getTelefoneComercial() + "' where codigo = " + prospect.getParceiro().getCodigo()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}

	public void realizarAtualizacaoTelefoneFornecedor(ProspectsVO prospect, UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().update("UPDATE fornecedor SET fornecedor.comercial1 = '" + prospect.getTelefoneComercial() + "' where codigo = " + prospect.getFornecedor().getCodigo()+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ProspectsVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(ProspectsVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (!obj.getEmailPrincipal().trim().equals("") && validarUnicidadeEmailProspect(obj.getEmailPrincipal(), obj.getCodigo())) {
			throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailPrincipal());
		}
		if (!obj.getEmailSecundario().trim().equals("") && validarUnicidadeEmailProspect(obj.getEmailSecundario(), obj.getCodigo())) {
			throw new Exception("Já existe um prospect cadastrado no sistema com esse EMAIL:" + obj.getEmailSecundario());
		}
		// if (validarUnicidadeEmailNomeProspect(obj.getEmailPrincipal(),
		// obj.getNome()) && obj.isNovoObj()) {
		// throw new
		// Exception("Já existe um prospect cadastrado no sistema com essa combinação (NOME/EMAIL)");
		// }
		if (obj.getNome().trim().equals("")) {
			throw new Exception("O nome social deve ser informado.");
		}
		if (obj.getNomeBatismo().equals("")) {
			throw new Exception("O nome de batismo deve ser informado.");
		}
		if (obj.getTelefoneComercial().isEmpty() && obj.getCelular().isEmpty() && obj.getTelefoneResidencial().isEmpty() && obj.getTelefoneRecado().isEmpty() && obj.getEmailPrincipal().isEmpty()) {
			throw new Exception("Deve ser informado um telefone ou email.");
		}
		// if (obj.getEmailPrincipal().isEmpty()) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_campoEmailPrincipalDeveSerInformado"));
		// }
		if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
		}
		// if ((obj.getTipoMidia() == null) ||
		// (obj.getTipoMidia().getCodigo().intValue() == 0)) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_campoTipoMidiaDeveSerInformado"));
		// }
	}

	public void validarDadosPreInscricao(ProspectsVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getCpf().equals("") && !configuracaoGeralSistemaVO.getOcultarCPFPreInscricao()) {
			throw new Exception("O campo CPF (Pré-Inscrição) deve ser informado.");
		}
		
		if (obj.getTipoMidia().getCodigo().intValue() == 0 && configuracaoGeralSistemaVO.getObrigarTipoMidiaProspect() == true) {
			throw new Exception("O campo Como Ficou Sabendo da Instituição (Pré-Inscrição) deve ser informado.");
		}

		if (obj.getNome().equals("")) {
			throw new Exception("O campo Nome (Prospect - Pré-Inscrição) deve ser informado.");
		}
		if (obj.getEmailPrincipal().trim().equals("")) {
			throw new Exception("O campo E-mail (Pré-Inscrição) deve ser informado.");
		}
		
		if(obj.getTelefoneComercial() != null && obj.getTelefoneComercial().length() > 15) {
			throw new Exception("O campo Telefone Comercial (Pré-Inscrição) é inválido ou possui muitos campos em branco.");
		}
		
		if(obj.getTelefoneResidencial() != null && obj.getTelefoneResidencial().length() > 15) {
			throw new Exception("O campo Telefone Residencial (Pré-Inscrição) é inválido ou possui muitos campos em branco.");
		}
		
		if(obj.getCelular() != null && obj.getCelular().length() > 15) {
			throw new Exception("O campo Celular (Pré-Inscrição) é inválido ou possui muitos campos em branco.");
		}
		
		if (configuracaoGeralSistemaVO.getTodosCamposObrigatoriosPreInscricao()) {
			if (obj.getTelefoneResidencial().equals("") && obj.getCelular().equals("") && obj.getTelefoneComercial().equals("")) {
				throw new Exception("O campo Telefone Residencial, Celular ou Telefone Comercial (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getDataNascimento() == null) {
				throw new Exception("O campo Data Nascimento (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getSexo().equals("")) {
				throw new Exception("O campo Sexo (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getRg().equals("")) {
				throw new Exception("O campo RG (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getDataExpedicao() == null) {
				throw new Exception("O campo Data Emissão RG (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getOrgaoEmissor().equals("")) {
				throw new Exception("O campo Orgão Emissor (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getEstadoEmissor().equals("")) {
				throw new Exception("O campo Estado Emissor (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getEstadoCivil().equals("")) {
				throw new Exception("O campo Estado Cicil (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getNaturalidade().getCodigo().intValue() == 0) {
				throw new Exception("O campo Naturalidade (Pré-Inscrição) deve ser informado.");
			}
			if (obj.getTipoMidia().getCodigo().intValue() == 0) {
				throw new Exception("O campo Como Ficou Sabendo da Instituição (Pré-Inscrição) deve ser informado.");
			}
		}
		if (!configuracaoGeralSistemaVO.getOcultarCPFPreInscricao() && configuracaoGeralSistemaVO.getTodosCamposObrigatoriosPreInscricao()) {
			if (obj.getCEP().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("O campo CEP (Pré-Inscrição) deve ser informado."));
			}
			if (obj.getEndereco().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("O campo Endereço (Pré-Inscrição) deve ser informado."));
			}
			if (obj.getSetor().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("O campo Setor (Pré-Inscrição) deve ser informado."));
			}
			if ((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0)) {
				throw new Exception(UteisJSF.internacionalizar("O campo Cidade (Pré-Inscrição) deve ser informado."));
			}
		}
	}

	public void validarDadosRegistroRapido(ProspectsVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_campoNomeDeveSerInformado"));
		}
		// if (obj.getTelefoneComercial().equals("") &&
		// obj.getCelular().equals("") &&
		// obj.getTelefoneResidencial().equals("") &&
		// obj.getTelefoneRecado().equals("")) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_campoTelefoneDeveSerInformado"));
		// }
		if (obj.getTelefoneComercial().isEmpty() && obj.getCelular().isEmpty() && obj.getTelefoneResidencial().isEmpty() && obj.getTelefoneRecado().isEmpty() && obj.getEmailPrincipal().isEmpty()) {
			throw new Exception("Deve ser informado um telefone ou email.");
		}
	}

	public Boolean isValido(ProspectsVO obj) throws Exception {
		if (obj.getNome().equals("")) {
			return false;
		}
		if (obj.getTelefoneComercial().equals("") && obj.getCelular().equals("") && obj.getTelefoneResidencial().equals("") && obj.getTelefoneRecado().equals("")) {
			return false;
		}
		return true;
	}

	public void verificarProspectComPessoaPreenchidoBaseELimpoObjeto(ProspectsVO prospect, PessoaVO pessoa) throws Exception {
		if (pessoa.getCodigo().equals(0)) {
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("select pessoa from prospects where codigo = ").append(prospect.getCodigo()).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			sqlStr = null;
			if (tabelaResultado.next()) {
				Integer codpessoa = new Integer(tabelaResultado.getInt("pessoa"));
				if (codpessoa > 0) {
					pessoa.setCodigo(codpessoa);
					////System.out.print("ERRO DE PERDA DO VINCULO DO PROSPECT COM A PESSOA => " + prospect.getCodigo() + " " + pessoa.getCodigo());
					// throw new
					// Exception("O Vinculo do Prospect com a Pessoa está sendo alterado, provavelmente o vinculo será perdido, entre em contato com o suporte relatando o problema!");
				}
			}
		}
	}

	public List<ProspectsVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
//		sqlStr.append("select distinct prospects.codigo, prospects.nome, position (upper(sem_acentos('").append(valorConsulta).append("')) in upper(sem_acentos(prospects.nome))) ");
//		sqlStr.append("from prospects ");
		sqlStr.append("WHERE sem_acentos(prospects.nome) ilike(sem_acentos('%");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%')) ");
		if (unidadeEnsino > 0) {
			sqlStr.append(" AND (prospects.unidadeEnsino = " + unidadeEnsino + " OR prospects.unidadeEnsino IS NULL)");
		}
		sqlStr.append(" ORDER BY position (upper(sem_acentos('").append(valorConsulta).append("')) in upper(sem_acentos(prospects.nome))), prospects.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		sqlStr = null;
		List<ProspectsVO> listaProspects = new ArrayList<ProspectsVO>();
		while (tabelaResultado.next()) {
			ProspectsVO obj = new ProspectsVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("prospects.codigo")));
			obj.setNome(tabelaResultado.getString("prospects.nome"));
			listaProspects.add(obj);
		}
		return listaProspects;
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>ProspectsVO</code>.
	 */
	public void validarUnicidade(List<ProspectsVO> lista, ProspectsVO obj) throws ConsistirException {
		for (ProspectsVO repetido : lista) {
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(ProspectsVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setNome(obj.getNome().toUpperCase());
		obj.setRazaoSocial(obj.getRazaoSocial().toUpperCase());
		obj.setCpf(obj.getCpf().toUpperCase());
		obj.setRg(obj.getRg().toUpperCase());
		obj.setSexo(obj.getSexo().toUpperCase());
		obj.setCnpj(obj.getCnpj().toUpperCase());
		obj.setInscricaoEstadual(obj.getInscricaoEstadual().toUpperCase());
		obj.setCEP(obj.getCEP().toUpperCase());
		obj.setEndereco(obj.getEndereco().toUpperCase());
		obj.setComplemento(obj.getComplemento().toUpperCase());
		obj.setSetor(obj.getSetor().toUpperCase());
		obj.setTelefoneComercial(obj.getTelefoneComercial().toUpperCase());
		obj.setTelefoneResidencial(obj.getTelefoneResidencial().toUpperCase());
		obj.setTelefoneRecado(obj.getTelefoneRecado().toUpperCase());
		obj.setCelular(obj.getCelular().toUpperCase());
		obj.setEstadoEmissor(obj.getEstadoEmissor().toUpperCase());
		obj.setOrgaoEmissor(obj.getOrgaoEmissor().toUpperCase());

	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * ProspectsCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public List<ProspectsVO> consultar(String valorConsulta, Integer unidadeEnsino, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.NOME.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getProspectsFacade().consultarPorNome(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.EMAIL.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getProspectsFacade().consultarPorEmailPrincipalSecundario(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.CURSO_INTERESSE.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return consultarPorCursoInteresse(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.RAZAO_SOCIAL.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getProspectsFacade().consultarPorRazaoSocial(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.CPF.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getProspectsFacade().consultarPorCpf(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.CNPJ.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getProspectsFacade().consultarPorCnpj(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		if (campoConsulta.equals(TipoConsultaComboProspectsEnum.NOME_UNIDADE_ENSINO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			}
			return getFacadeFactory().getProspectsFacade().consultarPorNomeUnidadeEnsino(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, tipoFiltroConsulta);
		}
		return new ArrayList(0);
	}

	public ProspectsVO consultarPorCPFCNPJUnico(ProspectsVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		validarTipoProspectCpfCnpj(obj);
		if (obj.getJuridico()) {
			String sqlStr = "SELECT prospects.* FROM Prospects WHERE replace(replace(replace(CNPJ,'.',''),'-',''),'/','') = '" + Uteis.retirarMascaraCNPJ(obj.getCnpj()) + "' ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (!tabelaResultado.next()) {
				return new ProspectsVO();
			}
			return montarDadosCpfCnpjUnico(tabelaResultado, nivelMontarDados, usuario);
		} else {
			String sqlStr = "SELECT prospects.* FROM Prospects WHERE replace(replace(CPF,'.',''),'-','') = '" + Uteis.retirarMascaraCPF(obj.getCpf()) + "' order by pessoa ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (!tabelaResultado.next()) {
				return new ProspectsVO();
			}
			return montarDadosCpfCnpjUnico(tabelaResultado, nivelMontarDados, usuario);
		}
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT prospects.codigo  AS \"prospects.codigo\", prospects.consultorPadrao AS \"prospects.consultorPadrao\", prospects.cpf AS \"prospects.cpf\",  prospects.rg AS \"prospects.rg\",prospects.estadoEmissor AS \"prospects.estadoEmissor\",prospects.orgaoEmissor AS \"prospects.orgaoEmissor\", prospects.tipoProspect AS \"prospects.tipoProspect\", prospects.cnpj AS \"prospects.cnpj\", prospects.nome AS \"prospects.nome\", prospects.pessoa AS \"prospects.pessoa\",");
		str.append("prospects.razaosocial AS \"prospects.razaosocial\", prospects.inativo AS \"prospects.inativo\", prospects.motivoInativacao AS \"prospects.motivoInativacao\", prospects.emailprincipal AS \"prospects.emailprincipal\", prospects.emailsecundario AS \"prospects.emailsecundario\", prospects.telefoneresidencial AS \"prospects.telefoneresidencial\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", prospects.tipomidia AS \"prospects.tipomidia\",sincronizadordstation, prospects.nomeBatismo AS \"prospects.nomeBatismo\"  FROM prospects ");
		str.append("LEFT JOIN unidadeensino on unidadeEnsino.codigo = prospects.unidadeEnsino ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		// Dados prospects
		str.append("SELECT prospects.codigo, prospects.nome, prospects.razaosocial, prospects.cpf, prospects.cnpj, prospects.rg,prospects.estadoEmissor ,prospects.orgaoEmissor ,  ");
		str.append("prospects.dataNascimento, prospects.sexo, prospects.inscricaoEstadual, prospects.endereco, prospects.emailPrincipal, prospects.emailSecundario, prospects.renda,");
		str.append("prospects.skype, prospects.celular, prospects.telefoneComercial, prospects.telefoneResidencial, prospects.telefoneRecado, prospects.setor, prospects.complemento,");
		str.append("prospects.numero, prospects.estadoCivil, prospects.dataExpedicao, prospects.nomePai, prospects.nomeMae, prospects.participaBancoCurriculum, prospects.divulgarMeusDados, ");
		str.append("prospects.formacaoAcademica, prospects.tipoProspect, prospects.cep, prospects.tipoEmpresa, re.descricao AS \"re.descricao\", re.dataEntrada AS \"re.dataEntrada\", re.codigo AS \"re.codigo\",");
		str.append("re.unidadeEnsino AS \"re.unidadeEnsino\", unidadeEnsinoRegistroEntrada.nome AS \"unidadeEnsinoRegistroEntrada.nome\", ");
		str.append("prospects.pessoa, prospects.fornecedor, prospects.parceiro, prospects.unidadeensinoprospect, prospects.nomeEmpresa, prospects.cargo, prospects.telefoneEmpresa, prospects.curso, ConsultorPadrao.nome AS nomeConsultorPadrao, ConsultorPadrao.codigo as codigoPessoaConsultorPadrao, funcionario.codigo AS codigoConsultorPadrao,  ");

		str.append("prospects.dataCadastro, prospects.responsavelCadastro, prospects.tipoOrigemCadastro, prospects.inativo, prospects.motivoInativacao, prospects.responsavelInativacao, prospects.nomeBatismo, ");
		// Dados usuario responsavel cadastro
		str.append("usuarioResponsavelCadastro.nome AS \"usuarioResponsavelCadastro.nome\", ");
		// Dados usuario responsavel inativacao
		str.append("usuarioResponsavelInativacao.nome AS \"usuarioResponsavelInativacao.nome\", ");

		// Dados cidade
		str.append(" cidade.nome AS \"cidade.nome\", cidade.codigo AS \"cidade.codigo\", ");
		// Dados Naturalidade
		str.append(" naturalidade.nome AS \"naturalidade.nome\", naturalidade.codigo AS \"naturalidade.codigo\", ");
		// Dados Nacionalidade
		str.append(" nacionalidade.nome AS \"nacionalidade.nome\", nacionalidade.codigo AS \"nacionalidade.codigo\", ");
		// Dados arquivo
		str.append(" arquivo.pastaBaseArquivo, arquivo.codigo AS codArquivo, arquivo.nome AS nomeArquivo, ");
		// Dados unidade Ensino
		str.append(" unEnsino.codigo AS \"unidadeEnsino.codigo\" , unEnsino.nome AS \"unidadeEnsino.nome\", ");
		// Dados curso interesse
		str.append(" cursoInteresse.codigo AS \"cursoInteresse.codigo\" , cursoInteresse.dataCadastro AS \"cursoInteresse.dataCadastro\", ");
		str.append(" curso.codigo AS \"curso.codigo\" , curso.nome AS \"curso.nome\", ");
		// Dados Formação Acadêmica
		str.append("formacaoAcademica.codigo AS \"formacaoAcademica.codigo\", formacaoAcademica.instituicao AS \"formacaoAcademica.instituicao\", formacaoAcademica.tipoInst AS \"formacaoAcademica.tipoInst\", ");
		str.append("formacaoAcademica.escolaridade AS \"formacaoAcademica.escolaridade\", formacaoAcademica.curso AS \"formacaoAcademica.curso\", ");
		str.append("formacaoAcademica.situacao AS \"formacaoAcademica.situacao\", formacaoAcademica.dataInicio AS \"formacaoAcademica.dataInicio\", ");
		str.append("formacaoAcademica.dataFim AS \"formacaoAcademica.dataFim\",  formacaoAcademica.pessoa AS \"formacaoAcademica.pessoa\", ");
		str.append("formacaoAcademica.anoDataFim AS \"formacaoAcademica.anoDataFim\",  formacaoAcademica.semestreDataFim AS \"formacaoAcademica.semestreDataFim\", ");
		// Dados Area Conhecimento
		str.append("areaConhecimento.codigo AS \"formacaoAcademica.areaConhecimento.codigo\", areaConhecimento.nome AS \"formacaoAcademica.areaConhecimento.nome\", ");
		str.append("cidadeFormacao.codigo AS \"formacaoAcademica.cidade.codigo\", cidadeFormacao.nome AS \"formacaoAcademica.cidade.nome\", ");
		str.append("estadoFormacao.codigo AS \"formacaoAcademica.estado.codigo\", estadoFormacao.nome AS \"formacaoAcademica.estado.nome\", ");
		str.append("estadoFormacao.sigla AS \"formacaoAcademica.estado.sigla\", estadoFormacao.paiz AS \"formacaoAcademica.estado.paiz\", ");
		// Dados pessoa
		str.append("pe.aluno AS isAluno, pe.professor AS isProfessor, pe.funcionario AS isFuncionario, ");
		// Dados Tipo Midia
		str.append("tm.codigo AS \"tm.codigo\", tm.nomeMidia AS \"tm.nomeMidia\", tm.descricaoMidia AS \"tm.descricaoMidia\", ");
		// Dados Opcoes de Segmentações
		str.append("pso.segmentacaoopcao, pso.codigo AS \"pso.codigo\", ");
		str.append("estado.sigla as estado_sigla, ");
		str.append("naturalidade_estado.sigla as naturalidade_estado_sigla ");
		str.append(" FROM prospects ");

		str.append(" LEFT JOIN cidade ON cidade.codigo = prospects.cidade ");
		str.append(" LEFT JOIN estado ON cidade.estado = estado.codigo ");
		str.append(" LEFT JOIN cursoInteresse ON cursoInteresse.prospects = prospects.codigo ");
		str.append(" LEFT JOIN unidadeEnsino AS unEnsino ON prospects.unidadeEnsino =  unEnsino.codigo ");
		str.append(" LEFT JOIN curso ON cursoInteresse.curso =  curso.codigo ");
		str.append(" LEFT JOIN arquivo ON prospects.arquivoFoto = arquivo.codigo ");
		str.append(" LEFT JOIN registroEntradaProspects AS rep ON rep.prospects = prospects.codigo");
		str.append(" LEFT JOIN registroEntrada AS re ON rep.registroEntrada = re.codigo");
		str.append(" LEFT JOIN unidadeEnsino AS unidadeEnsinoRegistroEntrada ON unidadeEnsinoRegistroEntrada.codigo = re.unidadeEnsino");
		str.append(" LEFT JOIN pessoa AS pe ON pe.codigo = prospects.pessoa");
		str.append(" LEFT JOIN funcionario ON funcionario.codigo = prospects.consultorPadrao");
		str.append(" LEFT JOIN pessoa AS consultorPadrao ON consultorPadrao.codigo = funcionario.pessoa");
		str.append(" LEFT JOIN paiz AS nacionalidade ON nacionalidade.codigo = prospects.nacionalidade ");
		str.append(" LEFT JOIN cidade AS naturalidade ON naturalidade.codigo = prospects.naturalidade ");
		str.append(" LEFT JOIN estado AS naturalidade_estado ON naturalidade_estado.codigo = naturalidade.estado ");
		str.append(" LEFT JOIN formacaoAcademica ON formacaoAcademica.prospects = prospects.codigo ");
		str.append(" LEFT JOIN areaConhecimento ON formacaoAcademica.areaConhecimento = areaConhecimento.codigo ");
		str.append(" LEFT JOIN cidade cidadeFormacao ON formacaoAcademica.cidade = cidadeFormacao.codigo ");
		str.append(" LEFT JOIN estado estadoFormacao ON estadoFormacao.codigo = cidadeFormacao.estado ");
		str.append(" LEFT JOIN usuario AS usuarioResponsavelCadastro ON prospects.responsavelCadastro = usuarioResponsavelCadastro.codigo ");
		str.append(" LEFT JOIN usuario AS usuarioResponsavelInativacao ON prospects.responsavelInativacao = usuarioResponsavelInativacao.codigo ");
		str.append(" LEFT JOIN prospectsegmentacaoopcao AS pso on pso.prospect = prospects.codigo ");
		str.append(" LEFT JOIN tipoMidiaCaptacao AS tm on tm.codigo = prospects.tipoMidia ");

		return str;
	}

	/**
	 * Responsável por realizar uma consulta de <code>Prospects</code> através
	 * do valor do atributo <code>nome</code> da classe
	 * <code>UnidadeEnsino</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProspectsVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE Prospects.unidadeEnsino = UnidadeEnsino.codigo and upper(sem_acentos( UnidadeEnsino.nome )) ilike(sem_acentos(?)) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND prospects.unidadeEnsino = ").append(unidadeEnsino);
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
				sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append(" or prospects.consultorpadrao is null) ");
			}
		}
		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}
		sqlStr.append(" ORDER BY UnidadeEnsino.nome");

		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase() + "%"), nivelMontarDados, "prospects.", usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Prospects</code> através
	 * do valor do atributo <code>String cnpj</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProspectsVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCnpj(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE upper( prospects.cnpj ) like(?) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND prospects.unidadeEnsino = ").append(unidadeEnsino);
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
				sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append("  or prospects.consultorpadrao is null) ");
			}
		}
		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}
		sqlStr.append(" ORDER BY prospects.cnpj");

		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase() + "%"), nivelMontarDados, "prospects.", usuario));
	}

	public ProspectsVO consultarPorEmail(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		ProspectsVO prospectsVO = new ProspectsVO();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT codigo, nome, telefoneresidencial, emailprincipal, nomeBatismo FROM prospects WHERE lower(sem_acentos(prospects.emailPrincipal))  ilike(sem_acentos('%").append(valorConsulta).append("%')) limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return prospectsVO;
		}
		return (montarDadosParaRegistroEntradaProspects(tabelaResultado, usuario));
	}

	public ProspectsVO consultarPorEmailUnico(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		ProspectsVO prospectsVO = new ProspectsVO();
		StringBuffer sqlStr = new StringBuffer();
		String[] emails = valorConsulta.split(";");
		for(String email: emails) {
			if(sqlStr.length() > 0) {
				sqlStr.append(" union ");	
			}
			sqlStr.append("select  * from (select codigo, nome, telefoneresidencial, regexp_split_to_table(replace((prospects.emailprincipal), ' ', ''), ';') as emailprincipal, celular, dataNascimento, sexo, cep, endereco, setor, cidade, inativo , nomebatismo from prospects where emailprincipal ilike ('%").append(email).append("%') union all select codigo, nome, telefoneresidencial, regexp_split_to_table(replace((prospects.emailsecundario), ' ', ''), ';') as emailprincipal, celular, dataNascimento, sexo, cep, endereco, setor, cidade, inativo , nomebatismo from prospects where emailsecundario ilike ('%").append(email).append("%')) as t  where t.emailprincipal ilike '").append(email).append("' ");
			
		}
		if(sqlStr.length() > 0) {
			sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return prospectsVO;
		}
		return (montarDadosParaRegistroEntradaProspects(tabelaResultado, usuario));
	}
		return prospectsVO;
	}
	
	@Override
	public ProspectsVO consultarPorNomeEmailUnico(String email, String nome, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		ProspectsVO prospectsVO = new ProspectsVO();
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT codigo, nome, telefoneresidencial, emailprincipal, celular, dataNascimento, sexo, cep, endereco, setor, cidade, inativo, nomeBatismo FROM prospects WHERE prospects.emailPrincipal ilike ? and upper(sem_acentos(nome)) like (upper(sem_acentos(?)))   order by pessoa limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { email, nome });
		if (!tabelaResultado.next()) {
			return prospectsVO;
		}
		return (montarDadosParaRegistroEntradaProspects(tabelaResultado, usuario));
	}

	public Boolean validarUnicidadeEmailNomeProspect(String email, String nome) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT codigo FROM prospects WHERE (lower(prospects.emailPrincipal) = '").append(email.toLowerCase()).append("' OR lower(prospects.emailsecundario) = '").append(email.toLowerCase()).append("') AND prospects.nome ilike '").append(nome).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public Boolean verificarProspectMatriculado(Integer prospect) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select matricula.matricula from prospects inner join matricula on matricula.aluno = prospects.pessoa where codigo = ").append(prospect).append("");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public Boolean validarUnicidadeEmailProspect(String email, Integer codigo) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		String[] emails = email.split(";");
		for(String mail : emails) {
			if(sqlStr.length() > 0) {
				sqlStr.append(" union ");
			}
			sqlStr.append(" select * from ( ");
			sqlStr.append(" SELECT regexp_split_to_table(replace((lower(emailPrincipal)), ' ', ''), ';') as email FROM prospects WHERE (lower(prospects.emailPrincipal) ilike '%").append(mail.toLowerCase()).append("%') AND codigo <> " + codigo);
			sqlStr.append(" union ");
			sqlStr.append(" SELECT regexp_split_to_table(replace((lower(emailsecundario)), ' ', ''), ';') as email FROM prospects WHERE (lower(prospects.emailsecundario) = '%").append(mail.toLowerCase()).append("%') AND codigo <> " + codigo);
			sqlStr.append(" ) as t where t.email ilike ('").append(mail).append("') ");
		}
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	/**
	 * Responsável por realizar uma consulta de <code>Prospects</code> através
	 * do valor do atributo <code>String cpf</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProspectsVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCpf(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE replace(replace(prospects.cpf,'.',''),'-','') like(?) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND prospects.unidadeEnsino = ").append(unidadeEnsino);
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
			sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append("  or prospects.consultorpadrao is null) ");
			}
		}
		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}
		sqlStr.append(" ORDER BY prospects.cpf");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.removerMascara(valorConsulta.toUpperCase()) + "%"), nivelMontarDados, "prospects.", usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Prospects</code> através
	 * do valor do atributo <code>String razaoSocial</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProspectsVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorRazaoSocial(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE upper( prospects.razaoSocial ) like(?) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND prospects.unidadeEnsino = ").append(unidadeEnsino);
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
			sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append("  or prospects.consultorpadrao is null) ");
			}
		}
		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}

		sqlStr.append(" ORDER BY prospects.razaoSocial");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase() + "%"), nivelMontarDados, "prospects.", usuario));
	}

	public List consultarPorCursoInteresse(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("INNER JOIN cursointeresse ON cursointeresse.prospects = prospects.codigo ");
		sqlStr.append("INNER JOIN curso ON cursointeresse.curso = curso.codigo ");
		sqlStr.append("WHERE upper (sem_acentos(curso.nome)) ilike(sem_acentos('" + valorConsulta + "%')) ");
		if (unidadeEnsino != 0) {
			sqlStr.append("AND prospects.unidadeEnsino = " + unidadeEnsino + " ");
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
			sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append("  or prospects.consultorpadrao is null) ");
			}
		}
		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}
		sqlStr.append(" ORDER BY prospects.nome ");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, "prospects.", usuario));
	}

	public Boolean verificarUsuarioPossuiPermissaoConsulta(String identificadorAcaoPermissao, UsuarioVO usuario) {
		Boolean liberar = Boolean.FALSE;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, usuario);
			liberar = Boolean.TRUE;
		} catch (Exception e) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}

	/**
	 * Responsável por realizar uma consulta de <code>Prospects</code> através
	 * do valor do atributo <code>String nome</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProspectsVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();

		sqlStr.append(" WHERE upper (sem_acentos(prospects.nome)) ilike(sem_acentos('%").append(valorConsulta).append("%')) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND prospects.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
			sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append(" or prospects.consultorpadrao is null) ");
			}
		}
		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}

		sqlStr.append(" ORDER BY prospects.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, "prospects.", usuario));
	}

	public List consultarPorEmailPrincipalSecundario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String tipoFiltroConsulta) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();

		sqlStr.append("WHERE (prospects.emailPrincipal ilike('%").append(valorConsulta).append("%') or ");
		sqlStr.append(" prospects.emailSecundario ilike('%").append(valorConsulta).append("%')) ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND prospects.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		Boolean controlarPermissaoConsultorResp = verificarUsuarioPossuiPermissaoConsulta("BuscaProspect_consultarProspectOutrosConsultoresResponsaveis", usuario);
		if (!controlarPermissaoConsultorResp) {
			Integer colaboradorUsuario = 0;
			try {
				FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuario.getPessoa().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
				colaboradorUsuario = func.getCodigo();
			} catch (Exception e) {
			}
			if(Uteis.isAtributoPreenchido(colaboradorUsuario)){
			sqlStr.append(" AND (prospects.consultorpadrao = ").append(colaboradorUsuario).append("  or prospects.consultorpadrao is null) ");
			}
		}

		if (tipoFiltroConsulta != null) {
			if (tipoFiltroConsulta.equals("AT")) {
				sqlStr.append(" AND prospects.inativo = FALSE ");
			} else if (tipoFiltroConsulta.equals("IN")) {
				sqlStr.append(" AND prospects.inativo = TRUE ");
			}
		}

		sqlStr.append("ORDER BY prospects.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, "prospects.", usuario));
	}

	public void carregarDados(ProspectsVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((ProspectsVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public void carregarDados(ProspectsVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
		montarDadosCompleto((ProspectsVO) obj, resultado);
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codProspects, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Prospects.codigo= ").append(codProspects).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codProspects, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Prospects.codigo= ").append(codProspects).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProspectsVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, String prefixo, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, prefixo, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static List<TipoProspectVO> montarDadosConsultaPorTipoProspect(SqlRowSet tabelaResultado) throws Exception {
		List<TipoProspectVO> vetResultado = new ArrayList<TipoProspectVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosTipoProspect(tabelaResultado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados ( <code>ResultSet</code>) em um objeto da classe
	 * <code>ProspectsVO</code>.
	 * 
	 * @return O objeto da classe <code>ProspectsVO</code> com os dados
	 *         devidamente montados.
	 */
	public static TipoProspectVO montarDadosTipoProspect(SqlRowSet dadosSQL) {
		TipoProspectVO obj = new TipoProspectVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setPerfisProspect(dadosSQL.getString("tipo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setCnpj(dadosSQL.getString("cnpj"));
		obj.setCpf(dadosSQL.getString("cpf"));
		return obj;
	}

	public static ProspectsVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, String prefixo, UsuarioVO usuario) throws Exception {
		ProspectsVO obj = new ProspectsVO();
		obj.setCodigo(new Integer(dadosSQL.getInt(prefixo + "codigo")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt(prefixo + "pessoa")));
		obj.setNome(dadosSQL.getString(prefixo + "nome"));
		obj.setNomeBatismo(dadosSQL.getString(prefixo + "nomeBatismo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.setRazaoSocial(dadosSQL.getString(prefixo + "razaoSocial"));
		obj.setCpf(dadosSQL.getString(prefixo + "cpf"));
		obj.setCnpj(dadosSQL.getString(prefixo + "cnpj"));
		obj.setRg(dadosSQL.getString(prefixo + "rg"));
		obj.setOrgaoEmissor(dadosSQL.getString(prefixo + "orgaoEmissor"));
		obj.setEstadoEmissor(dadosSQL.getString(prefixo + "estadoEmissor"));
		obj.setTipoProspect(TipoProspectEnum.valueOf(dadosSQL.getString(prefixo + "tipoProspect")));
		obj.setTelefoneResidencial(dadosSQL.getString(prefixo + "telefoneresidencial"));
		obj.setEmailPrincipal(dadosSQL.getString(prefixo + "emailprincipal"));
		obj.setEmailSecundario(dadosSQL.getString(prefixo + "emailsecundario"));
		obj.getConsultorPadrao().setCodigo(dadosSQL.getInt(prefixo + "consultorPadrao"));
		obj.setInativo(dadosSQL.getBoolean(prefixo + "inativo"));
		obj.setMotivoInativacao(dadosSQL.getString(prefixo + "motivoInativacao"));
		obj.setSincronizadoRDStation(dadosSQL.getBoolean("sincronizadoRdStation"));
		obj.getTipoMidia().setCodigo(dadosSQL.getInt("prospects.tipomidia"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static ProspectsVO montarDadosCpfCnpjUnico(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProspectsVO obj = new ProspectsVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setCpf(dadosSQL.getString("cpf"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
		obj.setRg(dadosSQL.getString("rg"));
		obj.setDataNascimento(dadosSQL.getDate("dataNascimento"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setCnpj(dadosSQL.getString("cnpj"));
		obj.setOrgaoEmissor(dadosSQL.getString("orgaoEmissor"));
		obj.setEstadoEmissor(dadosSQL.getString("estadoEmissor"));
		obj.setInscricaoEstadual(dadosSQL.getString("inscricaoEstadual"));
		obj.setCEP(dadosSQL.getString("cep"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setTelefoneComercial(dadosSQL.getString("telefoneComercial"));
		obj.setTelefoneResidencial(dadosSQL.getString("telefoneResidencial"));
		obj.setTelefoneRecado(dadosSQL.getString("telefoneRecado"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setSkype(dadosSQL.getString("skype"));
		obj.setEmailPrincipal(dadosSQL.getString("emailPrincipal"));
		obj.setEmailSecundario(dadosSQL.getString("emailSecundario"));
		obj.setRenda(RendaProspectEnum.valueOf(dadosSQL.getString("renda")));
		obj.setFormacaoAcademica(FormacaoAcademicaProspectEnum.valueOf(dadosSQL.getString("formacaoAcademica")));
		obj.setTipoEmpresa(TipoEmpresaProspectEnum.valueOf(dadosSQL.getString("tipoEmpresa")));
		obj.setTipoProspect(TipoProspectEnum.valueOf(dadosSQL.getString("tipoProspect")));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		obj.getConsultorPadrao().setCodigo(dadosSQL.getInt("consultorPadrao"));
		montarDadosCidade(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public static void montarDadosCidade(ProspectsVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCidade().getCodigo().intValue() == 0) {
			obj.setCidade(new CidadeVO());
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}

	public static ProspectsVO montarDadosParaRegistroEntradaProspects(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		ProspectsVO obj = new ProspectsVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.setEmailPrincipal(dadosSQL.getString("emailprincipal"));
		obj.setTelefoneResidencial(dadosSQL.getString("telefoneresidencial"));
		obj.setTelefoneResidencial(dadosSQL.getString("telefoneresidencial"));
		obj.setInativo(dadosSQL.getBoolean("inativo"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;

	}

	public static void montarDadosUnidadeEnsino(ProspectsVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(new UnidadeEnsino().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static ProspectsVO montarDadosCompleto(ProspectsVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados Prospects
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNomeBatismo(dadosSQL.getString("nomeBatismo"));
		obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
		obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
		obj.setCargo(dadosSQL.getString("cargo"));
		obj.setTelefoneEmpresa(dadosSQL.getString("telefoneEmpresa"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setRg(dadosSQL.getString("rg"));
		obj.setDataNascimento(dadosSQL.getDate("dataNascimento"));
		obj.setSexo(dadosSQL.getString("sexo"));
		obj.setCnpj(dadosSQL.getString("cnpj"));
		obj.setInscricaoEstadual(dadosSQL.getString("inscricaoEstadual"));
		obj.setCEP(dadosSQL.getString("cep"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setOrgaoEmissor(dadosSQL.getString("orgaoEmissor"));
		obj.setEstadoEmissor(dadosSQL.getString("estadoEmissor"));
		obj.setTelefoneComercial(dadosSQL.getString("telefoneComercial"));
		obj.setTelefoneResidencial(dadosSQL.getString("telefoneResidencial"));
		obj.setTelefoneRecado(dadosSQL.getString("telefoneRecado"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setSkype(dadosSQL.getString("skype"));
		obj.setEmailPrincipal(dadosSQL.getString("emailPrincipal"));
		obj.setEmailSecundario(dadosSQL.getString("emailSecundario"));
		obj.setRenda(RendaProspectEnum.valueOf(dadosSQL.getString("renda")));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setEstadoCivil(dadosSQL.getString("estadoCivil"));
		obj.setNomePai(dadosSQL.getString("nomePai"));
		obj.setNomeMae(dadosSQL.getString("nomeMae"));
		obj.setDataExpedicao(dadosSQL.getDate("dataExpedicao"));
		obj.setParticipaBancoCurriculum(dadosSQL.getBoolean("participaBancoCurriculum"));
		obj.setDivulgarMeusDados(dadosSQL.getBoolean("divulgarMeusDados"));

		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		// ResponsavelCadastro
		obj.getResponsavelCadastro().setCodigo(new Integer(dadosSQL.getInt("responsavelCadastro")));
		obj.getResponsavelCadastro().setNome(dadosSQL.getString("usuarioResponsavelCadastro.nome"));
		if (dadosSQL.getString("tipoOrigemCadastro") != null) {
			obj.setTipoOrigemCadastro(TipoOrigemCadastroProspectEnum.valueOf(dadosSQL.getString("tipoOrigemCadastro")));
		}
		obj.setInativo(dadosSQL.getBoolean("inativo"));
		obj.setMotivoInativacao(dadosSQL.getString("motivoInativacao"));
		// ResponsavelInativacao
		obj.getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("responsavelInativacao")));
		obj.getResponsavelInativacao().setNome(dadosSQL.getString("usuarioResponsavelInativacao.nome"));

		if (dadosSQL.getString("formacaoAcademica") != null) {
			obj.setFormacaoAcademica(FormacaoAcademicaProspectEnum.valueOf(dadosSQL.getString("formacaoAcademica")));
		}
		if (dadosSQL.getString("tipoEmpresa") != null) {
			obj.setTipoEmpresa(TipoEmpresaProspectEnum.valueOf(dadosSQL.getString("tipoEmpresa")));
		}
		if (dadosSQL.getString("tipoProspect") != null) {
			obj.setTipoProspect(TipoProspectEnum.valueOf(dadosSQL.getString("tipoProspect")));
		}
		obj.getPessoa().setAluno(dadosSQL.getBoolean("isAluno"));
		obj.getPessoa().setProfessor(dadosSQL.getBoolean("isProfessor"));
		obj.getPessoa().setFuncionario(dadosSQL.getBoolean("isFuncionario"));

		// Dados arquivo
		obj.getArquivoFoto().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getArquivoFoto().setNome(dadosSQL.getString("nomeArquivo"));
		obj.getArquivoFoto().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));

		// Dados unidade ensino
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));

		// Dados consultor padrão
		obj.getConsultorPadrao().getPessoa().setCodigo(dadosSQL.getInt("codigoPessoaConsultorPadrao"));
		obj.getConsultorPadrao().getPessoa().setNome(dadosSQL.getString("nomeConsultorPadrao"));
		obj.getConsultorPadrao().setCodigo(dadosSQL.getInt("codigoConsultorPadrao"));

		// Dados cidade
		obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade.codigo")));
		obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado_sigla"));

		// Dados Pais
		obj.getNacionalidade().setCodigo(new Integer(dadosSQL.getInt("nacionalidade.codigo")));
		obj.getNacionalidade().setNome(dadosSQL.getString("nacionalidade.nome"));

		// Dados Naturalidade
		obj.getNaturalidade().setCodigo(new Integer(dadosSQL.getInt("naturalidade.codigo")));
		obj.getNaturalidade().setNome(dadosSQL.getString("naturalidade.nome"));
		obj.getNaturalidade().getEstado().setSigla(dadosSQL.getString("naturalidade_estado_sigla"));

		// Montar Pessoa
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		if (obj.getPessoa().getCodigo().intValue() > 0 ) {
			getFacadeFactory().getPessoaFacade().carregarDadosPessoaProspect(obj.getPessoa(),  new UsuarioVO());
		}
		// Montar Fornecedor
		obj.getFornecedor().setCodigo(dadosSQL.getInt("fornecedor"));

		// Montar Parceiro
		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));

		// Montar Unidade Ensino Prospect
		obj.getUnidadeEnsinoProspect().setCodigo(dadosSQL.getInt("unidadeEnsinoProspect"));

		// Montar Tipo Midia
		obj.getTipoMidia().setCodigo(dadosSQL.getInt("tm.codigo"));
		obj.getTipoMidia().setNomeMidia(dadosSQL.getString("tm.nomeMidia"));
		obj.getTipoMidia().setDescricaoMidia(dadosSQL.getString("tm.descricaoMidia"));

		// Dados registro entrada
		RegistroEntradaVO registroEntradaVO = null;
		HashMap<Integer, RegistroEntradaVO> hashtableRegistroEntrada = new HashMap<Integer, RegistroEntradaVO>(0);
		HashMap<Integer, CursoInteresseVO> hashtableCursoInteresse = new HashMap<Integer, CursoInteresseVO>(0);
		HashMap<Integer, FormacaoAcademicaVO> hashtableFormacaoAcademica = new HashMap<Integer, FormacaoAcademicaVO>(0);
		HashMap<Long, ProspectSegmentacaoOpcaoVO> hashtableProspectSegmentacao = new HashMap<Long, ProspectSegmentacaoOpcaoVO>(0);
		obj.getListaRegistroEntrada().clear();
		obj.getCursoInteresseVOs().clear();
		obj.getFormacaoAcademicaVOs().clear();
		do {
			registroEntradaVO = new RegistroEntradaVO();
			registroEntradaVO.setCodigo(dadosSQL.getInt("re.codigo"));
			registroEntradaVO.setDescricao(dadosSQL.getString("re.descricao"));
			registroEntradaVO.setDataEntrada(dadosSQL.getDate("re.dataEntrada"));
			registroEntradaVO.getUnidadeEnsino().setCodigo(dadosSQL.getInt("re.unidadeEnsino"));
			registroEntradaVO.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsinoRegistroEntrada.nome"));
			if (!hashtableRegistroEntrada.containsKey(registroEntradaVO.getCodigo()) && registroEntradaVO.getCodigo() != 0) {
				obj.getListaRegistroEntrada().add(registroEntradaVO);
				hashtableRegistroEntrada.put(registroEntradaVO.getCodigo(), registroEntradaVO);
			}

			// Dados curso interesse
			CursoInteresseVO cursoInteresseVO = null;
			cursoInteresseVO = new CursoInteresseVO();
			cursoInteresseVO.setCodigo(dadosSQL.getInt("cursointeresse.codigo"));
			cursoInteresseVO.setDataCadastro(dadosSQL.getTimestamp("cursoInteresse.dataCadastro"));
			// Dados curso
			cursoInteresseVO.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso.codigo")));
			cursoInteresseVO.getCurso().setNome(dadosSQL.getString("curso.nome"));
			if (!hashtableCursoInteresse.containsKey(cursoInteresseVO.getCodigo()) && cursoInteresseVO.getCodigo() != 0) {
				obj.getCursoInteresseVOs().add(cursoInteresseVO);
				hashtableCursoInteresse.put(cursoInteresseVO.getCodigo(), cursoInteresseVO);
			}

			// DADOS FORMAÇÃO ACADÊMICA
			FormacaoAcademicaVO formacaoAcademicaVO = new FormacaoAcademicaVO();
			formacaoAcademicaVO.setCodigo(dadosSQL.getInt("formacaoAcademica.codigo"));
			formacaoAcademicaVO.setInstituicao(dadosSQL.getString("formacaoAcademica.instituicao"));
			formacaoAcademicaVO.setEscolaridade(dadosSQL.getString("formacaoAcademica.escolaridade"));
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
			formacaoAcademicaVO.setFuncionario(false);
			if (!hashtableFormacaoAcademica.containsKey(formacaoAcademicaVO.getCodigo()) && formacaoAcademicaVO.getCodigo() != 0) {
				obj.getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
				hashtableFormacaoAcademica.put(formacaoAcademicaVO.getCodigo(), formacaoAcademicaVO);
			}

			ProspectSegmentacaoOpcaoVO prospectSegmentacaoOpcaoVO = new ProspectSegmentacaoOpcaoVO();
			prospectSegmentacaoOpcaoVO.setCodigo(dadosSQL.getLong("pso.codigo"));
			prospectSegmentacaoOpcaoVO.setNovoObj(false);
			prospectSegmentacaoOpcaoVO.getSegmentacaoOpcao().setCodigo(dadosSQL.getInt("segmentacaoopcao"));
			prospectSegmentacaoOpcaoVO.getProspect().setCodigo(dadosSQL.getInt("codigo"));
			if (!hashtableProspectSegmentacao.containsKey(prospectSegmentacaoOpcaoVO.getCodigo()) && prospectSegmentacaoOpcaoVO.getCodigo() != 0) {
				obj.getProspectSegmentacaoOpcaoVOs().add(prospectSegmentacaoOpcaoVO);
				hashtableProspectSegmentacao.put(prospectSegmentacaoOpcaoVO.getCodigo(), prospectSegmentacaoOpcaoVO);
			}

		} while (dadosSQL.next());

		return obj;
	}

	public ProspectsVO realizarPreenchimentoProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(obj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;

	}

	public ProspectsVO realizarPreenchimentoProspectPorUnidade(String cnpj, String nome, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setUnidadeEnsinoProspect(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCNPJUnico(cnpj, nome, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setTipoProspect(TipoProspectEnum.JURIDICO);
		obj.setNome(obj.getUnidadeEnsinoProspect().getNome());
		obj.setCEP(obj.getUnidadeEnsinoProspect().getCEP());
		obj.setCnpj(obj.getUnidadeEnsinoProspect().getCNPJ());
		obj.setComplemento(obj.getUnidadeEnsinoProspect().getComplemento());
		obj.setEndereco(obj.getUnidadeEnsinoProspect().getEndereco());
		obj.setEmailPrincipal(obj.getUnidadeEnsinoProspect().getEmail());
		obj.setRazaoSocial(obj.getUnidadeEnsinoProspect().getRazaoSocial());
		obj.setSetor(obj.getUnidadeEnsinoProspect().getSetor());
		obj.setTelefoneComercial(obj.getUnidadeEnsinoProspect().getTelComercial1());
		obj.setCidade(obj.getUnidadeEnsinoProspect().getCidade());
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public ProspectsVO realizarPreenchimentoProspectPorFornecedorCpf(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorCPFUnico(cpf, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setTipoProspect(TipoProspectEnum.FISICO);
		obj.setNome(obj.getFornecedor().getNome());
		obj.setCEP(obj.getFornecedor().getCEP());
		obj.setCpf(obj.getFornecedor().getCPF());
		obj.setComplemento(obj.getFornecedor().getComplemento());
		obj.setEmailPrincipal(obj.getFornecedor().getEmail());
		obj.setEndereco(obj.getFornecedor().getEndereco());
		obj.setRg(obj.getFornecedor().getRG());
		obj.setTelefoneComercial(obj.getFornecedor().getTelComercial1());
		obj.setCidade(obj.getFornecedor().getCidade());
		obj.setNovoObj(Boolean.FALSE);
		return obj;

	}

	public ProspectsVO realizarPreenchimentoProspectPorFornecedorCnpj(String cnpj, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorCNPJUnico(cnpj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setTipoProspect(TipoProspectEnum.JURIDICO);
		obj.setNome(obj.getFornecedor().getNome());
		obj.setCEP(obj.getFornecedor().getCEP());
		obj.setCnpj(obj.getFornecedor().getCNPJ());
		obj.setComplemento(obj.getFornecedor().getComplemento());
		obj.setEmailPrincipal(obj.getFornecedor().getEmail());
		obj.setEndereco(obj.getFornecedor().getEndereco());
		obj.setInscricaoEstadual(obj.getFornecedor().getInscEstadual());
		obj.setRazaoSocial(obj.getFornecedor().getRazaoSocial());
		obj.setTelefoneComercial(obj.getFornecedor().getTelComercial1());
		obj.setCidade(obj.getFornecedor().getCidade());
		obj.setNovoObj(Boolean.FALSE);

		return obj;

	}

	public ProspectsVO realizarPreenchimentoProspectPorParceiroCpf(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorCPFUnico(cpf, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setTipoProspect(TipoProspectEnum.FISICO);
		obj.setNome(obj.getParceiro().getNome());
		obj.setCpf(obj.getParceiro().getCPF());
		obj.setCEP(obj.getParceiro().getCEP());
		obj.setEmailPrincipal(obj.getParceiro().getEmail());
		obj.setEndereco(obj.getParceiro().getEndereco());
		obj.setRg(obj.getParceiro().getRG());
		obj.setSetor(obj.getParceiro().getSetor());
		obj.setTelefoneComercial(obj.getParceiro().getTelComercial1());
		obj.setCidade(obj.getParceiro().getCidade());
		obj.setComplemento(obj.getParceiro().getComplemento());
		obj.setNovoObj(Boolean.FALSE);
		return obj;

	}

	public ProspectsVO realizarPreenchimentoProspectPorParceiroCnpj(String cnpj, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorCNPJUnico(cnpj, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setTipoProspect(TipoProspectEnum.JURIDICO);
		obj.setNome(obj.getParceiro().getNome());
		obj.setCnpj(obj.getParceiro().getCNPJ());
		obj.setCEP(obj.getParceiro().getCEP());
		obj.setEmailPrincipal(obj.getParceiro().getEmail());
		obj.setEndereco(obj.getParceiro().getEndereco());
		obj.setRazaoSocial(obj.getParceiro().getRazaoSocial());
		obj.setSetor(obj.getParceiro().getSetor());
		obj.setInscricaoEstadual(obj.getParceiro().getInscEstadual());
		obj.setTelefoneComercial(obj.getParceiro().getTelComercial1());
		obj.setCidade(obj.getParceiro().getCidade());
		obj.setComplemento(obj.getParceiro().getComplemento());
		obj.setNovoObj(Boolean.FALSE);
		return obj;

	}

	public ProspectsVO realizarPreenchimentoProspectPorPessoa(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorCPFUnico(cpf, null, false, false, usuario));
		obj.setTipoProspect(TipoProspectEnum.FISICO);
		obj.setNome(obj.getPessoa().getNome());
		obj.setNomeBatismo(obj.getPessoa().getNomeBatismo());
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getCEP())) {
		obj.setCEP(obj.getPessoa().getCEP());
		}
		obj.setCpf(obj.getPessoa().getCPF());
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getComplemento())) {
		obj.setComplemento(obj.getPessoa().getComplemento());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getComplemento())) {
			obj.setEndereco(obj.getPessoa().getComplemento());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getEmail())) {
		obj.setEmailPrincipal(obj.getPessoa().getEmail());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getSetor())) {
		obj.setSetor(obj.getPessoa().getSetor());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getTelefoneComer())) {
		obj.setTelefoneComercial(obj.getPessoa().getTelefoneComer());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getCidade())) {
		obj.setCidade(obj.getPessoa().getCidade());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getCelular())) {
		obj.setCelular(obj.getPessoa().getCelular());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getRG())) {
		obj.setRg(obj.getPessoa().getRG());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getTelefoneRecado())) {
		obj.setTelefoneRecado(obj.getPessoa().getTelefoneRecado());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getTelefoneRes())) {
		obj.setTelefoneResidencial(obj.getPessoa().getTelefoneRes());
		}
		obj.setArquivoFoto(obj.getPessoa().getArquivoImagem());
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getSexo())) {
		obj.setSexo(obj.getPessoa().getSexo());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getDataNasc())) {
		obj.setDataNascimento(obj.getPessoa().getDataNasc());
		}

		if(Uteis.isAtributoPreenchido(obj.getPessoa().getCargoPessoaEmpresa())) {
		obj.setCargo(obj.getPessoa().getCargoPessoaEmpresa());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getDataEmissaoRG())) {
		obj.setDataExpedicao(obj.getPessoa().getDataEmissaoRG());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getEmail2())) {
		obj.setEmailSecundario(obj.getPessoa().getEmail2());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getEstadoCivil())) {
		obj.setEstadoCivil(obj.getPessoa().getEstadoCivil());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getEstadoEmissaoRG())) {
		obj.setEstadoEmissor(obj.getPessoa().getEstadoEmissaoRG());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getNumero())) {
		obj.setNumero(obj.getPessoa().getNumero());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getNumero())) {
			obj.setOrgaoEmissor(obj.getPessoa().getNumero());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getNacionalidade())) {
		obj.setNacionalidade(obj.getPessoa().getNacionalidade());
		}
		if(Uteis.isAtributoPreenchido(obj.getPessoa().getNaturalidade())) {
		obj.setNaturalidade(obj.getPessoa().getNaturalidade());
		}

		for (FiliacaoVO filiacaoVO : obj.getPessoa().getFiliacaoVOs()) {
			if (filiacaoVO.getTipo().equals("PA")) {
				obj.setNomePai(filiacaoVO.getPais().getNome());
			}
			if (filiacaoVO.getTipo().equals("MA")) {
				obj.setNomeMae(filiacaoVO.getPais().getNome());
			}
		}

		for (FormacaoAcademicaVO formacaoAcademicaVO : obj.getPessoa().getFormacaoAcademicaVOs()) {
			obj.getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
		}

		return obj;

	}

	public PessoaVO realizarPreenchimentoPessoaPorProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception {
		PessoaVO pessoa = new PessoaVO();
		pessoa.setNome(obj.getNome());
		pessoa.setNomeBatismo(obj.getNomeBatismo());
		pessoa.setCEP(obj.getCEP());
		pessoa.setCPF(obj.getCpf());
		pessoa.setComplemento(obj.getComplemento());
		pessoa.setEndereco(obj.getEndereco());
		pessoa.setEmail(obj.getEmailPrincipal());
		pessoa.setSetor(obj.getSetor());
		pessoa.setTelefoneComer(obj.getTelefoneComercial());
		pessoa.setCidade(obj.getCidade());
		pessoa.setCelular(obj.getCelular());
		pessoa.setRG(obj.getRg());
		pessoa.setTelefoneRecado(obj.getTelefoneRecado());
		pessoa.setTelefoneRes(obj.getTelefoneResidencial());
		pessoa.setArquivoImagem(obj.getArquivoFoto());
		pessoa.setSexo(obj.getSexo());
		pessoa.setDataNasc(obj.getDataNascimento());

		pessoa.setCargoPessoaEmpresa(obj.getCargo());
		pessoa.setDataEmissaoRG(obj.getDataExpedicao());
		pessoa.setEmail2(obj.getEmailSecundario());
		pessoa.setEstadoCivil(obj.getEstadoCivil());
		pessoa.setEstadoEmissaoRG(obj.getEstadoEmissor());
		pessoa.setNumero(obj.getNumero());
		pessoa.setOrgaoEmissor(obj.getOrgaoEmissor());
		pessoa.setNacionalidade(obj.getNacionalidade());
		pessoa.setNaturalidade(obj.getNaturalidade());
		pessoa.setNomeEmpresa(obj.getNomeEmpresa());
		pessoa.setCargoPessoaEmpresa(obj.getCargo());
		pessoa.setTipoMidiaCaptacao(obj.getTipoMidia());
		pessoa.setCodProspect(obj.getCodigo());

		if (!obj.getNomePai().trim().isEmpty()) {
			FiliacaoVO filiacaoPai = new FiliacaoVO();
			PessoaVO pessoaPai = new PessoaVO();
			filiacaoPai.setTipo("PA");
			pessoaPai.setNome(obj.getNomePai());
			pessoaPai.setGerarNumeroCPF(true);
			filiacaoPai.setPais(pessoaPai);
			pessoa.getFiliacaoVOs().add(filiacaoPai);
		}
		if (!obj.getNomeMae().trim().isEmpty()) {
			FiliacaoVO filiacaoMae = new FiliacaoVO();
			PessoaVO pessoaMae = new PessoaVO();
			filiacaoMae.setTipo("MA");
			pessoaMae.setNome(obj.getNomeMae());
			pessoaMae.setGerarNumeroCPF(true);
			filiacaoMae.setPais(pessoaMae);
			pessoa.getFiliacaoVOs().add(filiacaoMae);
		}
		for (FormacaoAcademicaVO formacaoAcademicaVO : obj.getFormacaoAcademicaVOs()) {
			pessoa.getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
		}
		return pessoa;
	}

	public void realizarPreenchimentoPessoaPorProspect(PessoaVO pessoa, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		pessoa.setNome(obj.getNome());
		pessoa.setCEP(obj.getCEP());
		pessoa.setCPF(obj.getCpf());
		pessoa.setComplemento(obj.getComplemento());
		pessoa.setEndereco(obj.getEndereco());
		pessoa.setEmail(obj.getEmailPrincipal());
		pessoa.setSetor(obj.getSetor());
		pessoa.setTelefoneComer(obj.getTelefoneComercial());
		pessoa.setCidade(obj.getCidade());
		pessoa.setCelular(obj.getCelular());
		pessoa.setRG(obj.getRg());
		pessoa.setTelefoneRecado(obj.getTelefoneRecado());
		pessoa.setTelefoneRes(obj.getTelefoneResidencial());
		pessoa.setArquivoImagem(obj.getArquivoFoto());
		pessoa.setSexo(obj.getSexo());
		pessoa.setDataNasc(obj.getDataNascimento());

		pessoa.setCargoPessoaEmpresa(obj.getCargo());
		pessoa.setDataEmissaoRG(obj.getDataExpedicao());
		pessoa.setEmail2(obj.getEmailSecundario());
		pessoa.setEstadoCivil(obj.getEstadoCivil());
		pessoa.setEstadoEmissaoRG(obj.getEstadoEmissor());
		pessoa.setNumero(obj.getNumero());
		pessoa.setOrgaoEmissor(obj.getOrgaoEmissor());
		pessoa.setNacionalidade(obj.getNacionalidade());
		pessoa.setNaturalidade(obj.getNaturalidade());

		boolean existePai = false;
		boolean existeMae = false;
		for (FiliacaoVO filiacaoVO : pessoa.getFiliacaoVOs()) {
			if (filiacaoVO.getTipo().equals("PA")) {
				existePai = true;
			}
			if (filiacaoVO.getTipo().equals("MA")) {
				existeMae = true;
			}
		}

		if (!obj.getNomePai().trim().isEmpty() && !existePai) {

			FiliacaoVO filiacaoPai = new FiliacaoVO();
			PessoaVO pessoaPai = new PessoaVO();
			filiacaoPai.setTipo("PA");
			pessoaPai.setNome(obj.getNomePai());
			pessoaPai.setGerarNumeroCPF(true);
			filiacaoPai.setPais(pessoaPai);
			pessoa.getFiliacaoVOs().add(filiacaoPai);
		}
		if (!obj.getNomeMae().trim().isEmpty() && !existeMae) {
			FiliacaoVO filiacaoMae = new FiliacaoVO();
			PessoaVO pessoaMae = new PessoaVO();
			filiacaoMae.setTipo("MA");
			pessoaMae.setNome(obj.getNomeMae());
			pessoaMae.setGerarNumeroCPF(true);
			filiacaoMae.setPais(pessoaMae);
			pessoa.getFiliacaoVOs().add(filiacaoMae);
		}
		for (FormacaoAcademicaVO formacaoAcademicaVO : obj.getFormacaoAcademicaVOs()) {
			pessoa.adicionarObjFormacaoAcademicaVOs(formacaoAcademicaVO);
		}
	}

	public ProspectsVO realizarPreenchimentoCodProspectPorPessoa(String cpf, ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorCPFUnico(cpf, null, false, false, usuario));
		obj.setTipoProspect(TipoProspectEnum.FISICO);
		obj.setNome(obj.getPessoa().getNome());
		obj.setCEP(obj.getPessoa().getCEP());
		obj.setCpf(obj.getPessoa().getCPF());
		obj.setComplemento(obj.getPessoa().getComplemento());
		obj.setEndereco(obj.getPessoa().getEndereco());
		obj.setEmailPrincipal(obj.getPessoa().getEmail());
		obj.setSetor(obj.getPessoa().getSetor());
		obj.setTelefoneComercial(obj.getPessoa().getTelefoneComer());
		obj.setCidade(obj.getPessoa().getCidade());
		obj.setCelular(obj.getPessoa().getCelular());
		obj.setRg(obj.getPessoa().getRG());
		obj.setTelefoneRecado(obj.getPessoa().getTelefoneRecado());
		obj.setTelefoneResidencial(obj.getPessoa().getTelefoneRes());
		obj.setArquivoFoto(obj.getPessoa().getArquivoImagem());
		obj.setSexo(obj.getPessoa().getSexo());
		obj.setDataNascimento(obj.getPessoa().getDataNasc());

		obj.setCargo(obj.getPessoa().getCargoPessoaEmpresa());
		obj.setDataExpedicao(obj.getPessoa().getDataEmissaoRG());
		obj.setEmailSecundario(obj.getPessoa().getEmail2());
		obj.setEstadoCivil(obj.getPessoa().getEstadoCivil());
		obj.setEstadoEmissor(obj.getPessoa().getEstadoEmissaoRG());
		obj.setNumero(obj.getPessoa().getNumero());
		obj.setOrgaoEmissor(obj.getPessoa().getOrgaoEmissor());
		obj.setNacionalidade(obj.getPessoa().getNacionalidade());
		obj.setNaturalidade(obj.getPessoa().getNaturalidade());

		for (FiliacaoVO filiacaoVO : obj.getPessoa().getFiliacaoVOs()) {
			if (filiacaoVO.getTipo().equals("PA")) {
				obj.setNomePai(filiacaoVO.getPais().getNome());
			}
			if (filiacaoVO.getTipo().equals("MA")) {
				obj.setNomeMae(filiacaoVO.getPais().getNome());
			}
		}

		for (FormacaoAcademicaVO formacaoAcademicaVO : obj.getPessoa().getFormacaoAcademicaVOs()) {
			obj.getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
		}

		return obj;

	}

	@Override
	public List<TipoProspectVO> consultarTipoProspect(String cpfConsulta, String cnpjConsulta, Integer prospectIgnorar) throws Exception {
		if(!Uteis.isAtributoPreenchido(cpfConsulta) && !Uteis.isAtributoPreenchido(cnpjConsulta)) {
			return null;
		}
				StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo , 'PROSPECT' as tipo , cpf, cnpj, nome, emailPrincipal as email from prospects");
		if (cpfConsulta.isEmpty()) {
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%'");
		} else {
			sql.append(" where replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta));
			sql.append("%'");
		}
		sql.append(" and prospects.codigo != ").append(prospectIgnorar);
		if (cnpjConsulta.isEmpty()) {
			sql.append(" union all");
			sql.append(" SELECT codigo ,  'PESSOA' as tipo , cpf, '' as cnpj, nome, email  from pessoa ");
			sql.append(" where replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta));
			sql.append("%'");
			sql.append(" and  codigo not in (SELECT  pessoa  from prospects  where  replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta)).append("%' ");
			sql.append(" and prospects.codigo != ").append(prospectIgnorar);
			sql.append(" and pessoa is not null ) ");
		}
		sql.append(" union all ");
		sql.append(" SELECT codigo ,  'PARCEIRO' as tipo , cpf,  cnpj, nome, email  from parceiro ");
		if (cpfConsulta.isEmpty()) {
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%'");
		} else {
			sql.append("where replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta));
			sql.append("%'");
		}
		
		sql.append(" and  codigo not in (SELECT  parceiro  from prospects ");
		if (cpfConsulta.isEmpty()) {
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%' and parceiro is not null ");
		} else {
			sql.append("where replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta));
			sql.append("%' and parceiro is not null  ");
		}
		sql.append(" and prospects.codigo != ").append(prospectIgnorar).append(") ");
		sql.append(" union all ");
		sql.append(" SELECT codigo ,  'FORNECEDOR' as tipo , cpf,  cnpj , nome, email  from fornecedor ");
		if (cpfConsulta.isEmpty()) {
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%'");
		} else {
			sql.append(" where replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta));
			sql.append("%'");
		}
		sql.append(" and  codigo not in (SELECT  fornecedor  from prospects ");
		if (cpfConsulta.isEmpty()) {
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%' and fornecedor is not null ");
		} else {
			sql.append(" where replace(replace(CPF,'.',''),'-','') ilike '");
			sql.append(Uteis.retirarMascaraCPF(cpfConsulta));
			sql.append("%' and fornecedor is not null ");
		}
		sql.append(" and prospects.codigo != ").append(prospectIgnorar).append(") ");
		if (cpfConsulta.isEmpty()) {
			sql.append(" union all ");
			sql.append(" SELECT codigo ,  'UNIDADE' as tipo , '' as cpf,  cnpj, nome, email  from unidadeensino ");
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%'");
			sql.append(" and  codigo not in (SELECT  unidadeensino  from prospects ");
			sql.append(" where replace(replace(replace(CNPJ,'.',''),'-',''),'/','') ilike '");
			sql.append(Uteis.retirarMascaraCNPJ(cnpjConsulta));
			sql.append("%' and unidadeensino is not null");
			sql.append(" and prospects.codigo != ").append(prospectIgnorar).append(") ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaPorTipoProspect(tabelaResultado);
	}

	@Override
	public List<TipoProspectVO> consultarTipoProspectEmail(String emails, Integer unidadeEnsino, Integer prospectIgnorar) throws Exception {
		String[] mails = emails.split(";");
		StringBuilder sql = new StringBuilder();
		for(String email: mails) {		
			if(sql.length() > 0) {
				sql.append(" union ");
			}	
		sql.append("SELECT codigo , 'PROSPECT' as tipo , cpf, cnpj, nome, case when emailsecundario is not null and emailsecundario != '' and emailPrincipal is not null and emailPrincipal != '' then emailPrincipal||';'||emailsecundario else case when emailPrincipal is not null and emailPrincipal != '' then emailPrincipal else emailsecundario end end as email from prospects");
		sql.append(" where (emailPrincipal ilike '%");
		sql.append(email.trim());
		sql.append("%'  or emailsecundario ilike '%").append(email).append("%') ");
		if (unidadeEnsino != 0) {
			sql.append(" and prospects.unidadeensino = ").append(unidadeEnsino);
		}
		sql.append(" and prospects.codigo  != ").append(prospectIgnorar).append(" ");
		sql.append(" union ");
		sql.append(" SELECT codigo ,  'PESSOA' as tipo , cpf, '' as cnpj, nome, email  from pessoa ");
		sql.append(" where email ilike '%");
		sql.append(email.trim());
		sql.append("%'");
		sql.append(" and  codigo not in (SELECT  pessoa  from prospects ");
		sql.append(" where (emailPrincipal ilike '%");
		sql.append(email.trim());
		sql.append("%' or emailsecundario ilike '%").append(email).append("%') and pessoa is not null ");
		sql.append(" and prospects.codigo  != ").append(prospectIgnorar).append(" ");
		if (unidadeEnsino != 0) {
			sql.append(" and prospects.unidadeensino <> ").append(unidadeEnsino).append(" ) ");
		} else {
			sql.append(" ) ");
		}
		sql.append(" union ");
		sql.append(" SELECT codigo ,  'PARCEIRO' as tipo , cpf,  cnpj, nome, email  from parceiro ");
		sql.append(" where email ilike '%");
		sql.append(email.trim());
		sql.append("%'");
		sql.append(" and  codigo not in (SELECT  parceiro  from prospects ");
		sql.append(" where (emailPrincipal ilike '%");
		sql.append(email.trim());
		sql.append("%' or emailsecundario ilike '%").append(email).append("%' ) and parceiro is not null ");
		sql.append(" and prospects.codigo  != ").append(prospectIgnorar).append(" ");
		if (unidadeEnsino != 0) {
			sql.append(" and prospects.unidadeensino <> ").append(unidadeEnsino).append(" ) ");
		} else {
			sql.append(" ) ");
		}
		sql.append(" union ");
		sql.append(" SELECT codigo ,  'FORNECEDOR' as tipo , cpf,  cnpj , nome, email  from fornecedor ");
		sql.append(" where email ilike '%");
		sql.append(email.trim());
		sql.append("%'");
		sql.append(" and  codigo not in (SELECT  fornecedor  from prospects ");
		sql.append(" where (emailPrincipal ilike '%");
		sql.append(email.trim());
		sql.append("%' or emailsecundario ilike '%").append(email).append("%' ) and fornecedor is not null ");
		sql.append(" and prospects.codigo  != ").append(prospectIgnorar).append(" ");
		if (unidadeEnsino != 0) {
			sql.append(" and prospects.unidadeensino <> ").append(unidadeEnsino).append(" ) ");
		} else {
			sql.append(" ) ");
		}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaPorTipoProspect(tabelaResultado);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PessoaVO verificaCriandoPessoaProspect(ProspectsVO obj, UsuarioVO usuario) throws Exception {
		obj = this.consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if (obj.getPessoa().getCodigo() == null || obj.getPessoa().getCodigo().intValue() == 0) {
			obj.preencherDadosPessoaComProspect();
			getFacadeFactory().getPessoaFacade().incluir(obj.getPessoa(), false, usuario, null, false);
			this.alterarPessoaProspect(obj, usuario);
		}
		return obj.getPessoa();
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>CursoInteresseVO</code> ao List <code>cursoInteresseVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>CursoInteresse</code> - getCurso().getCodigo() - como identificador
	 * (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CursoInteresseVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjCursoInteresseVOs(ProspectsVO objProspectsVO, CursoInteresseVO obj) throws Exception {
		obj.setProspects(objProspectsVO);
		int index = 0;
		for (CursoInteresseVO objExistente : objProspectsVO.getCursoInteresseVOs()) {
			if (objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) {
				objProspectsVO.getCursoInteresseVOs().set(index, obj);
				return;
			}
			index++;
		}
		objProspectsVO.getCursoInteresseVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>CursoInteresseVO</code> no List <code>cursoInteresseVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>CursoInteresse</code> - getCurso().getCodigo() - como identificador
	 * (key) do objeto no List.
	 * 
	 * @param curso
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjCursoInteresseVOs(ProspectsVO objProspectsVO, Integer curso) throws Exception {
		int index = 0;
		for (CursoInteresseVO objExistente : objProspectsVO.getCursoInteresseVOs()) {
			if (objExistente.getCurso().getCodigo().equals(curso)) {
				objProspectsVO.getCursoInteresseVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>CursoInteresseVO</code> no List <code>cursoInteresseVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>CursoInteresse</code> - getCurso().getCodigo() - como identificador
	 * (key) do objeto no List.
	 * 
	 * @param curso
	 *            Parâmetro para localizar o objeto do List.
	 */
	public CursoInteresseVO consultarObjCursoInteresseVO(ProspectsVO objProspectsVO, Integer curso) throws Exception {
		for (CursoInteresseVO objExistente : objProspectsVO.getCursoInteresseVOs()) {
			if (objExistente.getCurso().getCodigo().equals(curso)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Operação responsável por adicionar um objeto da
	 * <code>CursoInteresseVO</code> no Hashtable <code>CursoInteresses</code>.
	 * Neste Hashtable são mantidos todos os objetos de CursoInteresse de uma
	 * determinada Prospects.
	 * 
	 * @param obj
	 *            Objeto a ser adicionado no Hashtable.
	 */
	public void adicionarObjCursoInteresses(CursoInteresseVO obj) throws Exception {
		getCursoInteresses().put(obj.getCurso().getCodigo() + "", obj);
		// adicionarObjSubordinadoOC
	}

	/**
	 * Operação responsável por remover um objeto da classe
	 * <code>CursoInteresseVO</code> do Hashtable <code>CursoInteresses</code>.
	 * Neste Hashtable são mantidos todos os objetos de CursoInteresse de uma
	 * determinada Prospects.
	 * 
	 * @param Curso
	 *            Atributo da classe <code>CursoInteresseVO</code> utilizado
	 *            como apelido (key) no Hashtable.
	 */
	public void excluirObjCursoInteresses(Integer Curso) throws Exception {
		getCursoInteresses().remove(Curso + "");
		// excluirObjSubordinadoOC
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ProspectsVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ProspectsVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Prospects.codigo= ").append(codigoPrm).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Prospects ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, "prospects.", usuario));
	}

	public ProspectsVO consultarPorCodigoPessoa(Integer codigoPessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		//ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Prospects.pessoa= ").append(codigoPessoa).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new ProspectsVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, "prospects.", usuario));
	}
	
	public String consultarAgendaProspect(Integer codigoProspect) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select prospect, (select count(*) from compromissoagendapessoahorario as comp1 ");		
		sqlStr.append(" where comp1.tiposituacaocompromissoenum in ('REALIZADO', 'PARALIZADO', 'REALIZADO_COM_REMARCACAO') ");		
		sqlStr.append(" and comp1.prospect = compromissoagendapessoahorario.prospect) as qtdRealizado, ");		
		sqlStr.append(" (select count(*) from compromissoagendapessoahorario as comp1 ");		
		sqlStr.append(" where comp1.tiposituacaocompromissoenum not in ('REALIZADO', 'PARALIZADO', 'REALIZADO_COM_REMARCACAO') ");		
		sqlStr.append(" and comp1.prospect = compromissoagendapessoahorario.prospect) as qtdPendente ");						
		sqlStr.append(" from compromissoagendapessoahorario where prospect = ").append(codigoProspect).append("");
		sqlStr.append(" group by prospect ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return "Existe(m) " + tabelaResultado.getInt("qtdRealizado") + " agenda(s) realizada(s) para este prospect e " + tabelaResultado.getInt("qtdPendente") + " agenda(s) pendende(s). Ao realizar a exclusão o sistema irá excluir todas as agendas já realizadas/pendentes, deseja realizar esta exclusão?";
		}
		return "Deseja realizar a exclusão do prospect?";
	}
	
	public Hashtable getCursoInteresses() {
		if (cursoInteresses == null) {
			cursoInteresses = new Hashtable(0);
		}
		return (cursoInteresses);
	}

	public void setCursoInteresses(Hashtable cursoInteresses) {
		this.cursoInteresses = cursoInteresses;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Prospects.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Prospects.idEntidade = idEntidade;
	}

	private void validarTipoProspectCpfCnpj(ProspectsVO obj) throws Exception {
		// if (obj.getTipoProspect() == TipoProspectEnum.NENHUM) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_campoTipoProspectDeveSerInformado"));
		// }
		if (obj.getJuridico()) {
			if (obj.getCnpj().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoCNPJDeveSerInformado"));
			}
		} else {
			if (obj.getCpf().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoCPFDeveSerInformado"));
			}
		}
	}

	private void excluirProspectsRegistroEntrada(Integer codigo, UsuarioVO usuario) {
		String sql = "DELETE FROM RegistroEntradaProspects WHERE ((prospects = " + codigo + "))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql);
	}

	public void montarCursoInteresseVO(SqlRowSet dadosSQL, ProspectsVO obj) {
		CursoInteresseVO cursoInteresseVO = null;
		if (adicionarCursos(obj.getCursoInteresseVOs(), dadosSQL.getInt("cewf_codigo"))) {
			cursoInteresseVO = new CursoInteresseVO();
			cursoInteresseVO.setCodigo(dadosSQL.getInt("cursoInteresse_codigo"));
			cursoInteresseVO.setDataCadastro(dadosSQL.getTimestamp("cursoInteresse_dataCadastro"));
			cursoInteresseVO.getProspects().setCodigo(obj.getCodigo());
			cursoInteresseVO.getCurso().setCodigo((dadosSQL.getInt("cursoInteresse_curso")));
			cursoInteresseVO.getCurso().setNome((dadosSQL.getString("cursoInteresse_nome")));
			if (cursoInteresseVO.getCodigo() != null && cursoInteresseVO.getCodigo() != 0) {
				obj.getCursoInteresseVOs().add(cursoInteresseVO);
			}
		}
	}

	public boolean adicionarCursos(List<CursoInteresseVO> lista, Integer codigo) {
		for (CursoInteresseVO curso : lista) {
			if (curso.getCodigo().equals(codigo)) {
				return false;
			}
		}
		return true;
	}

	public void adicionarObjFormacaoAcademicaVOs(FormacaoAcademicaVO obj, ProspectsVO prospectsVO) throws Exception {
		FormacaoAcademicaVO.validarDados(obj);
		int index = 0;
		Iterator i = prospectsVO.getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO objExistente = (FormacaoAcademicaVO) i.next();
			if (objExistente.getCurso().equals(obj.getCurso())) {
				prospectsVO.getFormacaoAcademicaVOs().set(index, obj);
				return;
			}
			index++;
		}
		prospectsVO.getFormacaoAcademicaVOs().add(obj);
		// adicionarObjSubordinadoOC
	}

	public void excluirObjFormacaoAcademicaVOs(String curso, ProspectsVO prospectsVO) throws Exception {
		int index = 0;
		Iterator i = prospectsVO.getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO objExistente = (FormacaoAcademicaVO) i.next();
			if (objExistente.getCurso().equals(curso)) {
				prospectsVO.getFormacaoAcademicaVOs().remove(index);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}


	public void obterProspectsDuplicadosUnificando(UsuarioVO usuarioVO) throws Exception {
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append(" select emailprincipal, count(prospects.codigo) from prospects where emailprincipal <> '' and prospects.pessoa is null ");
		sqlStr.append(" group by emailprincipal having count(prospects.codigo) > 1 ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {								
			String emailDuplicado = tabelaResultado.getString("emailprincipal");
			sqlStr = new StringBuffer();
			sqlStr.append(" select codigo, nome, cpf, emailprincipal from prospects where emailprincipal = '").append(emailDuplicado).append("'");
			SqlRowSet tabelaResultado2 = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());			
			Integer codProspectManter = 0;
			Integer codProspectRemover = 0;
			String nomeProspectManter = "";
			String nomeProspectRemover = "";
			while (tabelaResultado2.next()) {
				if (codProspectManter == 0) {
					nomeProspectManter = tabelaResultado2.getString("nome");
					codProspectManter = tabelaResultado2.getInt("codigo");
				} else {
					nomeProspectRemover = tabelaResultado2.getString("nome");
					codProspectRemover = tabelaResultado2.getInt("codigo");
					if (nomeProspectManter.length() < nomeProspectRemover.length()) {
						nomeProspectRemover = nomeProspectManter;
						codProspectRemover = codProspectManter;
						nomeProspectManter = tabelaResultado2.getString("nome");
						codProspectManter = tabelaResultado2.getInt("codigo");
					}
				}
			}
			try {
				getFacadeFactory().getProspectsFacade().unificarProspects(codProspectManter, codProspectRemover, usuarioVO);
			} catch (Exception e) {
				System.out.print("Erro ao unificar prospects - codManter => "+ codProspectManter + " - codRemover =>" + codProspectRemover);
			}
		}		
	}
	
	@Override
	public void realizarEnvioEmail(ComunicacaoInternaVO comunicacaoInternaVO, List<ProspectsVO> prospectsVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		validarDadosEnvioComunicadoInterno(comunicacaoInternaVO);
		List<EmailVO> emailVOs = new ArrayList<EmailVO>();
		EmailVO emailVO = null;
		for (ProspectsVO prospectsVO : prospectsVOs) {
			if (prospectsVO.getSelecionado() && (!prospectsVO.getEmailPrincipal().equals("") || !prospectsVO.getEmailSecundario().equals(""))) {
				emailVO = new EmailVO();
				emailVO.setAssunto(comunicacaoInternaVO.getAssunto());
				emailVO.setMensagem(comunicacaoInternaVO.getMensagem());
				emailVO.setNomeDest(prospectsVO.getNome());
				if(prospectsVO.getEmailPrincipal().equals("")) {
					emailVO.setEmailDest(prospectsVO.getEmailSecundario());					
				} else {
					emailVO.setEmailDest(prospectsVO.getEmailPrincipal());					
				}
				emailVO.setNomeRemet(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno().getNome());
				emailVO.setEmailRemet(configuracaoGeralSistemaVO.getEmailRemetente());
				emailVO.setCaminhoAnexos(getFacadeFactory().getEmailFacade().capturarCaminhosAnexosEnvioEmail(comunicacaoInternaVO, configuracaoGeralSistemaVO, usuarioVO));
				emailVOs.add(emailVO);
			}
		}
		JobEnviarEmailProspectsSelecionadosPainelGestor job = new JobEnviarEmailProspectsSelecionadosPainelGestor(emailVOs);
		job.run();
	}
	
	private void validarDadosEnvioComunicadoInterno(ComunicacaoInternaVO comunicacaoInternaVO) throws Exception {
		if (comunicacaoInternaVO.getAssunto().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_envioEmail_assunto"));
		}
	}
	

	@Override
	public List<ProspectsVO> consultarPossivelProspectVincularPessoa(Integer pessoa, String nome, String email, String cpf, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<ProspectsVO> prospectsVOs = new ArrayList<ProspectsVO>(0);
		if(Uteis.isAtributoPreenchido(pessoa)){
			ProspectsVO prospectsVO = new ProspectsVO();
			prospectsVO.setTipoProspect(TipoProspectEnum.FISICO);
			prospectsVO.setCpf(cpf);
			prospectsVO = consultarPorCodigoPessoa(pessoa, nivelMontarDados, usuario);
			if (!prospectsVO.isNovoObj()) {
				prospectsVOs.add(prospectsVO);
				return prospectsVOs;
			}
		}
		if (Uteis.isAtributoPreenchido(cpf)) {
			ProspectsVO prospectsVO = new ProspectsVO();
			prospectsVO.setTipoProspect(TipoProspectEnum.FISICO);
			prospectsVO.setCpf(cpf);
			prospectsVO = consultarPorCPFCNPJUnico(prospectsVO, controlarAcesso, nivelMontarDados, usuario);
			if (!prospectsVO.isNovoObj()) {
				prospectsVOs.add(prospectsVO);
			}
		}
		if (Uteis.isAtributoPreenchido(email)) {
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE (trim(sem_acentos(upper(prospects.emailPrincipal))) = trim(sem_acentos(upper('").append(email).append("'))) ");
			sqlStr.append(" or trim(sem_acentos(upper(prospects.emailsecundario))) = trim(sem_acentos(upper('").append(email).append("')))) ");
			//sqlStr.append(" AND sem_acentos(upper(prospects.nome)) = sem_acentos(upper(").append(nome).append(")) ");
			sqlStr.append(" ORDER BY prospects.nome");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (tabelaResultado.next()) {
				prospectsVOs.add(montarDados(tabelaResultado, nivelMontarDados, "prospects.", usuario));
			}
		}
		return prospectsVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVinculoUnidadeEnsinoProspectSemUnidadeEnsinoPorPessoa(Integer pessoa, Integer unidadeEnsino, UsuarioVO usuario) throws Exception{
		if(Uteis.isAtributoPreenchido(unidadeEnsino) && Uteis.isAtributoPreenchido(pessoa)){
			getConexao().getJdbcTemplate().update("update prospects set unidadeensino = ? where pessoa = ? and unidadeensino is null " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), unidadeEnsino, pessoa);			
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ProspectsVO realizarCriacaoProspectLigacaoReceptivaRS(ProspectsVO prospectsVO, UsuarioVO usuario) throws Exception {
        ProspectsVO prospectPessoa = new ProspectsVO();
		prospectPessoa.setTipoProspect(TipoProspectEnum.FISICO);
		prospectPessoa.setCpf(prospectsVO.getPessoa().getCPF());
		prospectPessoa.setCursoInteresseVOs(prospectsVO.getCursoInteresseVOs());
		prospectPessoa.setFormacaoAcademicaVOs(prospectsVO.getFormacaoAcademicaVOs());
		
        if (!prospectsVO.getPessoa().getCPF().toString().isEmpty()) {
			prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorCPFCNPJUnico(prospectPessoa, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		boolean encontrouProspect = false;
		if (prospectPessoa.getCodigo().equals(0)) {
			// Se entrarmos auqi é por que de fato não existe um Prospect
			// para a pessoa
			// Assim, temos que gerar um prospect e gravá-lo, passando a
			// referenciá-lo no compromisso abaixo.
			if (!prospectsVO.getEmailPrincipal().trim().equals("")) {
				prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(prospectsVO.getEmailPrincipal().trim(), false, usuario);
			}
			if (prospectPessoa.getCodigo().equals(0)) {
				if (!prospectsVO.getPessoa().getCodigo().equals(0)) {
					getFacadeFactory().getPessoaFacade().carregarDados(prospectsVO.getPessoa(), usuario);
				}
				prospectPessoa = prospectsVO.getPessoa().gerarProspectsVOAPartirPessoaVO();
			} else {
				encontrouProspect = true;
			}
		} else {
			encontrouProspect = true;
		}
		if (encontrouProspect) {
			
			if (prospectPessoa.getPessoa().getCodigo().equals(0)) {
				// Se entrar aqui é por que encontramos o prospect, mas o
				// mesmo não está
				// vinculado ao aluno (pessoa) correspondete. Logo temos que
				// fazer este vinculo.
				prospectPessoa.getPessoa().setCodigo(prospectsVO.getPessoa().getCodigo());
//				getFacadeFactory().getProspectsFacade().alterarPessoaProspect(prospectPessoa, usuario);
			}
			if (!prospectPessoa.getPessoa().getCodigo().equals(prospectsVO.getPessoa().getCodigo())) {
				throw new Exception("Ocorreu um erro ao identificar o prospect da pessoa de código: " + prospectsVO.getPessoa().getCodigo() + ". Prospect encontrado por CPF " + prospectsVO.getPessoa().getCPF() + " E-mail " + prospectsVO.getPessoa().getEmail() + " está vinculado a outra pessoa.");
			}
		}
		prospectPessoa.getUnidadeEnsino().setCodigo(prospectsVO.getUnidadeEnsino().getCodigo());
		prospectPessoa.getResponsavelCadastro().setCodigo(prospectsVO.getResponsavelCadastro().getCodigo());
		prospectPessoa.getPessoa().setEmail(prospectsVO.getEmailPrincipal());
		prospectPessoa.getPessoa().setNome(prospectsVO.getNome());
		// Primeiramente verificamos se existe prospect existente
		// Caso não exista iremos incluir um novo
		if (prospectPessoa.getCodigo().equals(0)) {
			getFacadeFactory().getProspectsFacade().incluirSemValidarDados(prospectPessoa, Boolean.FALSE, usuario, null);
		} else {
			getFacadeFactory().getProspectsFacade().alterarPessoaProspect(prospectPessoa, usuario);
			if (prospectPessoa.getInativo()) {
				prospectPessoa.setInativo(false);
				alterarSituacaoAtivacaoProspect(prospectPessoa, usuario);
			}
		}
		return prospectPessoa;
    }
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoAtivacaoProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Prospects set inativo=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getInativo());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarResponsavelFinanceiroProspect(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Prospects set responsavelFinanceiro=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getResponsavelFinanceiro());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public ProspectsVO consultarDadosCompletosPorIdProspect(Integer codigo, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		resultado = consultaRapidaPorChavePrimariaDadosCompletos(codigo, usuario);
		return (ProspectsVO) montarDadosCompleto((ProspectsVO) new ProspectsVO(), resultado);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFlagProspectSincronizadoComRDStation(final ProspectsVO obj, UsuarioVO usuario) throws Exception {
		try {
			
			StringBuffer sql = new StringBuffer("UPDATE Prospects set sincronizadordstation=?, logSincronizacaoRD=? WHERE ((codigo = ?))");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setBoolean(1, obj.getSincronizadoRDStation());
					sqlAlterar.setString(2, obj.getLogSincronizacaoRD());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarConsultorProspectUnificacaoFuncionario(Integer codigoConsutorResponsavelManter ,Integer codigoConsutorResponsavelRemover, UsuarioVO usuario) throws Exception {
		try {
			final String sqlStr = "UPDATE Prospects set consultorPadrao= ? WHERE ((consultorPadrao = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);;
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { codigoConsutorResponsavelManter, codigoConsutorResponsavelRemover });			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void tratarMensagemExcecaoProspects(Exception e, String cpf, Integer pessoa) throws Exception {
		if(e.getMessage().contains("ck_prospects_cpf")) {
			throw new Exception("Já existem outro PROSPECT cadastrado com o mesmo CPF "+cpf+", favor realizar a unificação dos mesmos.");
		}
		if(e.getMessage().contains("ck_prospects_pessoa")) {			
			throw new Exception("Este PROSPECT está vinculado a pessoa de código "+pessoa+", porém a mesma não possui o mesmo cpf e nem o mesmo e-mail.");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaProspectParaNull(Integer codProspectRemover, UsuarioVO usuario) throws Exception {
		try {
			
			if(Uteis.isAtributoPreenchido(codProspectRemover)){
				getConexao().getJdbcTemplate().update("update prospects set pessoa = null , cpf = null where codigo = ? " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), codProspectRemover);			
			}		
		
		} catch (Exception e) {
			throw e;
		}
	}
	
	//Criado o metodo para validar se os campos principais do prospects estão preenchidos , se não sera vinculado a informação da pessoa vinculado ao prospects.
	
	public ProspectsVO executarValidarDadosProspectsConformePessoa(ProspectsVO prospectVO) {
		
		  if (!Uteis.isAtributoPreenchido(prospectVO.getCpf()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getCPF())) {
		       		prospectVO.setCpf(prospectVO.getPessoa().getCPF());
		  }
		  if (!Uteis.isAtributoPreenchido(prospectVO.getEndereco()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getEndereco())) {
	     		prospectVO.setEndereco(prospectVO.getPessoa().getEndereco());
		  }
		  if (!Uteis.isAtributoPreenchido(prospectVO.getSetor()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getSetor())) {
	     		prospectVO.setSetor(prospectVO.getPessoa().getSetor());
		  }
		  if (!Uteis.isAtributoPreenchido(prospectVO.getCEP()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getCEP())) {
	     		prospectVO.setCEP(prospectVO.getPessoa().getCEP());
		  }
		  if (!Uteis.isAtributoPreenchido(prospectVO.getComplemento()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getComplemento())) {
	     		prospectVO.setComplemento(prospectVO.getPessoa().getComplemento());
		  }
		  if (!Uteis.isAtributoPreenchido(prospectVO.getCidade()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getCidade())) {
	     		prospectVO.setCidade(prospectVO.getPessoa().getCidade());
		 }
		 if (!Uteis.isAtributoPreenchido(prospectVO.getSexo()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getSexo())) {
				prospectVO.setSexo(prospectVO.getPessoa().getSexo());
		 }
		 if (!Uteis.isAtributoPreenchido(prospectVO.getEstadoCivil()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getEstadoCivil())) {
				prospectVO.setEstadoCivil(prospectVO.getPessoa().getEstadoCivil());
		 }
		 if (!Uteis.isAtributoPreenchido(prospectVO.getTelefoneRecado()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getTelefoneRecado())) {
	    		prospectVO.setTelefoneRecado(prospectVO.getPessoa().getTelefoneRecado());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getCelular()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getCelular())) {
				prospectVO.setCelular(prospectVO.getPessoa().getCelular());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getEmailPrincipal()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getEmail())) {
				prospectVO.setEmailPrincipal(prospectVO.getPessoa().getEmail());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getDataNascimento()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getDataNasc())) {
				prospectVO.setDataNascimento(prospectVO.getPessoa().getDataNasc());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getNaturalidade()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getNaturalidade())) {
				prospectVO.setNaturalidade(prospectVO.getPessoa().getNaturalidade());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getNacionalidade()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getNacionalidade())) {
				prospectVO.setNacionalidade(prospectVO.getPessoa().getNacionalidade());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getRg()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getRG())) {
				prospectVO.setRg(prospectVO.getPessoa().getRG());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getDataExpedicao()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getDataEmissaoRG())) {
				prospectVO.setDataExpedicao(prospectVO.getPessoa().getDataEmissaoRG());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getOrgaoEmissor()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getOrgaoEmissor())) {
				prospectVO.setOrgaoEmissor(prospectVO.getPessoa().getOrgaoEmissor());
		}
		if (!Uteis.isAtributoPreenchido(prospectVO.getNomeBatismo()) && Uteis.isAtributoPreenchido(prospectVO.getPessoa().getNomeBatismo())) {
				prospectVO.setNomeBatismo(prospectVO.getPessoa().getNomeBatismo());
		}			
		
		return prospectVO;
	}
	
	@Override
	public ProspectsVO consultarPaiMaeProspectPorCodigo(Integer codigoProspect) throws Exception{
		StringBuilder sql  = new StringBuilder("select nomePai, nomeMae , pessoa from prospects where codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString() , codigoProspect);
		ProspectsVO prospect = new ProspectsVO();
		if (rs.next()) {
			prospect.setNomePai(rs.getString("nomePai"));
			prospect.setNomeMae(rs.getString("nomeMae"));
			prospect.getPessoa().setCodigo(rs.getInt("pessoa"));
		}
		return prospect;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarApenasDadosPreenchidos(final ProspectsVO obj, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			Prospects.alterar(getIdEntidade());
			realizarUpperCaseDados(obj);
			verificarProspectComPessoaPreenchidoBaseELimpoObjeto(obj, obj.getPessoa());
			List<Object> lista = new ArrayList<Object>(0);
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE Prospects SET ");
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
			if (Uteis.isAtributoPreenchido(obj.getEstadoEmissor())) {
				sql.append(", estadoEmissor=? ");
				lista.add(obj.getEstadoEmissor());
			}
			if (Uteis.isAtributoPreenchido(obj.getDataExpedicao())) {
				sql.append(", dataExpedicao=? ");
				lista.add(Uteis.getDataJDBC(obj.getDataExpedicao()));
			}
			if (Uteis.isAtributoPreenchido(obj.getEmailPrincipal())) {
				sql.append(", emailPrincipal=? ");
				lista.add(obj.getEmailPrincipal());
			}
			if (Uteis.isAtributoPreenchido(obj.getTelefoneResidencial())) {
				sql.append(", telefoneResidencial=? ");
				lista.add(obj.getTelefoneResidencial());
			}
			if (Uteis.isAtributoPreenchido(obj.getTelefoneComercial())) {
				sql.append(", telefoneComercial=? ");
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
				sql.append(", dataNascimento=? ");
				lista.add(Uteis.getDataJDBC(obj.getDataNascimento()));
			}
			if (Uteis.isAtributoPreenchido(obj.getNaturalidade())) {
				sql.append(", naturalidade=? ");
				lista.add(obj.getNaturalidade().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
				sql.append(", unidadeEnsino=? ");
				lista.add(obj.getUnidadeEnsino().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(obj.getTipoMidia())) {
				sql.append(", tipoMidia=? ");
				lista.add(obj.getTipoMidia().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(obj.getTipoProspect())) {
				sql.append(", tipoProspect=? ");
				lista.add(obj.getTipoProspect().name());
			}
			sql.append(" WHERE codigo=? ");
			lista.add(obj.getCodigo());
			getConexao().getJdbcTemplate().update(sql.toString(), lista.toArray());
			if (Uteis.isAtributoPreenchido(obj.getCursoInteresseVOs())) {
				getFacadeFactory().getCursoInteresseFacade().alterarCursoInteresses(obj.getCodigo(), obj.getCursoInteresseVOs(), usuario);
			}
			if (Uteis.isAtributoPreenchido(obj.getFormacaoAcademicaVOs())) {
				getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicasProspects(obj.getCodigo(), obj.getFormacaoAcademicaVOs(), false, usuario);
			}
			if (Uteis.isAtributoPreenchido(obj.getPessoa())) {
				getFacadeFactory().getPessoaFacade().alterarApenasDadosPreenchidos(obj, false, usuario);
			}
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().realizarAlteracaoCompromissoAoAlterarConsultor(obj, usuario);
		} catch (Exception e) {
			tratarMensagemExcecaoProspects(e, obj.getCpf(), obj.getPessoa().getCodigo());
			throw e;
		}
	}

	
}