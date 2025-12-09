package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EmpresaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContatoParceiroVO;
import negocio.comuns.financeiro.ParceiroUnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ParceiroInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ParceiroVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ParceiroVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ParceiroVO
 * @see ControleAcesso
 * @see EmpresaVO
 */
@Repository
@Scope("singleton")
@Lazy
public class Parceiro extends ControleAcesso implements ParceiroInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Parceiro() throws Exception {
		super();
		setIdEntidade("Parceiro");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ParceiroVO</code>.
	 */
	public ParceiroVO novo() throws Exception {
		Parceiro.incluir(getIdEntidade());
		ParceiroVO obj = new ParceiroVO();
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ParceiroVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, configuracaoGeralSistema, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, configuracaoGeralSistema, usuarioVO);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaParceiroUnidadeEnsinoContaCorrente(), "parceiroUnidadeEnsinoContaCorrente", "parceiro", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getParceiroUnidadeEnsinoContaCorrenteFacade().persistir(obj.getListaParceiroUnidadeEnsinoContaCorrente(), false, usuarioVO);
		
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ParceiroVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ParceiroVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ParceiroVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {

		try {
			realizarValidacaoParticipacaoBancoCurriculum(obj);
			Parceiro.incluir(getIdEntidade(), verificarAcesso, usuario);
			ParceiroVO.validarDados(obj);
			if (validarUnicidadeNome(obj.getNome())) {
				throw new Exception("Já existe um parceiro cadastrado com este NOME");
			}
			if (obj.getTipoEmpresa().equals("FI")) {
				if (validarUnicidadeCPF(obj.getCPF())) {
					throw new Exception("Já existe um parceiro cadastrado com este CPF");
				}
			} else {
				if (validarUnicidadeCNPJ(obj.getCNPJ())) {
					throw new Exception("Já existe um parceiro cadastrado com este CNPJ");
				}
			}

			final String sql = "INSERT INTO Parceiro( nome, razaoSocial, endereco, setor, numero, complemento, cidade, CEP, CNPJ, inscEstadual, telComercial1, telComercial2, telComercial3, email, site, fax, tipoEmpresa, RG, CPF, tipoParceiro, participaBancoCurriculum, inscricaoMunicipal, descricaoEmpresa, dataCadastro, empresaSigilosaParaVaga, dataAceitouParticiparBancoCurriculum, isentarTaxaBoleto, isentarJuro, possuiAliquotaEmissaoNotaEspecifica, issqn, pis, cofins, inss,  csll, conveniadaParaVagasEstagio, dataInicioConvenioEstagio, dataFinalConvenioEstagio, nrAcordoConvenio, registroConselho, emitirNotaFiscalParaBeneficiario, orgaoRegistroConselho, irrf, categoriadespesa, formapagamento, formapagamentorecebimento, permiteenviarremessa, nomeBanco, numeroBancoRecebimento, numeroAgenciaRecebimento, digitoAgenciaRecebimento, contaCorrenteRecebimento, digitoCorrenteRecebimento,  chaveEnderecamentoPix , tipoIdentificacaoChavePixEnum  , custeaParcelasMaterialDidatico, considerarValorDescontoDeclaracaoImpostoRendaAluno, isentarMulta, isentarReajusteParcelaVencida, financiamentoProprio, numeroParcelaVencidasSuspenderFinanciamento, emitirBoletoEmNomeBeneficiado, permiteEmitirBoletoAlunoVinculadoParceiro, permiteRemessaBoletoAlunoVinculadoParceiro, realizarReajustePrecoComBaseIndiceReajuste, considerarParcelasMaterialDidaticoReajustePreco, validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula,validarDebitoFinanceiroAoIncluirConvenioMatricula) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ,? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);


			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getRazaoSocial());
					sqlInserir.setString(3, obj.getEndereco());
					sqlInserir.setString(4, obj.getSetor());
					sqlInserir.setString(5, obj.getNumero());
					sqlInserir.setString(6, obj.getComplemento());
					sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
					sqlInserir.setString(8, obj.getCEP());
					sqlInserir.setString(9, obj.getCNPJ());
					sqlInserir.setString(10, obj.getInscEstadual());
					sqlInserir.setString(11, obj.getTelComercial1());
					sqlInserir.setString(12, obj.getTelComercial2());
					sqlInserir.setString(13, obj.getTelComercial3());
					sqlInserir.setString(14, obj.getEmail());
					sqlInserir.setString(15, obj.getSite());
					sqlInserir.setString(16, obj.getFax());
					sqlInserir.setString(17, obj.getTipoEmpresa());
					sqlInserir.setString(18, obj.getRG());
					sqlInserir.setString(19, obj.getCPF());
					sqlInserir.setString(20, obj.getTipoParceiro());
					sqlInserir.setBoolean(21, obj.getParticipaBancoCurriculum());
					sqlInserir.setString(22, obj.getInscMunicipal());
					sqlInserir.setString(23, obj.getDescricaoEmpresa());
					sqlInserir.setDate(24, Uteis.getDataJDBC(new Date()));
					sqlInserir.setBoolean(25, obj.getEmpresaSigilosaParaVaga());
					sqlInserir.setDate(26, Uteis.getDataJDBC(obj.getDataAceitouParticiparBancoCurriculum()));
					sqlInserir.setBoolean(27, obj.getIsentarTaxaBoleto());
					sqlInserir.setBoolean(28, obj.isIsentarJuro());
					sqlInserir.setBoolean(29, obj.getPossuiAliquotaEmissaoNotaEspecifica());
					sqlInserir.setDouble(30, obj.getIssqn());
					sqlInserir.setDouble(31, obj.getPis());
					sqlInserir.setDouble(32, obj.getCofins());
					sqlInserir.setDouble(33, obj.getInss());
					sqlInserir.setDouble(34, obj.getCsll());
					sqlInserir.setBoolean(35, obj.getConveniadaParaVagasEstagio());
					if (obj.getDataInicioConvenioEstagio() != null) {
						sqlInserir.setDate(36, Uteis.getDataJDBC(obj.getDataInicioConvenioEstagio()));
					} else {
						sqlInserir.setNull(36, 0);
					}
					if (obj.getDataFinalConvenioEstagio() != null) {
						sqlInserir.setDate(37, Uteis.getDataJDBC(obj.getDataFinalConvenioEstagio()));
					} else {
						sqlInserir.setNull(37, 0);
					}
					if ((obj.getNrAcordoConvenio().equals(0)) && (obj.getConveniadaParaVagasEstagio())) {
						obj.setNrAcordoConvenio(obterProximoNrAcordoConvenio());
					}
					sqlInserir.setInt(38, obj.getNrAcordoConvenio());
					sqlInserir.setString(39, obj.getRegistroConselho());
					sqlInserir.setBoolean(40, obj.getEmitirNotaFiscalParaBeneficiario());
					sqlInserir.setString(41, obj.getOrgaoRegistroConselho());
					sqlInserir.setDouble(42, obj.getIRRF());
					if (Uteis.isAtributoPreenchido(obj.getCategoriaDespesa())) {
						sqlInserir.setInt(43, obj.getCategoriaDespesa().getCodigo());
					} else {
						sqlInserir.setNull(43, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamento())) {
						sqlInserir.setInt(44, obj.getFormaPagamento().getCodigo());
					} else {
						sqlInserir.setNull(44, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamentoRecebimento())) {
						sqlInserir.setInt(45, obj.getFormaPagamentoRecebimento().getCodigo());
					} else {
						sqlInserir.setNull(45, 0);
					}
					sqlInserir.setBoolean(46, obj.getPermiteenviarremessa());					
					sqlInserir.setString(47, obj.getNomeBanco());					
					sqlInserir.setString(48, obj.getNumeroBancoRecebimento());					
					sqlInserir.setString(49, obj.getNumeroAgenciaRecebimento());					
					sqlInserir.setString(50, obj.getDigitoAgenciaRecebimento());					
					sqlInserir.setString(51, obj.getContaCorrenteRecebimento());					
					sqlInserir.setString(52, obj.getDigitoCorrenteRecebimento());
					sqlInserir.setString(53, obj.getChaveEnderecamentoPix());
                    if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
						sqlInserir.setString(54, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
						sqlInserir.setNull(54, 0);
					}
					int i = 54;					
					Uteis.setValuePreparedStatement(obj.isCusteaParcelasMaterialDidatico(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getConsiderarValorDescontoDeclaracaoImpostoRendaAluno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isIsentarMulta(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isIsentarReajusteParcelaVencida(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isFinanciamentoProprio(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroParcelaVencidasSuspenderFinanciamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isEmitirBoletoEmNomeBeneficiado(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPermiteEmitirBoletoAlunoVinculadoParceiro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPermiteRemessaBoletoAlunoVinculadoParceiro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRealizarReajustePrecoComBaseIndiceReajuste(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getConsiderarParcelasMaterialDidaticoReajustePreco(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isValidarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.isValidarDebitoFinanceiroAoIncluirConvenioMatricula(), ++i, sqlInserir);
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
			if (obj.getParticipaBancoCurriculum() && obj.getParceiroJuridico()) {
				getFacadeFactory().getUsuarioFacade().executarInclusaoNovoUsuario(obj, configuracaoGeralSistema, usuario);
			}
			getFacadeFactory().getContatoParceiroFacade().incluirContatoParceiros(obj.getCodigo(), obj.getContatoParceiroVOs());
			getFacadeFactory().getAreaProfissionalParceiroFacade().incluirAreaProfissionalParceiroVOs(obj.getCodigo(), obj.getAreaProfissionalParceiroVOs());
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	public Integer obterProximoNrAcordoConvenio() {
		Integer ultimoCodigo = 0;
		StringBuilder sqlStr = new StringBuilder("SELECT max(nrAcordoConvenio) as ultimoCodigo FROM parceiro ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			ultimoCodigo = tabelaResultado.getInt("ultimoCodigo");
		}
		return ultimoCodigo + 1;
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ParceiroVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ParceiroVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ParceiroVO obj, Boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			realizarValidacaoParticipacaoBancoCurriculum(obj);
			Parceiro.alterar(getIdEntidade(), verificarAcesso, usuario);
			ParceiroVO.validarDados(obj);
			final String sql = "UPDATE Parceiro set nome=?, razaoSocial=?, endereco=?, setor=?, numero=?, complemento=?, cidade=?, CEP=?, CNPJ=?, inscEstadual=?, telComercial1=?, telComercial2=?, telComercial3=?, email=?, site=?, fax=?, tipoEmpresa=?, RG=?, CPF=?, tipoParceiro=?, participaBancoCurriculum=?, inscricaoMunicipal=?, descricaoEmpresa=?, empresaSigilosaParaVaga=?, dataAceitouParticiparBancoCurriculum=?, isentarTaxaBoleto=?, isentarJuro=?, possuiAliquotaEmissaoNotaEspecifica=?, issqn=?, pis=?, cofins=?, inss=?,  csll=?, conveniadaParaVagasEstagio=?, dataInicioConvenioEstagio=?, dataFinalConvenioEstagio=?, nrAcordoConvenio=?, registroConselho=?, emitirNotaFiscalParaBeneficiario=?, orgaoRegistroConselho=?, irrf=?, categoriadespesa=?, formapagamento=?, formapagamentorecebimento=?, permiteenviarremessa=?, nomeBanco=?, numeroBancoRecebimento=?, numeroAgenciaRecebimento=?, digitoAgenciaRecebimento=?, contaCorrenteRecebimento=?, digitoCorrenteRecebimento=?,  chaveEnderecamentoPix=?  , tipoIdentificacaoChavePixEnum=? ,  custeaParcelasMaterialDidatico=?, considerarValorDescontoDeclaracaoImpostoRendaAluno=?, isentarMulta=?, isentarReajusteParcelaVencida=?, financiamentoProprio=?, numeroParcelaVencidasSuspenderFinanciamento=?, emitirBoletoEmNomeBeneficiado=?, permiteEmitirBoletoAlunoVinculadoParceiro=?, permiteRemessaBoletoAlunoVinculadoParceiro=?, realizarReajustePrecoComBaseIndiceReajuste=?, considerarParcelasMaterialDidaticoReajustePreco=?, validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula=?,validarDebitoFinanceiroAoIncluirConvenioMatricula=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getRazaoSocial());
					sqlAlterar.setString(3, obj.getEndereco());
					sqlAlterar.setString(4, obj.getSetor());
					sqlAlterar.setString(5, obj.getNumero());
					sqlAlterar.setString(6, obj.getComplemento());
					sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
					sqlAlterar.setString(8, obj.getCEP());
					sqlAlterar.setString(9, obj.getCNPJ());
					sqlAlterar.setString(10, obj.getInscEstadual());
					sqlAlterar.setString(11, obj.getTelComercial1());
					sqlAlterar.setString(12, obj.getTelComercial2());
					sqlAlterar.setString(13, obj.getTelComercial3());
					sqlAlterar.setString(14, obj.getEmail());
					sqlAlterar.setString(15, obj.getSite());
					sqlAlterar.setString(16, obj.getFax());
					sqlAlterar.setString(17, obj.getTipoEmpresa());
					sqlAlterar.setString(18, obj.getRG());
					sqlAlterar.setString(19, obj.getCPF());
					sqlAlterar.setString(20, obj.getTipoParceiro());
					sqlAlterar.setBoolean(21, obj.getParticipaBancoCurriculum());
					sqlAlterar.setString(22, obj.getInscMunicipal());
					sqlAlterar.setString(23, obj.getDescricaoEmpresa());
					sqlAlterar.setBoolean(24, obj.getEmpresaSigilosaParaVaga());
					sqlAlterar.setDate(25, Uteis.getDataJDBC(obj.getDataAceitouParticiparBancoCurriculum()));
					sqlAlterar.setBoolean(26, obj.getIsentarTaxaBoleto());
					sqlAlterar.setBoolean(27, obj.isIsentarJuro());
					sqlAlterar.setBoolean(28, obj.getPossuiAliquotaEmissaoNotaEspecifica());
					sqlAlterar.setDouble(29, obj.getIssqn());
					sqlAlterar.setDouble(30, obj.getPis());
					sqlAlterar.setDouble(31, obj.getCofins());
					sqlAlterar.setDouble(32, obj.getInss());
					sqlAlterar.setDouble(33, obj.getCsll());
					sqlAlterar.setBoolean(34, obj.getConveniadaParaVagasEstagio());
					if (obj.getDataInicioConvenioEstagio() != null) {
						sqlAlterar.setDate(35, Uteis.getDataJDBC(obj.getDataInicioConvenioEstagio()));
					} else {
						sqlAlterar.setNull(35, 0);
					}
					if (obj.getDataFinalConvenioEstagio() != null) {
						sqlAlterar.setDate(36, Uteis.getDataJDBC(obj.getDataFinalConvenioEstagio()));
					} else {
						sqlAlterar.setNull(36, 0);
					}
					sqlAlterar.setInt(37, obj.getNrAcordoConvenio());
					sqlAlterar.setString(38, obj.getRegistroConselho());
					sqlAlterar.setBoolean(39, obj.getEmitirNotaFiscalParaBeneficiario());
					sqlAlterar.setString(40, obj.getOrgaoRegistroConselho());
					sqlAlterar.setDouble(41, obj.getIRRF());
					if (Uteis.isAtributoPreenchido(obj.getCategoriaDespesa())) {
						sqlAlterar.setInt(42, obj.getCategoriaDespesa().getCodigo());
					} else {
						sqlAlterar.setNull(42, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamento())) {
						sqlAlterar.setInt(43, obj.getFormaPagamento().getCodigo());
					} else {
						sqlAlterar.setNull(43, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamentoRecebimento())) {
						sqlAlterar.setInt(44, obj.getFormaPagamentoRecebimento().getCodigo());
					} else {
						sqlAlterar.setNull(44, 0);
					}
					sqlAlterar.setBoolean(45, obj.getPermiteenviarremessa());
					sqlAlterar.setString(46, obj.getNomeBanco());					
					sqlAlterar.setString(47, obj.getNumeroBancoRecebimento());					
					sqlAlterar.setString(48, obj.getNumeroAgenciaRecebimento());					
					sqlAlterar.setString(49, obj.getDigitoAgenciaRecebimento());					
					sqlAlterar.setString(50, obj.getContaCorrenteRecebimento());					
					sqlAlterar.setString(51, obj.getDigitoCorrenteRecebimento());					
					sqlAlterar.setString(52, obj.getChaveEnderecamentoPix());
                    if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
                    	sqlAlterar.setString(53, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
						sqlAlterar.setNull(53, 0);
					}
					int i=53;
					Uteis.setValuePreparedStatement(obj.isCusteaParcelasMaterialDidatico(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getConsiderarValorDescontoDeclaracaoImpostoRendaAluno(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isIsentarMulta(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isIsentarReajusteParcelaVencida(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isFinanciamentoProprio(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNumeroParcelaVencidasSuspenderFinanciamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isEmitirBoletoEmNomeBeneficiado(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPermiteEmitirBoletoAlunoVinculadoParceiro(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPermiteRemessaBoletoAlunoVinculadoParceiro(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRealizarReajustePrecoComBaseIndiceReajuste(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getConsiderarParcelasMaterialDidaticoReajustePreco(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isValidarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.isValidarDebitoFinanceiroAoIncluirConvenioMatricula(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					
					
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, verificarAcesso, configuracaoGeralSistema, usuario);
			};
			getFacadeFactory().getContatoParceiroFacade().alterarContatoParceiros(obj.getCodigo(), obj.getContatoParceiroVOs());
			getFacadeFactory().getAreaProfissionalParceiroFacade().alterarAreaProfissionalParceiro(obj.getCodigo(), obj.getAreaProfissionalParceiroVOs());
			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
			getFacadeFactory().getUsuarioFacade().realizarValidacaoPersitirUsuario(obj, configuracaoGeralSistema, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void unificarParceiro(final Integer codParceiroManter, final Integer codParceiroRemover, UsuarioVO usuario) throws Exception {
		try {
			ParceiroVO parRemover = this.consultarPorChavePrimaria(codParceiroRemover, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, new UsuarioVO());
			ParceiroVO parManter = this.consultarPorChavePrimaria(codParceiroRemover, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, new UsuarioVO());
			Iterator<ContatoParceiroVO> i = parRemover.getContatoParceiroVOs().iterator();
			while (i.hasNext()) {
				ContatoParceiroVO cont = (ContatoParceiroVO) i.next();
				parManter.adicionarObjContatoParceiroVOs(cont);
			}
			this.alterar(parManter, true, new ConfiguracaoGeralSistemaVO(), new UsuarioVO());
			getFacadeFactory().getVagasFacade().alterarParceiro(codParceiroManter, codParceiroRemover);
			getFacadeFactory().getContatoParceiroFacade().excluirContatoParceiros(codParceiroRemover);
			@SuppressWarnings("unchecked")
			List<UsuarioVO> listaUsuario = getFacadeFactory().getUsuarioFacade().consultarPorCodigoParceiro(codParceiroRemover, false, Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO());
			if (!listaUsuario.isEmpty()) {
				getFacadeFactory().getUsuarioFacade().excluir(listaUsuario.get(0), usuario);
			}
			getFacadeFactory().getContaReceberFacade().alterarParceiro(codParceiroManter, codParceiroRemover, new UsuarioVO());
			final String sql = "DELETE FROM Parceiro WHERE (codigo = ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, codParceiroRemover);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarValorIsentarJuroMulta(final ParceiroVO parceiro, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Parceiro set isentarJuro=?, isentarMulta=?  WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, parceiro.isIsentarJuro());
				sqlAlterar.setBoolean(2, parceiro.isIsentarMulta());
				sqlAlterar.setInt(3, parceiro.getCodigo());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ParceiroVO</code>. Sempre localiza o registro a ser excluído através da chave
	 * primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ParceiroVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ParceiroVO obj, UsuarioVO usuario) throws Exception {
		try {
			Parceiro.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM Parceiro WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String tipoParceiro</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ParceiroVO> consultarPorTipoParceiro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( tipoParceiro ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoParceiro";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public Boolean consultarExisteVagaAtivaCadastrada(Integer codigoParceiro, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT codigo FROM Vagas WHERE parceiro = " + codigoParceiro + " AND situacao = 'AT'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String CPF</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ParceiroVO> consultarPorCPF(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( CPF ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CPF";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String CPF</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return ParceiroVO Contendo objeto da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public ParceiroVO consultarPorCPFUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE regexp_replace(cpf , '[^0-9]*', '', 'g') = ? AND (cpf IS NOT NULL OR cpf <> '') AND tipoempresa = 'FI' ORDER BY CPF limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (!tabelaResultado.next()) {
			return new ParceiroVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String CPF</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return ParceiroVO Contendo objeto da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public ParceiroVO consultarPorCNPJUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE regexp_replace(cnpj , '[^0-9]*', '', 'g') = ? AND (cnpj IS NOT NULL OR cnpj <> '') AND tipoempresa = 'JU' ORDER BY CNPJ limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (!tabelaResultado.next()) {
			return new ParceiroVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarPorCNPJ(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( CNPJ ) like('" + valorConsulta.toUpperCase() + "%') AND (cnpj IS NOT NULL OR cnpj <> '') ORDER BY CNPJ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String RG</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ParceiroVO> consultarPorRG(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( RG ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY RG";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String razaoSocial</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ParceiroVO> consultarPorRazaoSocial(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( sem_acentos(razaoSocial )) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY razaoSocial";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>String nome</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ParceiroVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper(sem_acentos(nome) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<ParceiroVO> consultarPorTipoSindicato(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper(sem_acentos(nome) ) like upper(sem_acentos(?)) AND tipoparceiro = 'SI' ORDER BY nome";
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + "%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarPorNomeDuploPercent(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( nome ) like('%" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Parceiro</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ParceiroVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarPorRGBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( RG ) like('" + valorConsulta.toUpperCase() + "%') AND participabancocurriculum = TRUE ORDER BY RG";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarPorCPFBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( CPF ) like('" + valorConsulta.toUpperCase() + "%') AND participabancocurriculum = TRUE ORDER BY CPF";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ParceiroVO> consultarPorRazaoSocialBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( razaoSocial ) like('" + valorConsulta.toUpperCase() + "%') AND participabancocurriculum = TRUE ORDER BY razaoSocial";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarParceiroInativoBancoCurriculumFalse(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( razaoSocial ) like('" + valorConsulta.toUpperCase() + "%') AND participabancocurriculum = FALSE ORDER BY razaoSocial";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarParceiroBancoCurriculumTrueComVaga(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct Parceiro.* FROM Parceiro INNER JOIN vagas ON vagas.parceiro = parceiro.codigo WHERE upper( razaoSocial ) like('" + valorConsulta.toUpperCase() + "%') AND participabancocurriculum = TRUE and vagas.situacao = 'AT' ORDER BY razaoSocial";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarPorNomeBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper(sem_acentos(nome) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ParceiroVO> consultarPorCNPJBancoCurriculumTrue(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE upper( CNPJ ) like('" + valorConsulta.toUpperCase() + "%') AND participabancocurriculum = TRUE ORDER BY CNPJ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ParceiroVO> consultarPorCodigoBancoCurriculumTrue(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Parceiro WHERE codigo >= " + valorConsulta.intValue() + " AND participabancocurriculum = TRUE ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ParceiroVO</code> resultantes da consulta.
	 */
	public static List<ParceiroVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ParceiroVO> vetResultado = new ArrayList<ParceiroVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ParceiroVO</code>.
	 * 
	 * @return O objeto da classe <code>ParceiroVO</code> com os dados devidamente montados.
	 */
	public static ParceiroVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ParceiroVO obj = new ParceiroVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
		obj.setCNPJ(dadosSQL.getString("CNPJ"));
		obj.setCPF(dadosSQL.getString("CPF"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setEmpresaSigilosaParaVaga(dadosSQL.getBoolean("empresaSigilosaParaVaga"));
		obj.setIsentarJuro(dadosSQL.getBoolean("isentarJuro"));
		obj.setIsentarMulta(dadosSQL.getBoolean("isentarMulta"));
		obj.setIsentarReajusteParcelaVencida(dadosSQL.getBoolean("isentarReajusteParcelaVencida"));
		obj.setFinanciamentoProprio(dadosSQL.getBoolean("financiamentoProprio"));
		obj.setEmitirBoletoEmNomeBeneficiado(dadosSQL.getBoolean("emitirBoletoEmNomeBeneficiado"));

		obj.setPermiteEmitirBoletoAlunoVinculadoParceiro(dadosSQL.getBoolean("permiteEmitirBoletoAlunoVinculadoParceiro"));
		obj.setPermiteRemessaBoletoAlunoVinculadoParceiro(dadosSQL.getBoolean("permiteRemessaBoletoAlunoVinculadoParceiro"));
		
		obj.setNumeroParcelaVencidasSuspenderFinanciamento(dadosSQL.getInt("numeroParcelaVencidasSuspenderFinanciamento"));
		obj.setRealizarReajustePrecoComBaseIndiceReajuste(dadosSQL.getBoolean("realizarReajustePrecoComBaseIndiceReajuste"));
		obj.setConsiderarParcelasMaterialDidaticoReajustePreco(dadosSQL.getBoolean("considerarParcelasMaterialDidaticoReajustePreco"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setCEP(dadosSQL.getString("CEP"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
		obj.setInscEstadual(dadosSQL.getString("inscEstadual"));
		obj.setTelComercial1(dadosSQL.getString("telComercial1"));
		obj.setTelComercial2(dadosSQL.getString("telComercial2"));
		obj.setTelComercial3(dadosSQL.getString("telComercial3"));
		obj.setSite(dadosSQL.getString("site"));
		obj.setFax(dadosSQL.getString("fax"));
		obj.setTipoEmpresa(dadosSQL.getString("tipoEmpresa"));
		obj.setRG(dadosSQL.getString("RG"));
		obj.setTipoParceiro(dadosSQL.getString("tipoParceiro"));
		obj.setParticipaBancoCurriculum(dadosSQL.getBoolean("participaBancoCurriculum"));
		obj.setInscMunicipal(dadosSQL.getString("inscricaoMunicipal"));
		obj.setDescricaoEmpresa(dadosSQL.getString("descricaoEmpresa"));
		obj.setEmpresaSigilosaParaVaga(dadosSQL.getBoolean("empresaSigilosaParaVaga"));
		obj.setIsentarTaxaBoleto(dadosSQL.getBoolean("isentarTaxaBoleto"));
		obj.setPossuiAliquotaEmissaoNotaEspecifica(dadosSQL.getBoolean("possuiAliquotaEmissaoNotaEspecifica"));

		obj.setConveniadaParaVagasEstagio(dadosSQL.getBoolean("conveniadaParaVagasEstagio"));
		obj.setDataInicioConvenioEstagio(dadosSQL.getDate("dataInicioConvenioEstagio"));
		obj.setDataFinalConvenioEstagio(dadosSQL.getDate("dataFinalConvenioEstagio"));
		obj.setNrAcordoConvenio(dadosSQL.getInt("nrAcordoConvenio"));
		obj.setRegistroConselho(dadosSQL.getString("registroConselho"));
		obj.setEmitirNotaFiscalParaBeneficiario(dadosSQL.getBoolean("emitirNotaFiscalParaBeneficiario"));
		obj.setOrgaoRegistroConselho(dadosSQL.getString("orgaoRegistroConselho"));
		if ((Uteis.isAtributoPreenchido(obj.getPossuiAliquotaEmissaoNotaEspecifica())) && (obj.getPossuiAliquotaEmissaoNotaEspecifica())) {
			obj.setIssqn(dadosSQL.getDouble("issqn"));
			obj.setPis(dadosSQL.getDouble("pis"));
			obj.setCofins(dadosSQL.getDouble("cofins"));
			obj.setInss(dadosSQL.getDouble("inss"));
			obj.setCsll(dadosSQL.getDouble("csll"));
			obj.setIRRF(dadosSQL.getDouble("irrf"));
		}
		obj.getCategoriaDespesa().setCodigo(new Integer(dadosSQL.getInt("categoriadespesa")));
		obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formapagamento")));
		obj.getFormaPagamentoRecebimento().setCodigo(new Integer(dadosSQL.getInt("formapagamentorecebimento")));
		obj.setPermiteenviarremessa(dadosSQL.getBoolean("permiteenviarremessa"));
		obj.setNomeBanco(dadosSQL.getString("nomeBanco"));
        obj.setNumeroBancoRecebimento(dadosSQL.getString("numeroBancoRecebimento"));
        obj.setNumeroAgenciaRecebimento(dadosSQL.getString("numeroAgenciaRecebimento"));
        obj.setContaCorrenteRecebimento(dadosSQL.getString("contaCorrenteRecebimento"));
        obj.setDigitoAgenciaRecebimento(dadosSQL.getString("digitoAgenciaRecebimento"));
        obj.setDigitoCorrenteRecebimento(dadosSQL.getString("digitoCorrenteRecebimento"));
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoChavePixEnum"))) {
			obj.setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("tipoIdentificacaoChavePixEnum")));
		}		
		obj.setChaveEnderecamentoPix(dadosSQL.getString("chaveEnderecamentoPix") );
        obj.setCusteaParcelasMaterialDidatico(dadosSQL.getBoolean("custeaParcelasMaterialDidatico"));
        obj.setConsiderarValorDescontoDeclaracaoImpostoRendaAluno(dadosSQL.getBoolean("considerarValorDescontoDeclaracaoImpostoRendaAluno"));
        obj.setValidarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula(dadosSQL.getBoolean("validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula"));
        obj.setValidarDebitoFinanceiroAoIncluirConvenioMatricula(dadosSQL.getBoolean("validarDebitoFinanceiroAoIncluirConvenioMatricula"));
		montarDadosCidade(obj, usuario);
		montarDadosCategoriaDespesa(obj, usuario);
		montarDadosFormaPagamento(obj, usuario);
		montarDadosFormaPagamentoRecebimento(obj, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaParceiroUnidadeEnsinoContaCorrente(getFacadeFactory().getParceiroUnidadeEnsinoContaCorrenteFacade().consultaRapidaPorParceiro(obj, usuario));
		obj.setContatoParceiroVOs(ContatoParceiro.consultarContatoParceiros(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		obj.setAreaProfissionalParceiroVOs(getFacadeFactory().getAreaProfissionalParceiroFacade().consultarPorParceiro(obj.getCodigo(), usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}

		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>PessoaVO</code>. Faz uso da
	 * chave primária da classe <code>CidadeVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCidade(ParceiroVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCidade().getCodigo().intValue() == 0) {
			obj.setCidade(new CidadeVO());
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}
	
	public static void montarDadosCategoriaDespesa(ParceiroVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
			obj.setCategoriaDespesa(new CategoriaDespesaVO());
			return;
		}
		obj.setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}
	
	public static void montarDadosFormaPagamento(ParceiroVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}
	
	public static void montarDadosFormaPagamentoRecebimento(ParceiroVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamentoRecebimento().getCodigo().intValue() == 0) {
			obj.setFormaPagamentoRecebimento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamentoRecebimento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoRecebimento().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ParceiroVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ParceiroVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Parceiro WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Parceiro ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean realizarVerificacaoDebitoFinanceiroAoIncluirConvenioMatricula(Integer parceiro) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT count(distinct contareceber.codigo) as qtd ");
			sql.append(" FROM parceiro ");
			sql.append(" inner join contareceber on  contareceber.parceiro = parceiro.codigo ");
			sql.append(" where parceiro.validarDebitoFinanceiroAoIncluirConvenioMatricula = true ");
			sql.append(" and contareceber.tipoorigem = 'BCC' and contareceber.situacao = 'AR' and contareceber.datavencimento < current_date ");
			sql.append(" and parceiro.codigo =  ").append(parceiro);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Boolean validarSeParceiroUtilizarFinanciamentoProprioPorCodigoConvenio(Integer convenio) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT count(distinct parceiro.codigo) as qtd ");
			sql.append(" FROM parceiro ");
			sql.append(" inner join convenio on  convenio.parceiro = parceiro.codigo ");
			sql.append(" where parceiro.financiamentoProprio = true ");
			sql.append(" and parceiro.numeroParcelaVencidasSuspenderFinanciamento is not null and parceiro.numeroParcelaVencidasSuspenderFinanciamento > 0 ");
			sql.append(" and convenio.codigo =  ").append(convenio);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw e;
		}
	}

	public ParceiroVO consultarPorMatriculaAluno(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sql = "select distinct parceiro.* from contareceber ";
		sql += " inner join convenio on convenio.codigo = contareceber.convenio";
		sql += " inner join parceiro on parceiro.codigo = convenio.parceiro";
		sql += " where convenio is not null ";
		sql += " and contareceber.matriculaaluno = ? order by parceiro.datacadastro desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula });
		if (!tabelaResultado.next()) {
			return new ParceiroVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public Integer consultarQuantidadeParceiroCadastrados() throws Exception {
		String sql = "select count(codigo)as qtdEmpresasCadastradas from parceiro where participabancocurriculum  = true";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (tabelaResultado.getInt("qtdEmpresasCadastradas"));
	}

	public Integer consultarQuantidadeParceiroInativados() throws Exception {
		String sql = "select count(codigo)as qtdEmpresasCadastradas from parceiro where participabancocurriculum  = false";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			return 0;
		}
		return (tabelaResultado.getInt("qtdEmpresasCadastradas"));
	}

	public String consultarDataUltimoCadastro() throws Exception {
		String sql = "select dataCadastro from parceiro where participabancocurriculum  = true order by codigo desc limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			return "";
		}
		return (Uteis.getData(tabelaResultado.getDate("dataCadastro")));
	}

	public Boolean validarUnicidadeNome(String nome) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT nome FROM parceiro where upper(nome) like upper('");
			sql.append(nome);
			sql.append("')");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean validarUnicidadeCPF(String cpf) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT nome FROM parceiro where cpf like '");
			sql.append(cpf);
			sql.append("'");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean validarUnicidadeCNPJ(String cnpj) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT nome FROM parceiro where cnpj like '");
			sql.append(cnpj);
			sql.append("'");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	public void realizarValidacaoParticipacaoBancoCurriculum(ParceiroVO parceiroVO) {
		if (parceiroVO.getParticipaBancoCurriculum() && parceiroVO.getDataAceitouParticiparBancoCurriculum() == null) {
			parceiroVO.setDataAceitouParticiparBancoCurriculum(new Date());
		} else {
			parceiroVO.setDataAceitouParticiparBancoCurriculum(null);
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosParceiroUnidadeEnsinoContaCorrenteVO(ParceiroUnidadeEnsinoContaCorrenteVO puecc) throws Exception {
		if (!Uteis.isAtributoPreenchido(puecc.getUnidadeEnsinoVO())) {
			throw new Exception("O campo Unidade Ensino deve ser Informada.");
		}
		if (!Uteis.isAtributoPreenchido(puecc.getContaCorrenteVO())) {
			throw new Exception("O campo Conta Corrente deve ser Informada.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarParceiroUnidadeEnsinoContaCorrenteVO(ParceiroVO parceiro, ParceiroUnidadeEnsinoContaCorrenteVO puecc, UsuarioVO usuario) throws Exception {
		int index = 0;
		validarDadosParceiroUnidadeEnsinoContaCorrenteVO(puecc);
		puecc.setParceiroVO(parceiro);
		puecc.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(puecc.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		puecc.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(puecc.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		for (ParceiroUnidadeEnsinoContaCorrenteVO objExistente : parceiro.getListaParceiroUnidadeEnsinoContaCorrente()) {
			if (objExistente.equalsParceiroUnidadeEnsinoContaCorrente(puecc)) {
				parceiro.getListaParceiroUnidadeEnsinoContaCorrente().set(index, puecc);
				return;
			}
			index++;
		}
		parceiro.getListaParceiroUnidadeEnsinoContaCorrente().add(puecc);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerParceiroUnidadeEnsinoContaCorrenteVO(ParceiroVO parceiro, ParceiroUnidadeEnsinoContaCorrenteVO puecc) throws Exception {
		Iterator<ParceiroUnidadeEnsinoContaCorrenteVO> i = parceiro.getListaParceiroUnidadeEnsinoContaCorrente().iterator();
		while (i.hasNext()) {
			ParceiroUnidadeEnsinoContaCorrenteVO objExistente = (ParceiroUnidadeEnsinoContaCorrenteVO) i.next();
			if (objExistente.equalsParceiroUnidadeEnsinoContaCorrente(puecc)) {
				i.remove();
				return;
			}
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return Parceiro.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Parceiro.idEntidade = idEntidade;
	}
}
