package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
//import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
//import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;
//import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
//import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
//import negocio.comuns.recursoshumanos.FaixaSalarialVO;
//import negocio.comuns.recursoshumanos.NivelSalarialVO;
//import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
//import negocio.comuns.recursoshumanos.enumeradores.TipoContratacaoComissionadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
//import negocio.facade.jdbc.academico.DisciplinasInteresse;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.administrativo.FuncionarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>FuncionarioVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>FuncionarioVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see FuncionarioVO
 * @see ControleAcesso
 */
@SuppressWarnings({"unchecked","static-access", "rawtypes"})
@Scope("singleton")
@Repository
@Lazy
public class Funcionario extends ControleAcesso implements FuncionarioInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;
	protected Hashtable funcionarioCargos;

	public Funcionario() throws Exception {
		super();
		setIdEntidade("Funcionario");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>FuncionarioVO</code>.
	 */
	public FuncionarioVO novo() throws Exception {
		Funcionario.incluir(getIdEntidade());
		FuncionarioVO obj = new FuncionarioVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>FuncionarioVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FuncionarioVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FuncionarioVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		boolean pessoaNaoExiste = false;
		try {
			incluir(getIdEntidade(), true, usuario);
			getFacadeFactory().getPessoaFacade().setIdEntidade(Funcionario.getIdEntidade());
			if (obj.getPessoa().getNovoObj()) {
				getFacadeFactory().getPessoaFacade().incluir(obj.getPessoa(), usuario, configuracaoGeralSistema, false);
				pessoaNaoExiste = true;
			} else {
				getFacadeFactory().getPessoaFacade().alterar(obj.getPessoa(), false, usuario, configuracaoGeralSistema, false);
			}
			if (!obj.getInformarMatricula() && obj.getMatricula().equals("")) {
				obj.setMatricula(gerarNumeroMatricula(obj, usuario, configuracaoGeralSistema));
			}
			FuncionarioVO.validarDados(obj);
			if (obj.getArquivoAssinaturaVO().getPastaBaseArquivoEnum() != null) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoAssinaturaVO(), false, usuario, configuracaoGeralSistema);
			}
			final String sql = "INSERT INTO Funcionario( dataAdmissao, pessoa ,matricula, exerceCargoAdministrativo, nomeBanco, numeroBancoRecebimento, numeroAgenciaRecebimento, contaCorrenteRecebimento, observacao, escolaridade, arquivoAssinatura, empresaRecebimento, cnpjEmpresaRecebimento, digitoAgenciaRecebimento, digitoCorrenteRecebimento, operacaoBancaria  , chaveEnderecamentoPix , tipoIdentificacaoChavePixEnum, naoNotificarInclusaoUsuario ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataAdmissao()));
					sqlInserir.setInt(2, obj.getPessoa().getCodigo().intValue());
					sqlInserir.setString(3, obj.getMatricula());
					sqlInserir.setBoolean(4, obj.getExerceCargoAdministrativo().booleanValue());
					sqlInserir.setString(5, obj.getNomeBanco());
					sqlInserir.setString(6, obj.getNumeroBancoRecebimento());
					sqlInserir.setString(7, obj.getNumeroAgenciaRecebimento());
					sqlInserir.setString(8, obj.getContaCorrenteRecebimento());
					sqlInserir.setString(9, obj.getObservacao());
					sqlInserir.setString(10, obj.getEscolaridade());
					sqlInserir.setInt(11, obj.getArquivoAssinaturaVO().getCodigo());
					sqlInserir.setString(12, obj.getEmpresaRecebimento());
					sqlInserir.setString(13, obj.getCnpjEmpresaRecebimento());
					sqlInserir.setString(14, obj.getDigitoAgenciaRecebimento());
					sqlInserir.setString(15, obj.getDigitoCorrenteRecebimento());
					sqlInserir.setString(16, obj.getOperacaoBancaria());
					sqlInserir.setString(17, obj.getChaveEnderecamentoPix());
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
							sqlInserir.setString(18, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
							sqlInserir.setNull(18, 0);
					}
					sqlInserir.setBoolean(19, obj.isNaoNotificarInclusaoUsuario());
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
//			getFacadeFactory().getFuncionarioCargoFacade().incluirFuncionarioCargos(obj.getCodigo(), obj.getFuncionarioCargoVOs());
//			getFacadeFactory().getFuncionarioDependenteInterfaceFacade().incluirFuncionarioDependentes(obj.getCodigo(), obj.getDependenteVOs());
//			getFacadeFactory().getHistoricoDependentesInterfaceFacade().persistirTodos(obj, obj.getDependenteVOs(), usuario);
			obj.setNovoObj(Boolean.FALSE); 
			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj.getPessoa(), usuario);
			atualizarSequenceMatriculaFuncionario();

			getFacadeFactory().getLogFuncionarioFacade().registrarLogFuncionario(obj, "Inserção", usuario);
		} catch (DuplicateKeyException dup) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setMatricula("");
			obj.getPessoa().setNovoObj(pessoaNaoExiste);
			throw new ConsistirException("Já existe um funcionário cadastrado com esta matrícula => " + dup.getMessage());
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setMatricula("");
			obj.getPessoa().setNovoObj(pessoaNaoExiste);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>FuncionarioVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FuncionarioVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FuncionarioVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			if (!obj.getInformarMatricula() && obj.getMatricula().equals("")) {
				obj.setMatricula(gerarNumeroMatricula(obj, usuario, configuracaoGeralSistema));
			}
			FuncionarioVO.validarDados(obj);
			alterar(getIdEntidade(), true, usuario);

			getFacadeFactory().getPessoaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getPessoaFacade().alterar(obj.getPessoa(), false, usuario, configuracaoGeralSistema, false);

			if (obj.getArquivoAssinaturaVO().getPastaBaseArquivoEnum() != null) {
				if (obj.getArquivoAssinaturaVO().getCodigo() == 0) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoAssinaturaVO(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoAssinaturaVO(), false, usuario, configuracaoGeralSistema);
				}
			}
			final String sql = "UPDATE Funcionario set dataAdmissao=?, pessoa=? , matricula=?, exerceCargoAdministrativo=?, nomeBanco=?, numeroBancoRecebimento=?, numeroAgenciaRecebimento=?, contaCorrenteRecebimento=?, observacao=?, escolaridade=?, arquivoAssinatura=?, empresaRecebimento=?, cnpjEmpresaRecebimento=?, digitoAgenciaRecebimento=?, digitoCorrenteRecebimento=?, operacaobancaria=? , chaveEnderecamentoPix=? , tipoIdentificacaoChavePixEnum=?, naoNotificarInclusaoUsuario=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataAdmissao()));
					sqlAlterar.setInt(2, obj.getPessoa().getCodigo().intValue());
					sqlAlterar.setString(3, obj.getMatricula());
					sqlAlterar.setBoolean(4, obj.getExerceCargoAdministrativo().booleanValue());
					sqlAlterar.setString(5, obj.getNomeBanco());
					sqlAlterar.setString(6, obj.getNumeroBancoRecebimento());
					sqlAlterar.setString(7, obj.getNumeroAgenciaRecebimento());
					sqlAlterar.setString(8, obj.getContaCorrenteRecebimento());
					sqlAlterar.setString(9, obj.getObservacao());
					sqlAlterar.setString(10, obj.getEscolaridade());
					sqlAlterar.setInt(11, obj.getArquivoAssinaturaVO().getCodigo());
					sqlAlterar.setString(12, obj.getEmpresaRecebimento());
					sqlAlterar.setString(13, obj.getCnpjEmpresaRecebimento());
					sqlAlterar.setString(14, obj.getDigitoAgenciaRecebimento());
					sqlAlterar.setString(15, obj.getDigitoCorrenteRecebimento());
					sqlAlterar.setString(16, obj.getDigitoCorrenteRecebimento());
					sqlAlterar.setString(17, obj.getChaveEnderecamentoPix());
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
						sqlAlterar.setString(18, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
						sqlAlterar.setNull(18, 0);
					}
					sqlAlterar.setBoolean(19, obj.isNaoNotificarInclusaoUsuario());
					sqlAlterar.setInt(20, obj.getCodigo().intValue());
					
					return sqlAlterar;
				}
			});
//			getFacadeFactory().getFuncionarioCargoFacade().alterarFuncionarioCargos(obj, obj.getFuncionarioCargoVOs());
//			getFacadeFactory().getFuncionarioDependenteInterfaceFacade().alterarFuncionarioDependentes(obj.getCodigo(), obj.getDependenteVOs());
//			getFacadeFactory().getHistoricoDependentesInterfaceFacade().persistirTodos(obj, obj.getDependenteVOs(), usuario);		
			getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj.getPessoa(), usuario);
			atualizarSequenceMatriculaFuncionario();
			getFacadeFactory().getUsuarioFacade().alterarBoleanoUsuarioPerfilAdministrador(obj.getPessoa().getCodigo(), obj.getExerceCargoAdministrativo(), false, usuario);
			getFacadeFactory().getLogFuncionarioFacade().registrarLogFuncionario(obj, "Alteração", usuario);
		} catch (DuplicateKeyException dup) {
			if(dup.getMessage().contains("funcionario_matricula_key")) {
				throw new ConsistirException("Já existe um funcionário cadastrado com esta matrícula.");
			}else if(dup.getMessage().contains("Prospect_unique_nomeEmail")) {
				throw new ConsistirException("Erro na alteração de FUNCIONÁRIO! Duplicação no Prospects.");
			}else {			
				throw dup;
			}
		} catch (Exception e) {
			if(e.getMessage().contains("CPF") || e.getMessage().contains("cpf")) {
				throw new ConsistirException("Erro na alteração de FUNCIONÁRIO! O CPF informado (DADOS PESSOAIS) pertence a outra pessoa.");
			}
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoNaoNotificarInsercaoUsuario(FuncionarioVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, "funcionario", new AtributoPersistencia()
				.add("naoNotificarInclusaoUsuario", obj.isNaoNotificarInclusaoUsuario())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String gerarNumeroMatricula(FuncionarioVO funcionarioVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		String prefixo = "";
		String numeroMatricula = "";
		String sqlStr = "SELECT nextval('matricula_funcionario_seq') as matriculaFuncionario; ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			numeroMatricula = (new Long(tabelaResultado.getLong("matriculaFuncionario"))).toString();
		}
		if (!configuracaoGeralSistema.getPrefixoMatriculaProfessor().equals("") && funcionarioVO.getPessoa().getProfessor()) {
			prefixo = configuracaoGeralSistema.getPrefixoMatriculaProfessor();
		}
		if (!configuracaoGeralSistema.getPrefixoMatriculaFuncionario().equals("") && !funcionarioVO.getPessoa().getProfessor()) {
			prefixo = configuracaoGeralSistema.getPrefixoMatriculaFuncionario();
		}
		if (!prefixo.equals("")) {
			numeroMatricula = prefixo + numeroMatricula;
		}

		String sqlStr2 = "SELECT matricula from funcionario where matricula = ? ";
		SqlRowSet tabRes = getConexao().getJdbcTemplate().queryForRowSet(sqlStr2, numeroMatricula);
		if (tabRes.next()) {
			numeroMatricula = gerarNumeroMatricula(funcionarioVO, usuario, configuracaoGeralSistema);
		} else {
			return numeroMatricula;
		}

		return numeroMatricula;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void unificarFuncionario(final Integer codFuncionarioManter, final Integer codFuncionarioRemover, UsuarioVO usuario) throws Exception {
		try {
			if (codFuncionarioManter.intValue() == codFuncionarioRemover.intValue()) {
	               throw new Exception("O funcionário manter não pode ser o mesmo do funcionário remover.");
	        }
			FuncionarioVO funcRemover = this.consultarPorChavePrimaria(codFuncionarioRemover, 0, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, new UsuarioVO());
			FuncionarioVO funcManter = this.consultarPorChavePrimaria(codFuncionarioManter, 0, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, new UsuarioVO());

//			Iterator<FuncionarioCargoVO> i = funcRemover.getFuncionarioCargoVOs().iterator();
//			while (i.hasNext()) {
//				FuncionarioCargoVO cont = i.next();
//				funcManter.adicionarObjFuncionarioCargoVOs(cont);
//			}
//			this.alterar(funcManter, usuario, new ConfiguracaoGeralSistemaVO());
//			if(!funcRemover.getPessoa().getCodigo().equals(funcManter.getPessoa().getCodigo())) {
//				getFacadeFactory().getMatriculaFacade().alterarPessoaMatriculaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getContaReceberFacade().alterarPessoaContaReceberUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getNegociacaoRecebimentoFacade().alterarPessoaNegociacaoRecebimentoUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getUsuarioFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo(), usuario);
//				getFacadeFactory().getRequerimentoFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getReservaFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getEmprestimoFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getChequeFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterarPessoaProfessorTitularDisciplinaTurmaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//				List<PessoaVO> lista = new ArrayList<PessoaVO>();
//				funcRemover.getPessoa().setSelecionado(true);
//				lista.add(funcRemover.getPessoa());
//				getFacadeFactory().getPessoaFacade().realizarUnificacaoPessoa(lista, funcManter.getPessoa(), usuario);
//			}
//			StringBuilder sb = new StringBuilder();
//			sb.append("select fn_removerduplicidadefuncionario(").append(funcRemover.getCodigo()).append(",").append(funcManter.getCodigo()).append(")" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
//			getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
//			this.excluirUnificarFuncionario(funcRemover, funcManter, usuario);

//			Iterator<FuncionarioCargoVO> i = funcRemover.getFuncionarioCargoVOs().iterator();
//			while (i.hasNext()) {
//				FuncionarioCargoVO cont = i.next();
//				funcManter.adicionarObjFuncionarioCargoVOs(cont);
//			}
//			this.alterar(funcManter, usuario, new ConfiguracaoGeralSistemaVO());
//			getFacadeFactory().getMatriculaFacade().alterarPessoaMatriculaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getProspectsFacade().alterarConsultorProspectUnificacaoFuncionario(funcManter.getCodigo() ,funcRemover.getCodigo(), usuario);
//			getFacadeFactory().getAgendaPessoaFacade().alterarAgendaPessoaUnificacaoFuncionario(funcManter.getPessoa().getCodigo(), funcRemover.getPessoa().getCodigo(), usuario);
//			getFacadeFactory().getMatriculaFacade().alterarConsultorMatriculaUnificacaoFuncionario(funcManter.getCodigo() ,funcRemover.getCodigo(), usuario);
//			getFacadeFactory().getCampanhaPublicoAlvoProspectFacade().alterarCampanhaPublicoAlvoProspectUnificacaoFuncionario(funcManter.getCodigo() ,funcRemover.getCodigo(), usuario);
//			getFacadeFactory().getContaCorrenteFacade().alterarFuncionarioContaCorrenteUnificacaoFuncionario(funcManter.getCodigo() ,funcRemover.getCodigo(), usuario);
//			getFacadeFactory().getContaReceberFacade().alterarPessoaContaReceberUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getNegociacaoRecebimentoFacade().alterarPessoaNegociacaoRecebimentoUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getUsuarioFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo(), usuario);
//			getFacadeFactory().getRequerimentoFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getReservaFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getEmprestimoFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getChequeFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarPessoaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getCursoCoordenadorFacade().alterarFuncionarioCursoCoordenadorUnificacaoFuncionario(funcRemover.getCodigo(), funcManter.getCodigo());
//			getFacadeFactory().getRequerimentoFacade().alterarFuncionarioRequerimentoUnificacaoFuncionario(funcRemover.getCodigo(), funcManter.getCodigo());
//			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterarPessoaProfessorTitularDisciplinaTurmaUnificacaoFuncionario(funcRemover.getPessoa().getCodigo(), funcManter.getPessoa().getCodigo());
//			getFacadeFactory().getHistoricoDependentesInterfaceFacade().alterarFuncionarioHistoricoDependentesUnificacaoFuncionario(funcRemover.getCodigo(), funcManter.getCodigo());
//			this.excluir(funcRemover, usuario);
			StringBuilder sb = new StringBuilder();
			sb.append("select fn_unificarFuncionario(").append(funcRemover.getCodigo()).append(",").append(funcRemover.getPessoa().getCodigo()).append(",").append(funcManter.getCodigo()).append(",").append(funcManter.getPessoa().getCodigo()).append(",").append(usuario.getCodigo()).append(")" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FuncionarioVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FuncionarioVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUnificarFuncionario(FuncionarioVO funcRemover, FuncionarioVO funcManter, UsuarioVO usuario) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuario);

			String sql = "DELETE FROM Funcionario WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { funcRemover.getCodigo() });
//			getFacadeFactory().getFuncionarioCargoFacade().excluirFuncionarioCargos(funcRemover.getCodigo());
//			getFacadeFactory().getFuncionarioDependenteInterfaceFacade().incluirFuncionarioDependentes(funcRemover.getCodigo(), funcRemover.getFuncionarioCargoVOs());
			if(!funcRemover.getPessoa().getCodigo().equals(funcManter.getPessoa().getCodigo())) {
				getFacadeFactory().getPessoaFacade().setIdEntidade(this.getIdEntidade());
				getFacadeFactory().getPessoaFacade().excluir(funcRemover.getPessoa(), usuario);
			}
			getFacadeFactory().getLogFuncionarioFacade().registrarLogFuncionario(funcRemover, "Exclusão", usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FuncionarioVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>FuncionarioVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FuncionarioVO obj, UsuarioVO usuario) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuario);

			String sql = "DELETE FROM Funcionario WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

//			getFacadeFactory().getFuncionarioCargoFacade().excluirFuncionarioCargos(obj.getCodigo());
//			getFacadeFactory().getFuncionarioDependenteInterfaceFacade().excluirFuncionarioDependente(obj.getCodigo());
			getFacadeFactory().getPessoaFacade().setIdEntidade(this.getIdEntidade());
			getFacadeFactory().getPessoaFacade().excluir(obj.getPessoa(), usuario);

			getFacadeFactory().getLogFuncionarioFacade().registrarLogFuncionario(obj, "Exclusão", usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public FuncionarioVO importarPessoaCadastrada(FuncionarioVO funcionarioVO, PessoaVO pessoaVO, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM Funcionario WHERE pessoa = " + pessoaVO.getCodigo() + " ";
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return funcionarioVO;
		} else {
			funcionarioVO = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			funcionarioVO.setNovoObj(Boolean.FALSE);
			return funcionarioVO;
		}

	}

	public List<FuncionarioVO> consultaRapidaPorNomePessoaAutoComplete(String valorConsulta, Integer codigoUnidadeEnsino, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct funcionario.codigo, pessoa.nome, pessoa.codigo as \"pessoa.codigo\" ");
		sqlStr.append("from funcionario ");
		sqlStr.append("LEFT JOIN pessoa ON funcionario.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN funcionarioCargo ON funcionarioCargo.funcionario = funcionario.codigo ");
		sqlStr.append("WHERE lower(pessoa.nome) like(?)  ");
		if (!(codigoUnidadeEnsino == null || codigoUnidadeEnsino == 0)) {
			sqlStr.append("AND funcionarioCargo.unidadeEnsino = ").append(codigoUnidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY pessoa.nome ");
		sqlStr.append(" limit ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		sqlStr = null;
		List<FuncionarioVO> listaFuncionario = new ArrayList<FuncionarioVO>();
		while (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			obj.getPessoa().setCodigo(new Integer(tabelaResultado.getInt("pessoa.codigo")));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			listaFuncionario.add(obj);
		}
		return listaFuncionario;
	}

	public List<FuncionarioVO> consultarConsultoresPropsectsUnificacao(Integer prospect1, Integer prospect2, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "select distinct funcionario.* from prospects inner join funcionario on funcionario.codigo = prospects.consultorpadrao where prospects.codigo in (" + prospect1 + ", " + prospect2 + ") ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<FuncionarioVO> consultarProfessoresParaCenso(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT funcionario.* FROM Funcionario inner join pessoa on funcionario.pessoa = pessoa.codigo where pessoa.professor and pessoa.ativo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Funcionario</code> através do
	 * valor do atributo <code>Date dataAdmissao</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FuncionarioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataAdmissao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Funcionario WHERE ((dataAdmissao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataAdmissao <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataAdmissao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<FuncionarioVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	 return consultarPorCPF(valorConsulta, unidadeEnsino, controlarAcesso, nivelMontarDados, true, usuario);
	}
	
	@Override
	public List<FuncionarioVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, boolean funcionarioAtivo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append("SELECT Funcionario.* FROM Funcionario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" WHERE ");
			sqlStr.append(" (replace(replace(Pessoa.CPF,'.',''),'-','')) like(?) ");
			if(funcionarioAtivo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");	
			}			
			sqlStr.append(" and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue() + " ");
		} else {
			sqlStr.append(" SELECT Funcionario.* FROM Funcionario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" WHERE (replace(replace(Pessoa.CPF,'.',''),'-','')) ");
			sqlStr.append(" like(?) ");
			if(funcionarioAtivo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");	
			}
		}
		sqlStr.append(" ORDER BY Pessoa.CPF");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta)+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public FuncionarioVO consultarPorCPFUnico(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * " + "from funcionario " + "inner join pessoa on funcionario.pessoa = pessoa.codigo " + "inner join funcioariocargo on funcionariocargo.funcionario = funcionario.codigo " + "where (replace(replace(cpf,'.',''),'-','')) = ? ";

		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr += " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY Pessoa.CPF";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, Uteis.retirarMascaraCPF(valorConsulta));
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List consultarPorCPF(String valorConsulta, String tipoPessoa, Boolean ativo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";

		sqlStr = "SELECT funcionario.* FROM Pessoa, Funcionario WHERE  (replace(replace(CPF,'.',''),'-','')) like(replace(replace(?,'.',''),'-','')) and pessoa.codigo=funcionario.pessoa ";

		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sqlStr = sqlStr + " and funcionario.codigo in (select distinct funcionario from FuncionarioCargo where unidadeEnsino = " + unidadeEnsino + " )";
			}

		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sqlStr = sqlStr + " and pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma";
				sqlStr = sqlStr + " where  turma.unidadeEnsino = " + unidadeEnsino + ") ";
			}
		}

		if ((tipoPessoa == null || tipoPessoa.isEmpty()) && unidadeEnsino != null && unidadeEnsino > 0) {
			sqlStr = sqlStr + " and (funcionario.codigo in (select distinct funcionario from FuncionarioCargo where unidadeEnsino = " + unidadeEnsino + " )";
			sqlStr = sqlStr + " or (pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma";
			sqlStr = sqlStr + " where  turma.unidadeEnsino = " + unidadeEnsino + "))) ";
		}

		if (ativo != null) {
			sqlStr = sqlStr + " and  pessoa.ativo = '" + ativo + "'";
		}
		sqlStr += " ORDER BY pessoa.CPF";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase()+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<FuncionarioVO> consultarPorNomeCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Funcionario.*,Cidade.* FROM Pessoa,Funcionario, Cidade WHERE Pessoa.cidade = Cidade.codigo and funcionario.pessoa = pessoa.codigo and  lower (sem_acentos(Cidade.nome)) like(sem_acentos(?)) ORDER BY Cidade.nome";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT Funcionario.*,Cidade.* FROM Pessoa, Funcionario, Cidade WHERE Pessoa.cidade = Cidade.codigo and funcionario.pessoa = pessoa.codigo and  lower (sem_acentos(Cidade.nome)) like(sem_acentos(?)) and Funcionario.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY  Cidade.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase()+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultarPorNomeCidade(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Funcionario.* FROM Pessoa,Funcionario, Cidade WHERE Pessoa.cidade = Cidade.codigo and funcionario.pessoa = pessoa.codigo and lower (sem_acentos(Cidade.nome)) like(sem_acentos(?)) ";
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		sqlStr = sqlStr + " ORDER BY Cidade.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase()+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Pessoa</code> através do valor
	 * do atributo e do tipo de pessoa <code>String nome</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido e que sejam
	 * do tipo de pessoa do parâmetro. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>PessoaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<FuncionarioVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorNome(valorConsulta, unidadeEnsino, controlarAcesso, nivelMontarDados, true, usuario);
	}

	public List<FuncionarioVO> consultarPorNome(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, boolean ativo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			// sqlStr =
			// "SELECT DISTINCT funcionario.* FROM Pessoa, Funcionario, FuncionarioCargo
			// WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND
			// (funcionario.pessoa = pessoa.codigo) and lower (Pessoa.nome) like('"
			// + valorConsulta.toLowerCase() +
			// "%') and FuncionarioCargo.unidadeEnsino = " +
			// unidadeEnsino.intValue() + " ORDER BY Pessoa.nome";
			sqlStr.append(" SELECT DISTINCT funcionario.*, pessoa.nome FROM Pessoa, Funcionario ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" WHERE (funcionario.pessoa = pessoa.codigo) ");
			sqlStr.append(" and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue());
			sqlStr.append(" and  lower (sem_acentos(Pessoa.nome)) ilike(sem_acentos(?)) ");
			if (ativo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");
			}
			sqlStr.append(" ORDER BY Pessoa.nome");

		} else {
			sqlStr.append("SELECT DISTINCT funcionario.*, pessoa.nome FROM Pessoa, Funcionario ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" WHERE (funcionario.pessoa = pessoa.codigo) and  lower(sem_acentos(Pessoa.nome)) ilike(sem_acentos(?)) ");
			if (ativo) {
				sqlStr.append(" and funcionariocargo.ativo = true  ");
			}
			sqlStr.append(" ORDER BY Pessoa.nome");

		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<FuncionarioVO> consultarPorNome(String valorConsulta, String tipoPessoa, Boolean ativo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";

		sqlStr = "SELECT funcionario.* FROM Pessoa, Funcionario WHERE lower (sem_acentos(pessoa.nome)) like(sem_acentos(?)) and pessoa.codigo=funcionario.pessoa ";

		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sqlStr = sqlStr + " and funcionario.codigo in (select distinct funcionario from FuncionarioCargo where unidadeEnsino = " + unidadeEnsino + " )";
			}

		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sqlStr = sqlStr + " and pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma";
				sqlStr = sqlStr + " where  turma.unidadeEnsino = " + unidadeEnsino + ") ";
			}
		}
		if ((tipoPessoa == null || tipoPessoa.isEmpty()) && unidadeEnsino != null && unidadeEnsino > 0) {
			sqlStr = sqlStr + " and (funcionario.codigo in (select distinct funcionario from FuncionarioCargo where unidadeEnsino = " + unidadeEnsino + " )";
			sqlStr = sqlStr + " or (pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma";
			sqlStr = sqlStr + " where  turma.unidadeEnsino = " + unidadeEnsino + "))) ";
		}

		if (ativo != null) {
			sqlStr += " and pessoa.ativo = '" + ativo + "' ";
		}
		sqlStr += " ORDER BY pessoa.nome";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorNomeCargo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append("SELECT distinct  Funcionario.*, pessoa.nome FROM Funcionario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" left join cargo on funcionariocargo.cargo = cargo.codigo  ");
			sqlStr.append(" WHERE ");
			sqlStr.append(" lower(sem_acentos(Cargo.nome)) ");
			sqlStr.append(" like(sem_acentos(?)) ");
			sqlStr.append(" and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue());
		} else {
			sqlStr.append("SELECT distinct Funcionario.*, pessoa.nome FROM Funcionario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" left join cargo on funcionariocargo.cargo = cargo.codigo  ");
			sqlStr.append(" WHERE ");
			sqlStr.append(" lower(sem_acentos(Cargo.nome)) ");
			sqlStr.append(" like(sem_acentos(?)) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT distinct Funcionario.*, pessoa.nome FROM Funcionario ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
		sqlStr.append(" left join cargo on funcionariocargo.cargo = cargo.codigo  ");
		sqlStr.append(" left join departamento on cargo.departamento = departamento.codigo  ");
		sqlStr.append(" WHERE ");
		sqlStr.append(" lower (sem_acentos(Departamento.nome)) like(sem_acentos(?)) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultarFuncionarioPorNomeDepartamentoAtivo(String nome, DepartamentoVO departamento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct funcionario.*, pessoa.nome from funcionario");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo");
		sqlStr.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo");
		sqlStr.append(" inner join departamento on cargo.departamento = departamento.codigo");
		sqlStr.append(" where departamento.codigo = " + departamento.getCodigo() + " and  sem_acentos(pessoa.nome) ilike sem_acentos(?) and pessoa.ativo = true ");
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+nome+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<FuncionarioVO> consultarFuncionarioPorNomeAtivo(String nome, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select distinct funcionario.*, pessoa.nome from funcionario");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");		
		sqlStr.append(" where lower(pessoa.nome) ilike '%" + nome + "%' and pessoa.ativo = true ");
		sqlStr.append(" ORDER BY pessoa.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeDepartamentoESemDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "SELECT DISTINCT Funcionario.*, Pessoa.nome FROM Funcionario, FuncionarioCargo, Pessoa, Cargo, Departamento WHERE " + " (FuncionarioCargo.funcionario = Funcionario.codigo)  " + " AND (FuncionarioCargo.cargo = Cargo.codigo) " + " AND (Cargo.departamento = Departamento.codigo)" + " and (pessoa.codigo = funcionario.pessoa) " + " and sem_acentos(Departamento.nome) ilike sem_acentos(?) ";

		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + "and (  funcionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
			sqlStr = sqlStr + " or funcionarioCargo.unidadeEnsino is null)";
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		sqlStr += " ORDER BY pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoDepartamentoESemDepartamento(Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Funcionario.*, Departamento.nome FROM Funcionario, Pessoa, Departamento, FuncionarioCargo, Cargo WHERE " + " FuncionarioCargo.funcionario = Funcionario.codigo  " + "AND FuncionarioCargo.cargo = Cargo.codigo " + "AND Cargo.departamento = Departamento.codigo and (pessoa.codigo = funcionario.pessoa) and Departamento.codigo = " + codigo + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + "and (  funcionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
			sqlStr = sqlStr + " or funcionarioCargo.unidadeEnsino is null)";
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		sqlStr += " ORDER BY Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeECodigoDepartamentoEMultiDepartamento(String nome, Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct  Departamento.nome,Pessoa.nome,Funcionario.* ");
		sb.append(" FROM Funcionario ");
		sb.append(" INNER JOIN pessoa           on pessoa.codigo               = funcionario.pessoa");
		sb.append(" inner  JOIN FuncionarioCargo on funcionario.codigo          = FuncionarioCargo.funcionario");
		sb.append(" inner  join Cargo            on FuncionarioCargo.cargo      = cargo.codigo");
		sb.append(" inner  join Departamento     on ((FuncionarioCargo.departamento is not null and FuncionarioCargo.departamento = Departamento.codigo) or (FuncionarioCargo.departamento is null and cargo.Departamento = Departamento.codigo ))");
		sb.append(" WHERE Departamento.codigo = ").append(codigo);
		sb.append(" AND sem_acentos(Pessoa.nome) ilike sem_acentos(?) and FuncionarioCargo.ativo and pessoa.ativo ");		
		sb.append(" ORDER BY Departamento.nome, Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), Uteis.isAtributoPreenchido(nome) ? nome : "%%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCPFECodigoDepartamentoEMultiDepartamento(String nome, Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Funcionario.*, Departamento.nome FROM Funcionario, Pessoa, Departamento, FuncionarioCargo, Cargo WHERE " + " lower (replace(replace(Pessoa.CPF,'.',''),'-','')) like(?) " + " AND FuncionarioCargo.funcionario = Funcionario.codigo  " + " AND FuncionarioCargo.cargo = Cargo.codigo " + " AND Cargo.departamento = Departamento.codigo and (pessoa.codigo = funcionario.pessoa) and Departamento.codigo = " + codigo + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + "and (  funcionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
			sqlStr = sqlStr + " or funcionarioCargo.unidadeEnsino is null)";
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		sqlStr += " ORDER BY Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, Uteis.retirarMascaraCPF(nome));
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorMatriculaECodigoDepartamentoEMultiDepartamento(String nome, Integer codigo, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Funcionario.*, Departamento.nome FROM Funcionario, Pessoa, Departamento, FuncionarioCargo, Cargo WHERE " + " Funcionario.matricula ilike(?) " + " AND FuncionarioCargo.funcionario = Funcionario.codigo  " + " AND FuncionarioCargo.cargo = Cargo.codigo " + " AND Cargo.departamento = Departamento.codigo and (pessoa.codigo = funcionario.pessoa) and Departamento.codigo = " + codigo + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + "and (  funcionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
			sqlStr = sqlStr + " or funcionarioCargo.unidadeEnsino is null)";
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		sqlStr += " ORDER BY Departamento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, nome);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Funcionario</code> através do
	 * valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>FuncionarioVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT Funcionario.*, Pessoa.nome FROM Funcionario,UnidadeEnsino,Pessoa,FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) and (pessoa.codigo = funcionario.pessoa) and sem_acentos (UnidadeEnsino.nome) ilike sem_acentos(?) ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		sqlStr += " ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Funcionario</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FuncionarioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM Funcionario,FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND codigo >= " + valorConsulta.intValue() + " ";
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		} else {
			sqlStr = "SELECT * FROM Funcionario WHERE codigo >= " + valorConsulta.intValue() + " ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public String consultarMatriculaFuncionarioPorCodigoPessoa(Integer valorConsulta, Integer unidadeEnsino) throws Exception {

		String sqlStr = "SELECT matricula FROM Funcionario WHERE pessoa = " + valorConsulta.intValue() + " ";
		// if (unidadeEnsino.intValue() != 0) {
		// sqlStr = sqlStr + " and unidadeEnsino = " + unidadeEnsino.intValue();
		// }
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (tabelaResultado.getString("matricula"));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Funcionario</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>FuncionarioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public FuncionarioVO consultarPorCodigoPessoa(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM Funcionario ");
		sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sqlStr.append(" WHERE pessoa = " + valorConsulta.intValue() + " ");
		// sqlStr.append(" and funcionariocargo.ativo = true ");
		// if (unidadeEnsino.intValue() != 0) {
		// sqlStr = sqlStr + " and unidadeEnsino = " + unidadeEnsino.intValue();
		// }
		sqlStr.append(" ORDER BY funcionario.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Pessoa).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Verifica se existe outro funcionário com a mesma matrícula
	 */
	public boolean validarUnicidadeMatricula(String matricula) throws Exception {
		String sqlStr = "SELECT matricula FROM Funcionario WHERE matricula ilike ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, matricula);
		if (tabelaResultado.next()) {
			throw new ConsistirException("Já existe um funcionário cadastrado com esta matrícula.");
		}
		return true;
	}

	public FuncionarioVO consultaRapidaPorCodigoPessoa(Integer codPessoa, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Funcionario.pessoa = ").append(codPessoa).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Funcionário).");
		}
		FuncionarioVO obj = new FuncionarioVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public FuncionarioVO consultaRapidaPorCodigoPessoaCenso(Integer codPessoa, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select funcionario.codigo AS \"funcionario.codigo\", funcionario.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome, pessoa.cpf, ");
		sb.append(" pessoa.dataNasc, pessoa.sexo, pessoa.corRaca, pessoa.nacionalidade, paiz.nome AS \"paiz.nome\", estado.sigla AS UFNascimento, pessoa.email AS \"pessoa.email\"  ");
		sb.append("  from funcionario  ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" inner join paiz on paiz.codigo = pessoa.nacionalidade ");
		sb.append(" left join cidade naturalidade on naturalidade.codigo = pessoa.naturalidade ");
		sb.append(" left join estado on estado.codigo = naturalidade.estado ");
		sb.append(" where pessoa.codigo = ").append(codPessoa);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		FuncionarioVO obj = new FuncionarioVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("funcionario.codigo"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCPF(tabelaResultado.getString("cpf"));
			obj.getPessoa().setSexo(tabelaResultado.getString("sexo"));
			obj.getPessoa().setCorRaca(tabelaResultado.getString("corRaca"));
			obj.getPessoa().setDataNasc(tabelaResultado.getDate("dataNasc"));
			obj.getPessoa().getNacionalidade().setCodigo(tabelaResultado.getInt("nacionalidade"));
			obj.getPessoa().getNacionalidade().setNome(tabelaResultado.getString("paiz.nome"));
			obj.getPessoa().getNaturalidade().getEstado().setSigla(tabelaResultado.getString("UFNascimento"));
			obj.getPessoa().setEmail(tabelaResultado.getString("pessoa.email"));
			obj.getPessoa().setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarPorCodigoPessoaTipo(obj.getPessoa().getCodigo(), "", false, usuario));
			return obj;
		}
		return obj;
	}

	@Override
	public FuncionarioVO consultaRapidaDiretorGeralPorCodigoUnidadeEnsino(Integer codUndiadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select funcionario.codigo AS \"funcionario.codigo\", pessoa.codigo AS \"pessoa.codigo\",  pessoa.cpf AS \"pessoa.cpf\", pessoa.nome AS \"pessoa.nome\",  ");
		sb.append(" pessoa.email AS \"pessoa.email\" ");
		sb.append(" from funcionario ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" left join unidadeensino on unidadeEnsino.diretorGeral = funcionario.codigo ");
		sb.append(" WHERE (unidadeEnsino.codigo = ").append(codUndiadeEnsino).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new FuncionarioVO();
		}
		FuncionarioVO obj = new FuncionarioVO();
		obj.setCodigo(tabelaResultado.getInt("funcionario.codigo"));
		obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
		obj.getPessoa().setCPF(tabelaResultado.getString("pessoa.cpf"));
		obj.getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
		obj.getPessoa().setEmail(tabelaResultado.getString("pessoa.email"));
		return obj;
	}

	public FuncionarioVO consultaRapidaConsultorPorMatricula(String matricula, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer str = new StringBuffer();
		str.append("SELECT DISTINCT funcionario.codigo, funcionario.exerceCargoAdministrativo, funcionario.matricula, funcionario.observacao, funcionario.escolaridade, funcionario.empresaRecebimento, funcionario.cnpjEmpresaRecebimento, pessoa.gerenciaPreInscricao AS \"funcionario.gerenciaPreInscricao\", pessoa.nome AS \"funcionario.nome\", pessoa.codigo AS \"funcionario.codigo\", cidade.nome AS \"cidade.nome\", pessoa.cpf AS \"pessoa.cpf\", pessoa.email AS \"pessoa.email\",  pessoa.tipoAssinaturaDocumentoEnum AS \"pessoa.tipoAssinaturaDocumentoEnum\", funcionarioCargo.codigo AS \"funcionarioCargo.codigo\", departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\" FROM funcionario ");
		str.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		str.append("LEFT JOIN usuario on usuario.pessoa = pessoa.codigo ");
		str.append("LEFT JOIN usuarioPerfilAcesso on usuarioPerfilAcesso.usuario = usuario.codigo ");
		str.append("LEFT JOIN UnidadeEnsino on usuarioPerfilAcesso.UnidadeEnsino = UnidadeEnsino.codigo ");
		str.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		str.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		str.append("LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo  ");
		str.append("LEFT JOIN departamento on cargo.departamento = departamento.codigo ");
		str.append("INNER JOIN matricula on matricula.consultor = funcionario.codigo ");
		str.append(" WHERE (matricula.matricula = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), matricula);
		if (!tabelaResultado.next()) {
			return new FuncionarioVO();
		}
		FuncionarioVO obj = new FuncionarioVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public FuncionarioVO consultaRapidaPorChavePrimaria(Integer codigo, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Funcionario.codigo = ").append(codigo).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new FuncionarioVO();// new
			// ConsistirException("Dados Não Encontrados.");
		}
		FuncionarioVO obj = new FuncionarioVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public FuncionarioVO consultaRapidaPorChavePrimariaSemExcecao(Integer codigo, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Funcionario.codigo = ").append(codigo).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		FuncionarioVO obj = new FuncionarioVO();
		if (!tabelaResultado.next()) {
			return obj;
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>FuncionarioVO</code>
	 *         resultantes da consulta.
	 */
	public  List<FuncionarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<FuncionarioVO> vetResultado = new ArrayList<FuncionarioVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe <code>FuncionarioVO</code>.
	 * 
	 * @return O objeto da classe <code>FuncionarioVO</code> com os dados
	 *         devidamente montados.
	 */
	public  FuncionarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FuncionarioVO obj = new FuncionarioVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.setMatricula(dadosSQL.getString("matricula"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}

		obj.setNomeBanco(dadosSQL.getString("nomeBanco"));
		obj.setNumeroBancoRecebimento(dadosSQL.getString("numeroBancoRecebimento"));
		obj.setNumeroAgenciaRecebimento(dadosSQL.getString("numeroAgenciaRecebimento"));
		obj.setContaCorrenteRecebimento(dadosSQL.getString("contaCorrenteRecebimento"));
		obj.setOperacaoBancaria(dadosSQL.getString("operacaobancaria"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoChavePixEnum"))) {
			obj.setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("tipoIdentificacaoChavePixEnum")));
		}		
		obj.setChaveEnderecamentoPix(dadosSQL.getString("chaveEnderecamentoPix") );
		obj.setEscolaridade(dadosSQL.getString("escolaridade"));
		obj.setEmpresaRecebimento(dadosSQL.getString("empresaRecebimento"));
		obj.setCnpjEmpresaRecebimento(dadosSQL.getString("cnpjEmpresaRecebimento"));
		obj.getArquivoAssinaturaVO().setCodigo(dadosSQL.getInt("arquivoAssinatura"));
		obj.setDigitoAgenciaRecebimento(dadosSQL.getString("digitoAgenciaRecebimento"));
		obj.setDigitoCorrenteRecebimento(dadosSQL.getString("digitoCorrenteRecebimento"));
		obj.setNaoNotificarInclusaoUsuario(dadosSQL.getBoolean("naoNotificarInclusaoUsuario"));
//		obj.setDependenteVOs(getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarPorFuncionario(obj.getCodigo(), false, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		obj.setDataAdmissao(dadosSQL.getDate("dataAdmissao"));
		obj.setExerceCargoAdministrativo(dadosSQL.getBoolean("exerceCargoAdministrativo"));
		obj.setNovoObj(Boolean.FALSE);
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO)
			return obj;
		
		montarDadosArquivo(obj);
		montarDadosPessoa(obj, nivelMontarDados, usuario);

//		obj.inicializarCentroCustoFuncionario();

//		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
//			obj.setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(obj.getCodigo(), false, nivelMontarDados, usuario));			
//		}
		return obj;
	}
	public  FuncionarioVO montarDadosHistorico(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FuncionarioVO obj = new FuncionarioVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa")));
		obj.setMatricula(dadosSQL.getString("matricula"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}

		
	
		obj.setEscolaridade(dadosSQL.getString("escolaridade"));		
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		
		obj.setNovoObj(Boolean.FALSE);
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO)
			return obj;
		
		//montarDadosArquivo(obj);
		//monta dados basicos para pessoas para carregamento de titulaçao professor
		montarDadosPessoaBasico(obj, nivelMontarDados, usuario);

		//obj.inicializarCentroCustoFuncionario();

//		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
//			obj.setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(obj.getCodigo(), false, nivelMontarDados, usuario));			
//		}
		return obj;
	}

	public  void montarDadosArquivo(FuncionarioVO obj) throws Exception {
		if (obj.getArquivoAssinaturaVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoAssinaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
	}

	public FuncionarioVO consultarPorChavePrimaria(Integer codigoPrm, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT funcionario.* FROM Funcionario LEFT JOIN FuncionarioCargo on Funcionario.codigo = FuncionarioCargo.funcionario WHERE Funcionario.codigo = ?";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql = sql + " and FuncionarioCargo.unidadeEnsino = ? ";
		}
		SqlRowSet tabelaResultado;
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue(), unidadeEnsino.intValue() });
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		}
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		FuncionarioVO func = new FuncionarioVO();
		func.setCodigo(codigoPrm);
		return func;
	}

	public FuncionarioVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT funcionario.* FROM Funcionario WHERE Funcionario.codigo = ?";
		SqlRowSet tabelaResultado;
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		FuncionarioVO func = new FuncionarioVO();
		func.setCodigo(codigoPrm);
		return func;
	}

	public String consultarNomeFuncionarioPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select initcap(pessoa.nome) AS nome from funcionario  ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where funcionario.codigo = ? ");
		SqlRowSet tabelaResultado;
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { codigoPrm.intValue() });
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("nome");
		}
		return "";
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>FuncionarioVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosPessoa(FuncionarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
//		obj.setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(obj.getCodigo(), false, nivelMontarDados, usuario));
	}
	public  void montarDadosPessoaBasico(FuncionarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
//		obj.setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(obj.getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return Funcionario.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Funcionario.idEntidade = idEntidade;
	}

	public List consultarPorMatricula(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT funcionario.* FROM Funcionario,Pessoa,FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND lower (funcionario.matricula) like( '" + valorConsulta.toLowerCase() + "%') and pessoa.codigo=funcionario.pessoa ";
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY funcionario.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<FuncionarioVO> consultarPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorMatricula(valorConsulta, unidadeEnsino, nivelMontarDados, true, usuario);
	}

	public List<FuncionarioVO> consultarPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, boolean funcionarioAtivo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
		StringBuffer sqlStr = new StringBuffer();
		if (unidadeEnsino.intValue() == 0) {
			sqlStr.append("SELECT funcionario.* FROM Funcionario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" WHERE  (funcionario.matricula) ilike( ?)  ");
			if(funcionarioAtivo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");	
			}
		} else {
			sqlStr.append("SELECT DISTINCT funcionario.* FROM Funcionario,Pessoa,FuncionarioCargo ");
			sqlStr.append(" WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND (funcionario.matricula) ilike( ?) and pessoa.codigo=funcionario.pessoa ");
			sqlStr.append(" and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue());
			if(funcionarioAtivo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");	
			}
			sqlStr.append(" ORDER BY funcionario.matricula");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public FuncionarioVO consultarPorMatricula(String prm, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT funcionario.* FROM Funcionario,Pessoa,FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND funcionario.matricula ilike ? and pessoa.codigo=funcionario.pessoa ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " and funcionariocargo.ativo = true ";
		sqlStr += " ORDER BY funcionario.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, prm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Funcionário).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public FuncionarioVO consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT * FROM Funcionario WHERE UPPER(funcionario.matricula) = ? ";
		sqlStr += " ORDER BY funcionario.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, matricula.toUpperCase());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	public FuncionarioVO consultarFuncionarioPorMatricula(String prm, String tipoPessoa, Boolean ativo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);

		String sqlStr = "SELECT funcionario.* FROM Pessoa, Funcionario WHERE   (funcionario.matricula) ilike ? and pessoa.codigo=funcionario.pessoa ";

		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + " and pessoa.funcionario = 'true'";
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sqlStr = sqlStr + " and funcionario.codigo in (select distinct funcionario from FuncionarioCargo where unidadeEnsino = " + unidadeEnsino + " )";
			}

		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sqlStr = sqlStr + " and pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma";
				sqlStr = sqlStr + " where  turma.unidadeEnsino = " + unidadeEnsino + ") ";
			}
		}

		if ((tipoPessoa == null || tipoPessoa.isEmpty()) && unidadeEnsino != null && unidadeEnsino > 0) {
			sqlStr = sqlStr + " and (funcionario.codigo in (select distinct funcionario from FuncionarioCargo where unidadeEnsino = " + unidadeEnsino + " )";
			sqlStr = sqlStr + " or (pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma";
			sqlStr = sqlStr + " where  turma.unidadeEnsino = " + unidadeEnsino + "))) ";
		}

		if (ativo != null) {
			sqlStr += " and pessoa.ativo = '" + ativo + "' ";
		}
		sqlStr += " ORDER BY funcionario.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, prm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));

	}

	public FuncionarioVO consultarPorRequisitanteMatricula(String campoConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT Funcionario.* FROM Funcionario, FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND matricula ilike ? ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, campoConsulta);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Funcionário).");
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorRG(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT funcionario.*, Pessoa.* FROM Pessoa, Funcionario, FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND pessoa.RG ilike ? and pessoa.codigo=funcionario.pessoa ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY pessoa.RG";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorRG(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT Funcionario.*, Pessoa.* FROM Pessoa, Funcionario, FuncionarioCargo WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND Pessoa.RG ilike ? and pessoa.codigo=funcionario.pessoa ";
		if (tipoPessoa.equals("FU")) {
			sqlStr = sqlStr + "and pessoa.funcionario = 'true'";
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr = sqlStr + " and pessoa.professor = 'true'";
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = sqlStr + " and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY Pessoa.RG";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public FuncionarioVO consultarProfessorPorMatriculaDisciplina(String matricula, Integer disciplina, Integer unidadeEnsino, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN professorministrouaulaturma ON pessoa.codigo = professorministrouaulaturma.professor");
		sqlStr.append(" LEFT JOIN formacaoacademica ON formacaoacademica.pessoa = pessoa.codigo");
		sqlStr.append(" INNER JOIN registroaula ON registroaula.professor = pessoa.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.turma = professorministrouaulaturma.turma AND matriculaperiodoturmadisciplina.disciplina = professorministrouaulaturma.disciplina");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" WHERE matricula.matricula = ? AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina.intValue()).append(" AND professorministrouaulaturma.titular = ").append(verificarTitular);
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			return null;
			// throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FuncionarioVO consultarProfessorTitularPorMatriculaDisciplina(String matricula, Integer disciplina, Integer unidadeEnsino, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN professortitulardisciplinaturma ON pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" LEFT JOIN formacaoacademica ON formacaoacademica.pessoa = pessoa.codigo");
		sqlStr.append(" INNER JOIN registroaula ON registroaula.professor = pessoa.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.turma = professortitulardisciplinaturma.turma AND matriculaperiodoturmadisciplina.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" WHERE matricula.matricula = ? AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina.intValue()).append(" AND professortitulardisciplinaturma.titular = ").append(verificarTitular);
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			return null;
			// throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FuncionarioVO consultarProfessorTitularPorMatriculaDisciplinaSemRegistroAulta(String matricula, Integer disciplina, String nomeProfessor, Integer unidadeEnsino, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados,boolean historicoPorEquivalencia, String anoDisciplina , String semestreDisciplina, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		SqlRowSet tabelaResultado = null;
		
		//Buscando Professor Titular da disciplina de turma teorica
		StringBuilder sqlStr = new StringBuilder("select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and matriculaperiodoturmadisciplina.turmateorica is not null");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turmateorica");
		sqlStr.append(" and professortitulardisciplinaturma.titular = ").append(verificarTitular);
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append(" union all");
		//Buscando Professor Titular da disciplina de turma Pratica
		sqlStr.append(" select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and matriculaperiodoturmadisciplina.turmapratica is not null");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turmapratica");
		sqlStr.append(" and professortitulardisciplinaturma.titular = true");
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append(" union all");
		//Buscando Professor Titular da disciplina Regular
		sqlStr.append(" select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" and professortitulardisciplinaturma.titular = true");
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append(" union all");
		//Buscando Professor Titular da disciplina Equivalente
		sqlStr.append("	");
		sqlStr.append(" select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.mapaequivalenciadisciplina is not null and historico.historicoporequivalencia");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" inner join historico as hisequivalencia on hisequivalencia.matricula = matriculaperiodo.matricula  and hisequivalencia.mapaequivalenciadisciplina = historico.mapaequivalenciadisciplina");
		sqlStr.append(" and hisequivalencia.numeroagrupamentoequivalenciadisciplina = historico.numeroagrupamentoequivalenciadisciplina and hisequivalencia.historicoequivalente and hisequivalencia.mapaequivalenciadisciplinacursada is not null");
		sqlStr.append(" and hisequivalencia.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = hisequivalencia.matriculaperiodoturmadisciplina");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turma and historico.matriculaperiodoturmadisciplina is null ");
		sqlStr.append(" and professortitulardisciplinaturma.titular = true");
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append("");
		sqlStr.append(" union all");
		//Buscando Professor Titular da Turma Teorica de uma disciplina Equivalente
		sqlStr.append(" select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.mapaequivalenciadisciplina is not null and historico.historicoporequivalencia");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" inner join historico as hisequivalencia on hisequivalencia.matricula = matriculaperiodo.matricula  and hisequivalencia.mapaequivalenciadisciplina = historico.mapaequivalenciadisciplina");
		sqlStr.append(" and hisequivalencia.numeroagrupamentoequivalenciadisciplina = historico.numeroagrupamentoequivalenciadisciplina and hisequivalencia.historicoequivalente and hisequivalencia.mapaequivalenciadisciplinacursada is not null");
		sqlStr.append(" and hisequivalencia.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = hisequivalencia.matriculaperiodoturmadisciplina");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turmateorica and historico.matriculaperiodoturmadisciplina is null ");
		sqlStr.append(" and professortitulardisciplinaturma.titular = true");
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append("");
		sqlStr.append(" union all");
		//Buscando Professor Titular da Turma Pratica de uma disciplina Equivalente
		sqlStr.append(" select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.mapaequivalenciadisciplina is not null and historico.historicoporequivalencia");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" inner join historico as hisequivalencia on hisequivalencia.matricula = matriculaperiodo.matricula  and hisequivalencia.mapaequivalenciadisciplina = historico.mapaequivalenciadisciplina");
		sqlStr.append(" and hisequivalencia.numeroagrupamentoequivalenciadisciplina = historico.numeroagrupamentoequivalenciadisciplina and hisequivalencia.historicoequivalente and hisequivalencia.mapaequivalenciadisciplinacursada is not null");
		sqlStr.append(" and hisequivalencia.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = hisequivalencia.matriculaperiodoturmadisciplina");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodoturmadisciplina.turmapratica and historico.matriculaperiodoturmadisciplina is null ");
		sqlStr.append(" and professortitulardisciplinaturma.titular = true");
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append("");
		sqlStr.append(" union all");
		//Buscando Professor Titular da Turma de disciplina sem matriculaPeriodoTurmaDisciplina
		sqlStr.append(" select distinct funcionario .* from professortitulardisciplinaturma");
		sqlStr.append(" INNER JOIN funcionario on  funcionario.pessoa = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join pessoa on pessoa.codigo = professortitulardisciplinaturma.professor");
		sqlStr.append(" inner join historico on historico.disciplina = professortitulardisciplinaturma.disciplina");
		sqlStr.append(" and  historico.anohistorico = professortitulardisciplinaturma.ano ");
		sqlStr.append(" and  historico.semestrehistorico = professortitulardisciplinaturma.semestre");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" where professortitulardisciplinaturma.turma = matriculaperiodo.turma and historico.matriculaperiodoturmadisciplina is null");
		sqlStr.append(" and professortitulardisciplinaturma.titular = true");
		sqlStr.append(" and historico.matricula = '").append(matricula).append("' and historico.disciplina = ").append(disciplina);
		if (Uteis.isAtributoPreenchido(anoDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.ano = '").append(anoDisciplina).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestreDisciplina)) {
			sqlStr.append(" and professortitulardisciplinaturma.semestre = '").append(semestreDisciplina).append("'");
		}
		sqlStr.append(" limit 1");
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FuncionarioVO consultarProfessorComAulaProgramadaPorMatriculaDisciplina(String matricula, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);

		StringBuilder sqlStr = new StringBuilder(" SELECT DISTINCT funcionario.*, ra.data FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN registroaula ra ON ra.disciplina = ").append(disciplina.intValue());
		sqlStr.append(" AND ra.professor = pessoa.codigo");
		sqlStr.append(" INNER JOIN frequenciaaula fa ON fa.registroaula = ra.codigo AND fa.matricula = ? ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" INNER JOIN Matricula on Matricula.matricula = fa.matricula AND unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" order by ra.data desc limit 1");
		// StringBuilder sqlStr = new
		// StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		// sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		// sqlStr.append(" INNER JOIN HorarioTurmaProfessorDisciplina ON pessoa.codigo =
		// HorarioTurmaProfessorDisciplina.professor ");
		// //
		// sqlStr.append(" LEFT JOIN formacaoacademica ON formacaoacademica.pessoa =
		// pessoa.codigo");
		// sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON
		// matriculaperiodoturmadisciplina.turma = HorarioTurmaProfessorDisciplina.turma
		// AND matriculaperiodoturmadisciplina.disciplina =
		// HorarioTurmaProfessorDisciplina.disciplina");
		// sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo =
		// matriculaperiodoturmadisciplina.matriculaperiodo");
		// sqlStr.append(" INNER JOIN matricula ON matricula.matricula =
		// matriculaperiodo.matricula");
		// sqlStr.append(" INNER JOIN registroaula ra ON ra.turma =
		// matriculaperiodoturmadisciplina.turma AND ra.disciplina =
		// matriculaperiodoturmadisciplina.disciplina");
		// sqlStr.append(" AND ra.professor =
		// HorarioTurmaProfessorDisciplina.professor");
		// sqlStr.append(" INNER JOIN frequenciaaula fa ON fa.registroaula = ra.codigo
		// AND fa.matricula = matricula.matricula");
		// sqlStr.append(" WHERE matricula.matricula = '").append(matricula).append("'
		// AND matriculaperiodoturmadisciplina.disciplina =
		// ").append(disciplina.intValue());
		// if (unidadeEnsino.intValue() != 0) {
		// sqlStr.append(" AND unidadeEnsino = ").append(unidadeEnsino.intValue());
		// }
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	public FuncionarioVO consultarProfessorComAulaProgramadaPorMatriculaDisciplinaNome(String matricula, Integer disciplina, String nomeProfessor, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN HorarioTurmaProfessorDisciplina ON pessoa.codigo = HorarioTurmaProfessorDisciplina.professor ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.turma = HorarioTurmaProfessorDisciplina.turma AND matriculaperiodoturmadisciplina.disciplina = HorarioTurmaProfessorDisciplina.disciplina");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" INNER JOIN registroaula ra ON ra.turma = matriculaperiodoturmadisciplina.turma AND ra.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" AND ra.professor = HorarioTurmaProfessorDisciplina.professor");
		sqlStr.append(" INNER JOIN frequenciaaula fa ON fa.registroaula = ra.codigo AND fa.matricula = matricula.matricula");
		sqlStr.append(" WHERE matricula.matricula = ? AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina.intValue());
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if (!nomeProfessor.equals("")) {
			sqlStr.append(" AND upper(pessoa.nome) = ? ");
		}
		SqlRowSet tabelaResultado = null;
		if (!nomeProfessor.equals("")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula, nomeProfessor.toUpperCase());
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		}
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	public FuncionarioVO SegundoconsultarProfessorComAulaProgramadaPorMatriculaDisciplinaNome(String matricula, Integer disciplina, String nomeProfessor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN HorarioTurmaProfessorDisciplina ON pessoa.codigo = HorarioTurmaProfessorDisciplina.professor ");
		sqlStr.append(" LEFT JOIN formacaoacademica ON formacaoacademica.pessoa = pessoa.codigo");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.turma = HorarioTurmaProfessorDisciplina.turma AND matriculaperiodoturmadisciplina.disciplina = HorarioTurmaProfessorDisciplina.disciplina");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" INNER JOIN registroaula ra ON ra.turma = matriculaperiodoturmadisciplina.turma AND ra.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" AND ra.professor = HorarioTurmaProfessorDisciplina.professor");
		sqlStr.append(" INNER JOIN frequenciaaula fa ON fa.registroaula = ra.codigo AND fa.matricula = matricula.matricula");
		sqlStr.append(" WHERE matricula.matricula = ? AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina.intValue());
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (!nomeProfessor.equals("")) {
			sqlStr.append(" AND upper(pessoa.nome) = ? ");
		}
		SqlRowSet tabelaResultado = null;
		if (!nomeProfessor.equals("")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula, nomeProfessor.toUpperCase());
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		}
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	
	}
	public FuncionarioVO consultarProfessorTutoriaOnlineDisciplina(String matricula, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct funcionario.* from programacaotutoriaonline ");
		sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
		sqlStr.append(" inner join funcionario on funcionario.codigo = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.turma = programacaotutoriaonline.turma and mptd.disciplina = programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join matricula on matricula.matricula = mptd.matricula ");
		sqlStr.append(" where matricula.matricula = ? ");
		if (disciplina.intValue() > 0) {
			sqlStr.append(" AND mptd.disciplina = ").append(disciplina.intValue());
		}
		SqlRowSet tabelaResultado = null;
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public void adicionarObjFuncionarioCargos(FuncionarioCargoVO obj) throws Exception {
		getFuncionarioCargos().put(obj.getCargo().getCodigo(), obj);
	}

	public void excluirObjTelefonePostoAtendimentos(Integer cargo) throws Exception {
		getFuncionarioCargos().remove(cargo);
	}

	/**
	 * @return the funcionarioCargos
	 */
	public Hashtable getFuncionarioCargos() {
		return funcionarioCargos;
	}

	/**
	 * @param funcionarioCargos
	 *            the funcionarioCargos to set
	 */
	public void setFuncionarioCargos(Hashtable funcionarioCargos) {
		this.funcionarioCargos = funcionarioCargos;
	}

	/**
	 * Responsável por realizar uma consulta de <code>UnidadeEnsino</code> através
	 * do valor do atributo <code>String nome</code>. Retorna os objetos, com início
	 * do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UnidadeEnsinoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 * 
	 */
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT DISTINCT funcionario.codigo, funcionario.exerceCargoAdministrativo, funcionario.matricula, ")
		.append(" funcionario.observacao, funcionario.escolaridade, funcionario.empresaRecebimento, funcionario.cnpjEmpresaRecebimento, ")
		.append(" pessoa.gerenciaPreInscricao AS \"funcionario.gerenciaPreInscricao\", pessoa.nome AS \"funcionario.nome\", ")
		.append(" pessoa.codigo AS \"funcionario.codigo\", cidade.nome AS \"cidade.nome\", pessoa.cpf AS \"pessoa.cpf\", ")
		.append(" pessoa.tipoAssinaturaDocumentoEnum AS \"pessoa.tipoAssinaturaDocumentoEnum\", ")
		.append(" pessoa.email AS \"pessoa.email\" FROM funcionario ");
		str.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		str.append("LEFT JOIN usuario on usuario.pessoa = pessoa.codigo ");
		str.append("LEFT JOIN usuarioPerfilAcesso on usuarioPerfilAcesso.usuario = usuario.codigo ");
		str.append("LEFT JOIN UnidadeEnsino on usuarioPerfilAcesso.UnidadeEnsino = UnidadeEnsino.codigo ");
		str.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		str.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		str.append("LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo  ");
		str.append("LEFT JOIN departamento on cargo.departamento = departamento.codigo ");
		str.append("LEFT JOIN secaofolhapagamento as secao on secao.codigo = funcionarioCargo.secaofolhapagamento ");

		// str.append("LEFT JOIN unidadeEnsino on funcionarioCargo.unidadeEnsino =
		// unidadeEnsino.codigo ");
		return str;
	}

	private StringBuffer getSQLCountPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT COUNT(distinct funcionario.codigo) as qtde FROM funcionario ");
		str.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		str.append("LEFT JOIN usuario on usuario.pessoa = pessoa.codigo ");
		str.append("LEFT JOIN usuarioPerfilAcesso on usuarioPerfilAcesso.usuario = usuario.codigo ");
		str.append("LEFT JOIN UnidadeEnsino on usuarioPerfilAcesso.UnidadeEnsino = UnidadeEnsino.codigo ");
		str.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		str.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		str.append("LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo  ");
		str.append("LEFT JOIN departamento on cargo.departamento = departamento.codigo ");
		str.append("LEFT JOIN secaofolhapagamento as secao on secao.codigo = funcionarioCargo.secaofolhapagamento ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaProfessorTitularDisciplinaTurma() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT distinct funcionario.codigo, funcionario.exerceCargoAdministrativo, funcionario.matricula, funcionario.observacao, funcionario.escolaridade, funcionario.empresaRecebimento, funcionario.cnpjEmpresaRecebimento, pessoa.gerenciaPreInscricao AS \"funcionario.gerenciaPreInscricao\",  ");
		str.append("pessoa.nome AS \"funcionario.nome\", pessoa.codigo AS \"funcionario.codigo\", cidade.nome AS \"cidade.nome\", pessoa.cpf AS \"pessoa.cpf\", pessoa.email AS \"pessoa.email\", ");
		str.append("funcionarioCargo.codigo AS \"funcionarioCargo.codigo\", horarioturma.semestrevigente as semestre, horarioturma.anovigente as ano, departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\"  ");
		str.append(" FROM funcionario ");
		str.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		str.append("LEFT JOIN usuario on usuario.pessoa = pessoa.codigo ");
		str.append("LEFT JOIN usuarioPerfilAcesso on usuarioPerfilAcesso.usuario = usuario.codigo ");
		str.append("LEFT JOIN UnidadeEnsino on usuarioPerfilAcesso.UnidadeEnsino = UnidadeEnsino.codigo ");
		str.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		str.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		str.append("LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo  ");
		str.append("LEFT JOIN departamento on cargo.departamento = departamento.codigo ");
		str.append("inner JOIN horarioturmadiaitem ON horarioturmadiaitem.professor = funcionario.pessoa ");
		str.append("inner JOIN horarioturmadia ON horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		str.append("inner JOIN horarioturma ON horarioturmadia.horarioturma = horarioturma.codigo ");
		str.append("inner JOIN turma ON turma.codigo = horarioturma.turma ");
		str.append("inner JOIN curso ON ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo) ))");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT distinct funcionario.codigo, funcionario.exerceCargoAdministrativo, funcionario.matricula, funcionario.observacao, funcionario.escolaridade, funcionario.empresaRecebimento, funcionario.cnpjEmpresaRecebimento, ");
		str.append("pessoa.nome AS \"funcionario.nome\", pessoa.tipoAssinaturaDocumentoEnum AS \"pessoa.tipoAssinaturaDocumentoEnum\", pessoa.codigo AS \"funcionario.codigo\", cidade.nome AS \"cidade.nome\", pessoa.cpf AS \"pessoa.cpf\", ");
		str.append("funcionarioCargo.codigo AS \"funcionarioCargo.codigo\" FROM funcionario ");
		str.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		str.append("inner join horarioturmaprofessordisciplina htpd on pessoa.codigo = htpd.professor ");
		str.append("inner join turma on turma.codigo = htpd.turma ");
		str.append("inner join unidadeEnsino on unidadeEnsino.codigo = turma.unidadeensino ");
		str.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		str.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		str.append("LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo ");
		str.append("LEFT JOIN departamento on cargo.departamento = departamento.codigo ");
		return str;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorNome(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorNome(valorConsulta, 0, tipoPessoa, unidadeEnsino, null, null, null, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorNome(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		if (departamento != 0) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public List<FuncionarioVO> consultarPorArquivoConsiderandoDocumentoAssinado(ArquivoVO arquivo) throws Exception{
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select funcionario.codigo, funcionario.exerceCargoAdministrativo, funcionario.matricula, funcionario.observacao, funcionario.escolaridade, funcionario.empresaRecebimento, funcionario.cnpjEmpresaRecebimento,  ");
		sqlStr.append(" pessoa.gerenciaPreInscricao AS \"funcionario.gerenciaPreInscricao\", pessoa.nome AS \"funcionario.nome\", "); 
		sqlStr.append(" pessoa.codigo AS \"funcionario.codigo\", pessoa.cpf AS \"pessoa.cpf\",  cidade.nome AS \"cidade.nome\",  ");
		sqlStr.append(" pessoa.email AS \"pessoa.email\" ");
		sqlStr.append(" from funcionario ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" inner join cidade on pessoa.cidade = cidade.codigo ");
		sqlStr.append(" inner join documentoassinadopessoa on pessoa.codigo = documentoassinadopessoa.pessoa ");
		sqlStr.append(" inner join documentoassinado on documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
		sqlStr.append(" where documentoassinado.arquivo = ?  ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { arquivo.getCodigo() });
		return montarDadosConsultaRapida(tabelaResultado);
		
	}

	@Override
	public List<FuncionarioVO> consultaPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo, String tipoPessoa,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {

		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasica().toString());
		dataModelo.getListaFiltros().clear();
		dataModelo.setLimitePorPagina(10);
		
		sql.append(" WHERE 1 = 1");
		switch (dataModelo.getCampoConsulta()) {
		case "matricula":
			sql.append(" and lower(funcionario.matricula) ilike(?) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
			break;
		case "nome":
			sql.append(" and sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
			break;
		case "cpf":
			sql.append(" and pessoa.cpf ilike(?) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
			break;
		default:
			break;
		}

		montarSqlUnidadeEnsinoBiblioteca(unidadeEnsinoBibliotecaVOs, sql);
		montarSqlTipoPessoa(tipoPessoa, sql);

		sql.append(" ORDER BY Pessoa.nome ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override
	public Integer consultaTotalPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo, String tipoPessoa,
			List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(getSQLCountPadraoConsultaBasica().toString());
		dataModelo.getListaFiltros().clear();
		dataModelo.setLimitePorPagina(10);
		
		sql.append(" WHERE 1 = 1");
		switch (dataModelo.getCampoConsulta()) {
		case "matricula":
			sql.append(" and lower(funcionario.matricula) ilike(?) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
			break;
		case "nome":
			sql.append(" and sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
			break;
		case "cpf":
			sql.append(" and pessoa.cpf ilike(?) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
			break;
		default:
			break;
		}

		montarSqlUnidadeEnsinoBiblioteca(unidadeEnsinoBibliotecaVOs, sql);
		montarSqlTipoPessoa(tipoPessoa, sql);
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private void montarSqlTipoPessoa(String tipoPessoa, StringBuilder sql) {
		if (tipoPessoa.equals("FU")) {
			sql.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sql.append(" and pessoa.professor = 'true'");
		}
	}

	private void montarSqlUnidadeEnsinoBiblioteca(List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs,
			StringBuilder sql) {
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sql.append(" AND (funcionarioCargo.unidadeensino in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sql.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sql.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sql.append(") or funcionarioCargo.codigo is null) ");
		}
	}

	public List<FuncionarioVO> consultaRapidaPorNomeConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE funcionarioCargo.consultor = true AND sem_acentos(pessoa.nome) ilike(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorNome(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND ((funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" and funcionariocargo.ativo = true) ");
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}else {
			sqlStr.append(" and (funcionariocargo.ativo = true ");
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}

		sqlStr.append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorNome(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" Select COUNT(distinct funcionario.codigo) From funcionario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
		sqlStr.append(" LEFT JOIN usuario on usuario.pessoa = pessoa.codigo ");
		sqlStr.append(" LEFT JOIN usuarioPerfilAcesso on usuarioPerfilAcesso.usuario = usuario.codigo");
		sqlStr.append(" LEFT JOIN UnidadeEnsino on usuarioPerfilAcesso.UnidadeEnsino = UnidadeEnsino.codigo");
		sqlStr.append(" LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sqlStr.append(" LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario");
		sqlStr.append(" LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo ");
		sqlStr.append(" LEFT JOIN departamento on cargo.departamento = departamento.codigo");
		sqlStr.append(" LEFT JOIN secaofolhapagamento as secao on secao.codigo = funcionarioCargo.secaofolhapagamento");
		sqlStr.append(" WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?))");

		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	public List consultaRapidaProfessorPorNomeAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?) ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaProfessorPorMatriculaAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE funcionario.matricula ilike ? ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaProfessorPorNomeCidadeAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE sem_acentos(cidade.nome) ilike(sem_acentos(?)) ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaProfessorPorCPFAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE replace(replace(pessoa.cpf,'.',''),'-','') ilike(?) ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%");
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaProfessorPorCargoAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE sem_acentos(cargo.nome) ilike(sem_acentos(?)) ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public List consultaRapidaProfessorPorDepartamentoAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE sem_acentos(departamento.nome) ilike(sem_acentos(?)) ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return (montarDadosConsultaRapida(tabelaResultado));
	}

	public FuncionarioVO consultaRapidaProfessorPorMatriculaUnicaAgendaProfessoresVisaoCoordenador(String valorConsulta, Integer codigoCoordenador, String tipoPessoa, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaAgendaProfessorVisaoCoordenador();
		sqlStr.append("INNER JOIN curso ON curso.codigo = turma.curso ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sqlStr.append("WHERE lower(funcionario.matricula) = (?) ");
		if (tipoPessoa.equals("PR")) {
			sqlStr.append("and pessoa.professor = 'true' ");
		}
		sqlStr.append("AND ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa WHERE pessoa.codigo = ").append(codigoCoordenador).append(")) ");
		sqlStr.append("OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sqlStr.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoCoordenador).append(" AND cc.turma IS NULL))) ");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		sqlStr.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sqlStr.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sqlStr.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sqlStr.append("ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		FuncionarioVO obj = new FuncionarioVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
		}
		return obj;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorMatricula(valorConsulta, 0, unidadeEnsino, null, null, nivelMontarDados, null, null, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorMatricula(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(funcionario.matricula) like(?)");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		if (departamento != 0) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY funcionario.matricula");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorMatriculaUnidadeEnsinoBiblioteca(String valorConsulta, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(funcionario.matricula) like(?)");
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND (funcionarioCargo.unidadeensino in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sqlStr.append(") or funcionarioCargo.codigo is null) ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorMatriculaConsultor(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE funcionarioCargo.consultor = true AND lower(funcionario.matricula) like(?)");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY funcionario.matricula");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(funcionario.matricula) like(?)");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY funcionario.matricula");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT COUNT(funcionario.codigo) FROM funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		sqlStr.append("WHERE lower(funcionario.matricula) like(?)");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");

		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;

	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorCidade(valorConsulta, unidadeEnsino, null, null, null, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(cidade.nome)) like(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY cidade.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(cidade.nome)) like(sem_acentos(?))");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY cidade.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorCidade(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT COUNT(funcionario.codigo) FROM funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		sqlStr.append("WHERE sem_acentos(lower(cidade.nome)) like(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;

	}

	public List<FuncionarioVO> consultaRapidaPorNomeProfessorTitularDisciplinaTurma(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, String semestre, String ano, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaProfessorTitularDisciplinaTurma();
		sqlStr.append("WHERE sem_acentos(lower(pessoa.nome)) like(sem_acentos(?))");
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && !curso.equals(0)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (turma != null && !turma.equals(0)) {
			sqlStr.append(" AND turma.codigo = ").append(turma);
		}
		if (disciplina != null && !disciplina.equals(0)) {
			sqlStr.append(" AND horarioturmadiaitem.disciplina = ").append(disciplina);
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append(" AND horarioturma.semestrevigente = '").append(semestre).append("'");
		}
		if (ano != null && !ano.equals("")) {
			sqlStr.append(" AND horarioturma.anovigente = '").append(ano).append("'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY horarioturma.anovigente, horarioturma.semestrevigente, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta.toLowerCase() + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorCpfProfessorTitularDisciplinaTurma(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, String semestre, String ano, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaProfessorTitularDisciplinaTurma();
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?) ");
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (curso != null && !curso.equals(0)) {
			sqlStr.append(" AND curso.curso = ").append(curso);
		}
		if (turma != null && !turma.equals(0)) {
			sqlStr.append(" AND turma.codigo = ").append(turma);
		}
		if (disciplina != null && !disciplina.equals(0)) {
			sqlStr.append(" AND horarioturmadiaitem.disciplina = ").append(disciplina);
		}
		if (semestre != null && !semestre.equals("")) {
			sqlStr.append(" AND horarioturma.semestrevigente = '").append(semestre).append("'");
		}
		if (ano != null && !ano.equals("")) {
			sqlStr.append(" AND horarioturma.anovigente = '").append(ano).append("'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}

		sqlStr.append(" ORDER BY horarioturma.anovigente, horarioturma.semestrevigente, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()) + "%" });
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorCPF(valorConsulta, 0, tipoPessoa, unidadeEnsino, null, null, null, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorCPF(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and  pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and  pessoa.professor = 'false'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		if (departamento != 0) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.cpf");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorCPFConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE funcionarioCargo.consultor = true AND lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and  pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.cpf");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorCPF(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and  pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.cpf");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorCPF(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("Select COUNT(funcionario.codigo) as count FROM funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND funcionario.codigo in (select funcionario from funcionariocargo where unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue()).append(") ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and  pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%");
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;

	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorCargo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorCargo(valorConsulta, 0, unidadeEnsino, null, null, null, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorCargo(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(cargo.nome)) like(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		if (departamento != 0) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorCargoConsultor(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE funcionarioCargo.consultor = true AND sem_acentos(lower(cargo.nome)) like(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorCargo(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(cargo.nome)) like(sem_acentos(?))");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorCargo(String valorConsulta, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLCountPadraoConsultaBasica());
		sqlStr.append("WHERE sem_acentos(lower(cargo.nome)) like(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		if (resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;

	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorNomeDepartamento(valorConsulta, tipoPessoa, unidadeEnsino, null, null, null, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(departamento.nome)) like(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamentoConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE funcionarioCargo.consultor = true AND sem_acentos(lower(departamento.nome)) like(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(departamento.nome)) like(sem_acentos(?))");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorNomeDepartamento(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLCountPadraoConsultaBasica());
		sqlStr.append("WHERE sem_acentos(lower(departamento.nome)) like(sem_acentos(?))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		if (resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;
	}

	/**
	 * Método responsavel por invocar uma consulta rápida(Básica) e padrão que
	 * buscará apenas campos necessários para visualização do cliente na tela. Está
	 * consulta é considerada Padrão pelo motivo de todos os métodos chamar a mesma
	 * consulta(getSQLPadraoConsultaBasica) e apenas adicionar as cláusulas de
	 * condições e ordenação.
	 * 
	 * @author Carlos
	 */
	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorUnidadeEnsino(valorConsulta, tipoPessoa, unidadeEnsino, null, null, null, null, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(unidadeEnsino.nome)) like(sem_acentos(lower(?)))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsinoConsultor(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE funcionarioCargo.consultor = true AND sem_acentos(lower(unidadeEnsino.nome)) like(sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(lower(unidadeEnsino.nome)) like(sem_acentos(?))");
		sqlStr.append(" and funcionariocargo.ativo = true ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsultaRapida(tabelaResultado);
	}

	public Integer consultaTotalDeRegistroRapidaPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT COUNT(codigo) FROM ( ");
		sqlStr.append(getSQLPadraoConsultaBasica());
		sqlStr.append("WHERE sem_acentos(lower(unidadeEnsino.nome)) like(sem_acentos(lower(?)))");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
			sqlStr.append(" or funcionarioCargo.codigo is null) ");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (!tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		sqlStr.append(" ) AS t");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}
	
	public List<FuncionarioVO> consultaRapidaPorUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			sqlStr.append(" and funcionarioCargo.unidadeensino in ( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaUnidadeEnsino)).append(" )");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado);
	}

	/**
	 * Método responsavel por fazer uma seleção completa da Entidade Funcionário e
	 * mais algumas outras entidades que possuem relacionamento com a mesma. É uma
	 * consulta que busca completa e padrão.
	 * 
	 * @return List Contendo vários objetos da classe
	 * @author Carlos
	 */
	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		// Dados do Funcionário
		str.append("SELECT func.codigo, func.matricula, func.nomeBanco, func.numeroBancoRecebimento, func.numeroAgenciaRecebimento, func.contaCorrenteRecebimento,  ");
		str.append("func.dataAdmissao, func.exerceCargoAdministrativo, func.observacao, func.escolaridade, func.empresaRecebimento, func.cnpjEmpresaRecebimento, ");
		str.append("func.digitoAgenciaRecebimento, func.digitoCorrenteRecebimento, func.operacaoBancaria,   func.chaveEnderecamentoPix ,   func.tipoIdentificacaoChavePixEnum  ,");
		str.append("func.naoNotificarInclusaoUsuario, ");
		// Dados da Pessoa
		str.append("pes.codigo AS \"pessoa.codigo\", pes.nome AS \"funcionario.nome\", pes.cep AS \"pessoa.cep\", pes.endereco AS \"pessoa.endereco\",  ");
		str.append(" pes.setor AS \"pessoa.setor\", pes.complemento AS \"pessoa.complemento\", pes.numero AS \"pessoa.numero\", ");
		str.append(" pes.telefoneComer as \"pessoa.telefoneComer\", pes.telefoneRes as \"pessoa.telefoneRes\", pes.telefoneRecado as \"pessoa.telefoneRecado\", pes.atuaComoDocente as \"pessoa.atuaComoDocente\", ");
		str.append(" pes.celular as \"pessoa.celular\", pes.email as \"pessoa.email\", pes.dataNasc as \"pessoa.dataNasc\", pes.necessidadesEspeciais as \"pessoa.necessidadesEspeciais\", ");
		str.append(" pes.sexo as \"pessoa.sexo\", pes.estadoCivil as \"pessoa.estadoCivil\", paiz.codigo as \"nacionalidade.codigo\", naturalidade.codigo as \"pessoa.codNaturalidade\", ");
		str.append(" naturalidade.nome as \"pessoa.naturalidade\", pes.cpf as \"pessoa.cpf\", pes.ativo as \"pessoa.ativo\", pes.rg as \"pessoa.rg\", pes.dataEmissaoRG as \"pessoa.dataEmissaoRG\", pes.orgaoEmissor as \"pessoa.orgaoEmissor\", ");
		str.append(" pes.estadoEmissaoRG as \"pessoa.estadoEmissaoRG\", pes.tituloEleitoral as \"pessoa.tituloEleitoral\", pes.certificadoMilitar as \"pessoa.certificadoMilitar\", ");
		str.append(" pes.dataExpedicaoTituloEleitoral as \"pessoa.dataExpedicaoTituloEleitoral\", pes.zonaEleitoral as \"pessoa.zonaEleitoral\", pes.dataExpedicaoCertificadoMilitar as \"pessoa.dataExpedicaoCertificadoMilitar\", ");
		str.append(" pes.orgaoExpedidorCertificadoMilitar as \"pessoa.orgaoExpedidorCertificadoMilitar\", ");
		str.append(" pes.professor as \"pessoa.professor\", pes.coordenador as \"pessoa.coordenador\", pes.gerenciaPreInscricao as \"pessoa.gerenciaPreInscricao\", pes.pispasep as \"pessoa.pispasep\", pes.isentarTaxaBoleto as \"pessoa.isentarTaxaBoleto\", ");
		str.append(" pes.funcionario as \"pessoa.funcionario\", pes.aluno as \"pessoa.aluno\", pes.possuiAcessoVisaoPais as \"pessoa.possuiAcessoVisaoPais\", ");
		str.append(" pes.permiteenviarremessa as \"pessoa.permiteenviarremessa\",  pes.tipoAssinaturaDocumentoEnum as \"pessoa.tipoAssinaturaDocumentoEnum\", ");
		// Dados da Cidade
		str.append(" cidade.nome AS \"cidade.nome\", cidade.codigo AS \"cidade.codigo\", ");
		str.append(" naturalidade.estado AS \"naturalidade.estado\", estadoNaturalidade.codigo AS \"estadoNaturalidade.codigo\",  estadoNaturalidade.nome AS \"estadoNaturalidade.nome\", estadoNaturalidade.sigla AS \"estadoNaturalidade.sigla\", ");
		str.append(" estado.codigo AS \"estado.codigo\", estado.nome AS \"estado.nome\", estado.sigla AS \"estado.sigla\", ");
		// Dados do Cargo
		str.append(" cargo.codigo as \"cargo.codigo\", cargo.nome AS \"cargo.nome\", cargo.cbo AS \"cargo.cbo\",  cargo.controlaNivelExperiencia AS \"cargo.controlaNivelExperiencia\" , cargo.consultorVendas as \"cargo.consultorVendas\", ");
		// Dados do FuncionarioCargo
		str.append(" funcionarioCargo.codigo as \"funcionarioCargo.codigo\", funcionarioCargo.nivelExperiencia as \"funcionarioCargo.nivelExperiencia\", ");
		str.append(" funcionarioCargo.consultor as \"funcionarioCargo.consultor\", funcionarioCargo.gerente as \"funcionarioCargo.gerente\", ");
		str.append(" funcionariocargo.ativo as \"funcionariocargo.ativo\", funcionariocargo.matriculacargo as \"funcionariocargo.matriculacargo\", ");
		str.append(" funcionariocargo.dataadmissao AS \"funcionariocargo.dataadmissao\", funcionariocargo.datademissao AS \"funcionariocargo.datademissao\",  ");
		str.append(" funcionariocargo.tipoRecebimento AS \"funcionariocargo.tipoRecebimento\" , funcionariocargo.formaContratacao as \"funcionariocargo.formaContratacao\", ");
		str.append(" funcionariocargo.situacaoFuncionario as \"funcionariocargo.situacaoFuncionario\", funcionariocargo.salario AS \"funcionariocargo.salario\", ");
		str.append(" funcionariocargo.jornada AS \"funcionariocargo.jornada\",  funcionariocargo.utilizaRH AS \"funcionariocargo.utilizaRH\" , ");
		str.append(" funcionariocargo.comissionado AS \"funcionariocargo.comissionado\",  funcionariocargo.secaoFolhaPagamento AS \"funcionariocargo.secaoFolhaPagamento\" , ");
		str.append(" funcionariocargo.cargoAtual AS \"funcionariocargo.cargoAtual\",  funcionariocargo.tipoContratacaoComissionado AS \"funcionariocargo.tipoContratacaoComissionado\" , ");
		str.append(" funcionariocargo.nivelSalarial AS \"funcionariocargo.nivelSalarial\",  funcionariocargo.faixaSalarial AS \"funcionariocargo.faixaSalarial\" , ");
		str.append(" funcionariocargo.dataBaseQuinquenio as \"funcionariocargo.dataBaseQuinquenio\" , funcionariocargo.previdencia as \"funcionariocargo.previdencia\" ,");
		str.append(" funcionariocargo.optanteTotal as \"funcionariocargo.optanteTotal\", ");
		// Dados do Departamento
		str.append(" depart.codigo as \"departamento.codigo\", ");
		str.append(" depart.nome as \"departamento.nome\", ");
		// Dados da Unidade Ensino
		str.append(" unEnsino.codigo AS \"unidadeEnsino.codigo\" , unEnsino.nome AS \"unidadeEnsino.nome\", ");

		str.append(" arquivo.pastaBaseArquivo, arquivo.codigo AS codArquivo, arquivo.nome AS nomeArquivo, ");
		str.append(" arq2.pastaBaseArquivo as pastaBaseArquivoAssinatura, arq2.codigo AS codArquivoAssinatura, arq2.nome AS nomeArquivoAssinatura ");
		str.append("  FROM funcionario as func ");

		str.append(" LEFT JOIN pessoa as pes ON pes.codigo = func.pessoa ");
		str.append(" LEFT JOIN cidade ON cidade.codigo = pes.cidade ");
		str.append(" LEFT JOIN estado ON estado.codigo = cidade.estado ");
		str.append(" LEFT JOIN cidade as naturalidade on naturalidade.codigo = pes.naturalidade ");
		str.append(" LEFT JOIN estado as estadoNaturalidade ON estadoNaturalidade.codigo = naturalidade.estado ");
		str.append(" LEFT JOIN paiz ON paiz.codigo = pes.nacionalidade ");
		str.append(" LEFT JOIN funcionarioCargo on func.codigo = funcionarioCargo.funcionario ");
		str.append(" LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo ");
		str.append(" LEFT JOIN departamento AS depart  ON cargo.departamento = depart.codigo ");
		str.append(" LEFT JOIN unidadeEnsino AS unEnsino ON funcionarioCargo.unidadeEnsino =  unEnsino.codigo ");
		str.append(" LEFT JOIN arquivo ON pes.arquivoImagem = arquivo.codigo ");
		str.append(" LEFT JOIN arquivo arq2 ON func.arquivoAssinatura = arq2.codigo ");
		return str;
	}

	public List<FuncionarioVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
		List<FuncionarioVO> vetResultado = new ArrayList<FuncionarioVO>(0);
		while (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}

	public void carregarDados(FuncionarioVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((FuncionarioVO) obj, NivelMontarDados.TODOS, usuario);
	}

	/**
	 * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
	 * Completo e faz a consulta de acordo com o nível especificado.
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @author Carlos
	 */
	public void carregarDados(FuncionarioVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((FuncionarioVO) obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((FuncionarioVO) obj, resultado);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Funcionario.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Funcionário).");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codFuncionario, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Func.codigo= ").append(codFuncionario).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Funcionário).");
		}
		return tabelaResultado;
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar os
	 * atributos básicos do objeto e alguns atributos relacionados de relevância
	 * para o contexto da aplicação.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasico(FuncionarioVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados do Funcionário
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setEscolaridade(dadosSQL.getString("escolaridade"));
		obj.setEmpresaRecebimento(dadosSQL.getString("empresaRecebimento"));
		obj.setCnpjEmpresaRecebimento(dadosSQL.getString("cnpjEmpresaRecebimento"));
		obj.setExerceCargoAdministrativo(dadosSQL.getBoolean("exerceCargoAdministrativo"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Pessoa
		obj.getPessoa().setGerenciaPreInscricao(dadosSQL.getBoolean("funcionario.gerenciaPreInscricao"));
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("funcionario.codigo")));
		obj.getPessoa().setNome(dadosSQL.getString("funcionario.nome"));
		obj.getPessoa().getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getPessoa().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getPessoa().setEmail(dadosSQL.getString("pessoa.email"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum"))){
			obj.getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum")));
		}
		obj.getPessoa().setNivelMontarDados(NivelMontarDados.BASICO);

	}

	/**
	 * Consulta que espera um ResultSet com todos os campos e dados de objetos
	 * relacionados, Para reconstituir o objeto por completo, de uma determinada
	 * entidade.
	 * 
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(FuncionarioVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.getPessoa().getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getPessoa().getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.getPessoa().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));

		obj.getArquivoAssinaturaVO().setCodigo(dadosSQL.getInt("codArquivoAssinatura"));
		obj.getArquivoAssinaturaVO().setNome(dadosSQL.getString("nomeArquivoAssinatura"));
		obj.getArquivoAssinaturaVO().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivoAssinatura"));

		// Dados do Funcionário
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setNomeBanco(dadosSQL.getString("nomeBanco"));
		obj.setNumeroBancoRecebimento(dadosSQL.getString("numeroBancoRecebimento"));
		obj.setNumeroAgenciaRecebimento(dadosSQL.getString("numeroAgenciaRecebimento"));
		obj.setContaCorrenteRecebimento(dadosSQL.getString("contaCorrenteRecebimento"));
		obj.setDigitoAgenciaRecebimento(dadosSQL.getString("digitoAgenciaRecebimento"));
		obj.setDigitoCorrenteRecebimento(dadosSQL.getString("digitoCorrenteRecebimento"));
		obj.setOperacaoBancaria(dadosSQL.getString("operacaoBancaria"));
		obj.setDataAdmissao(dadosSQL.getDate("dataAdmissao"));
		obj.setExerceCargoAdministrativo(dadosSQL.getBoolean("exerceCargoAdministrativo"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setEscolaridade(dadosSQL.getString("escolaridade"));
		obj.setEmpresaRecebimento(dadosSQL.getString("empresaRecebimento"));
		obj.setCnpjEmpresaRecebimento(dadosSQL.getString("cnpjEmpresaRecebimento"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoChavePixEnum"))) {
			obj.setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("tipoIdentificacaoChavePixEnum")));
		}		
		obj.setChaveEnderecamentoPix(dadosSQL.getString("chaveEnderecamentoPix") );
		obj.setNaoNotificarInclusaoUsuario(dadosSQL.getBoolean("naoNotificarInclusaoUsuario"));
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		// Dados da Pessoa
		obj.getPessoa().setCodigo(new Integer(dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoa().setNome(dadosSQL.getString("funcionario.nome"));
		// obj.getPessoa().setFoto((byte[]) dadosSQL.getObject("pessoa.foto"));
		obj.getPessoa().setCEP(dadosSQL.getString("pessoa.cep"));
		obj.getPessoa().setEndereco(dadosSQL.getString("pessoa.endereco"));
		obj.getPessoa().setSetor(dadosSQL.getString("pessoa.setor"));
		obj.getPessoa().setComplemento(dadosSQL.getString("pessoa.complemento"));
		obj.getPessoa().setNumero(dadosSQL.getString("pessoa.numero"));
		obj.getPessoa().setTelefoneComer(dadosSQL.getString("pessoa.telefoneComer"));
		obj.getPessoa().setAtuaComoDocente(dadosSQL.getString("pessoa.atuaComoDocente"));
		obj.getPessoa().setTelefoneRes(dadosSQL.getString("pessoa.telefoneRes"));
		obj.getPessoa().setTelefoneRecado(dadosSQL.getString("pessoa.telefoneRecado"));
		obj.getPessoa().setCelular(dadosSQL.getString("pessoa.celular"));
		obj.getPessoa().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoa().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		obj.getPessoa().setNecessidadesEspeciais(dadosSQL.getString("pessoa.necessidadesEspeciais"));
		obj.getPessoa().setSexo(dadosSQL.getString("pessoa.sexo"));
		obj.getPessoa().setEstadoCivil(dadosSQL.getString("pessoa.estadoCivil"));
		obj.getPessoa().getNacionalidade().setCodigo(new Integer(dadosSQL.getInt("nacionalidade.codigo")));
		obj.getPessoa().getNaturalidade().setCodigo(dadosSQL.getInt("pessoa.codNaturalidade"));
		obj.getPessoa().getNaturalidade().setNome(dadosSQL.getString("pessoa.naturalidade"));
		obj.getPessoa().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getPessoa().setAtivo(dadosSQL.getBoolean("pessoa.ativo"));
		obj.getPessoa().setGerenciaPreInscricao(dadosSQL.getBoolean("pessoa.gerenciaPreInscricao"));
		obj.getPessoa().setRG(dadosSQL.getString("pessoa.rg"));
		obj.getPessoa().setDataEmissaoRG(dadosSQL.getDate("pessoa.dataEmissaoRG"));
		obj.getPessoa().setOrgaoEmissor(dadosSQL.getString("pessoa.orgaoEmissor"));
		obj.getPessoa().setEstadoEmissaoRG(dadosSQL.getString("pessoa.estadoEmissaoRG"));
		obj.getPessoa().setTituloEleitoral(dadosSQL.getString("pessoa.tituloEleitoral"));
		obj.getPessoa().setCertificadoMilitar(dadosSQL.getString("pessoa.certificadoMilitar"));
		obj.getPessoa().setDataExpedicaoTituloEleitoral(dadosSQL.getDate("pessoa.dataExpedicaoTituloEleitoral"));
		obj.getPessoa().setZonaEleitoral(dadosSQL.getString("pessoa.zonaEleitoral"));
		obj.getPessoa().setDataExpedicaoCertificadoMilitar(dadosSQL.getDate("pessoa.dataExpedicaoCertificadoMilitar"));
		obj.getPessoa().setOrgaoExpedidorCertificadoMilitar(dadosSQL.getString("pessoa.orgaoExpedidorCertificadoMilitar"));
		obj.getPessoa().setProfessor(dadosSQL.getBoolean("pessoa.professor"));
		obj.getPessoa().setCoordenador(dadosSQL.getBoolean("pessoa.coordenador"));
		obj.getPessoa().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		obj.getPessoa().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoa().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("pessoa.possuiAcessoVisaoPais"));
		obj.getPessoa().setIsentarTaxaBoleto(dadosSQL.getBoolean("pessoa.isentarTaxaBoleto"));
		obj.getPessoa().setPermiteEnviarRemessa(dadosSQL.getBoolean("pessoa.permiteenviarremessa"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum"))){
			obj.getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum")));
		}
		obj.getPessoa().setPispasep(dadosSQL.getString("pessoa.pispasep"));
		obj.getPessoa().getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
		obj.getPessoa().getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getPessoa().getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
		obj.getPessoa().getNaturalidade().getEstado().setCodigo(dadosSQL.getInt("estadoNaturalidade.codigo"));
		obj.getPessoa().getNaturalidade().getEstado().setNome(dadosSQL.getString("estadoNaturalidade.nome"));
		obj.getPessoa().getNaturalidade().getEstado().setSigla(dadosSQL.getString("estadoNaturalidade.sigla"));
		obj.getPessoa().setNivelMontarDados(NivelMontarDados.TODOS);
		// Dados da Cidade
		obj.getPessoa().getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade.codigo")));
		obj.getPessoa().getCidade().setNome(dadosSQL.getString("cidade.nome"));
		// Dados do Departamento
		obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamento().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade de Ensino
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino.codigo")));
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
//		obj.setDependenteVOs(getFacadeFactory().getFuncionarioDependenteInterfaceFacade().consultarPorFuncionario(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null));

		FuncionarioCargoVO funcionariocargoVO = null;
		obj.getFuncionarioCargoVOs().clear();
		do {
			funcionariocargoVO = new FuncionarioCargoVO();
			// Dados do Cargo
			funcionariocargoVO.setCodigo(dadosSQL.getInt("funcionarioCargo.codigo"));
			funcionariocargoVO.getCargo().setCodigo(dadosSQL.getInt("cargo.codigo"));
			funcionariocargoVO.getCargo().setNome(dadosSQL.getString("cargo.nome"));
			funcionariocargoVO.setAtivo(dadosSQL.getBoolean("funcionariocargo.ativo"));
			if (funcionariocargoVO.getCargo().getCodigo() == null || funcionariocargoVO.getCargo().getCodigo().equals(0)) {
				continue;
			}
			funcionariocargoVO.setMatriculaCargo(dadosSQL.getString("funcionariocargo.matriculacargo"));
			funcionariocargoVO.setDataAdmissao(dadosSQL.getDate("funcionariocargo.dataadmissao"));
			funcionariocargoVO.setDataDemissao(dadosSQL.getDate("funcionariocargo.datademissao"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("funcionariocargo.tipoRecebimento"))) {
				funcionariocargoVO.setTipoRecebimento(TipoRecebimentoEnum.getEnumPorValor(dadosSQL.getString("funcionariocargo.tipoRecebimento")));
			}

	        if (Uteis.isAtributoPreenchido(dadosSQL.getString("funcionariocargo.formaContratacao"))) {
	        	funcionariocargoVO.setFormaContratacao(FormaContratacaoFuncionarioEnum.valueOf(dadosSQL.getString("funcionariocargo.formaContratacao")));
			}
			funcionariocargoVO.setSituacaoFuncionario(dadosSQL.getString("funcionariocargo.situacaoFuncionario"));
			funcionariocargoVO.setSalario(dadosSQL.getBigDecimal("funcionariocargo.salario"));
			funcionariocargoVO.setJornada(dadosSQL.getInt("funcionariocargo.jornada"));
			funcionariocargoVO.setUtilizaRH(dadosSQL.getBoolean("funcionariocargo.utilizaRH"));
//			funcionariocargoVO.setSecaoFolhaPagamento(Uteis.montarDadosVO(dadosSQL.getInt("funcionariocargo.secaoFolhaPagamento"), SecaoFolhaPagamentoVO.class, p -> getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
			
//			funcionariocargoVO.setNivelSalarial(Uteis.montarDadosVO(
//					dadosSQL.getInt("funcionariocargo.nivelSalarial"), NivelSalarialVO.class, p -> 
//					getFacadeFactory().getNivelSalarialInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
//			
//			funcionariocargoVO.setFaixaSalarial(Uteis.montarDadosVO(
//					dadosSQL.getInt("funcionariocargo.faixaSalarial"), FaixaSalarialVO.class, p -> 
//					getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
//			
			
			funcionariocargoVO.getCargo().setNome(dadosSQL.getString("cargo.nome"));
			funcionariocargoVO.getCargo().setControlaNivelExperiencia(dadosSQL.getBoolean("cargo.controlaNivelExperiencia"));
			funcionariocargoVO.getCargo().setConsultorVendas(dadosSQL.getBoolean("cargo.consultorVendas"));
			funcionariocargoVO.getCargo().setCbo(dadosSQL.getString("cargo.cbo"));
			funcionariocargoVO.setConsultor(dadosSQL.getBoolean("funcionarioCargo.consultor"));
			funcionariocargoVO.setGerente(dadosSQL.getBoolean("funcionarioCargo.gerente"));
			// Dados do Departamento
			funcionariocargoVO.getCargo().getDepartamento().setCodigo(dadosSQL.getInt("departamento.codigo"));
			funcionariocargoVO.getCargo().getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
//			funcionariocargoVO.setNivelExperiencia(NivelExperienciaCargoEnum.valueOf(dadosSQL.getString("funcionarioCargo.nivelExperiencia")));
			// Dados da Unidade Ensino
			funcionariocargoVO.getUnidade().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino.codigo")));
			funcionariocargoVO.getUnidade().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			
			//Dados do comissionado
			funcionariocargoVO.setComissionado(dadosSQL.getBoolean("funcionariocargo.comissionado"));
			funcionariocargoVO.setDataBaseQuinquenio(dadosSQL.getDate("funcionariocargo.dataBaseQuinquenio"));

	        if (Uteis.isAtributoPreenchido(dadosSQL.getString("funcionariocargo.previdencia"))) {
	        	funcionariocargoVO.setPrevidencia(PrevidenciaEnum.valueOf(dadosSQL.getString("funcionariocargo.previdencia")));
	        }
	        funcionariocargoVO.setOptanteTotal(dadosSQL.getBoolean("funcionariocargo.optanteTotal"));

	        funcionariocargoVO.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorCodigoFuncionarioCargo(funcionariocargoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));

//			if(funcionariocargoVO.getComissionado()) {
//				funcionariocargoVO.setCargoAtual(Uteis.montarDadosVO(
//						dadosSQL.getInt("funcionariocargo.cargoAtual"), CargoVO.class, p -> 
//						getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_TODOS, null)));
//				
//				funcionariocargoVO.setTipoContratacaoComissionado(TipoContratacaoComissionadoEnum.valueOf(dadosSQL.getString("funcionariocargo.tipoContratacaoComissionado")));				
//			}
			
			obj.getFuncionarioCargoVOs().add(funcionariocargoVO);
//			obj.inicializarCentroCustoFuncionario();
			if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("codigo")))) {
				return;
			}
		} while (dadosSQL.next());

	}

	/**
	 * Método responsavel por montar as listas de Funcionario no momento em que é
	 * selecionado o Funcionario para editar.
	 * 
	 * @author Carlos
	 */
	public void inicializarDadosEntidadesSubordinadasFuncionarioVOCompleto(FuncionarioVO obj, UsuarioVO usuario) throws Exception {
//		obj.getPessoa().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicas(obj.getPessoa().getCodigo(), false, true, usuario));
		obj.getPessoa().setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(obj.getPessoa().getCodigo(), false, usuario));
//		obj.getPessoa().setDisciplinasInteresseVOs(getFacadeFactory().getDisciplinasInteresseFacade().consultarDisciplinasInteresses(obj.getPessoa().getCodigo(), false, usuario));
	}

	/**
	 * Método responsável por atualizar o número inicial da sequence da matrícula
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSequenceMatriculaFuncionario() {
		StringBuilder sqlStr = new StringBuilder("select setval('matricula_funcionario_seq', (select matricula::bigint from funcionario ");
		sqlStr.append("where matricula !~* '-' and matricula !~* '[a-zA-Z]' order by matricula desc Limit 1))");
		getConexao().getJdbcTemplate().execute(sqlStr.toString());
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>FuncionarioVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	@Override
	public void validarDados(PessoaVO obj) throws ConsistirException {
		if (obj.getNome() == null || obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Funcionário) deve ser informado.");
		}

		if (obj.getDataNasc() == null) {
			throw new ConsistirException("O campo DATA NASCIMENTO (Dados Pessoais) deve ser informado.");
		}
		if (obj.getCEP() == null || obj.getCEP().equals("")) {
			throw new ConsistirException("O campo CEP (Dados Pessoais) deve ser informado.");
		}
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public FuncionarioVO consultaFuncionarioResponsavelAssinaturaTermoEstagioRapidaPorCurso(Integer curso, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" inner join curso on curso.funcionarioResponsavelAssinaturaTermoEstagio = funcionario.codigo ");
		sqlStr.append(" WHERE curso.codigo = ? ");
		sqlStr.append(" ORDER BY funcionario.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), curso);
		FuncionarioVO obj = new FuncionarioVO();
		tabelaResultado.next();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}
	
	public FuncionarioVO consultaRapidaPorMatriculaUnica(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(funcionario.matricula) = ? ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND UnidadeEnsino.codigo = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY funcionario.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase());
		FuncionarioVO obj = new FuncionarioVO();
		tabelaResultado.next();
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public FuncionarioVO consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(String valorConsulta, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(funcionario.matricula) = ? ");
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND (unidadeEnsino.codigo in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sqlStr.append(") or unidadeEnsino.codigo is null) ");
		}
		sqlStr.append(" ORDER BY funcionario.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase());
		FuncionarioVO obj = new FuncionarioVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
		}
		return obj;
	}

	@Override
	public FuncionarioVO consultaRapidaPorCPFUnicaUnidadeEnsinoBiblioteca(String valorConsulta, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE lower(pessoa.cpf) = ? ");
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND (unidadeEnsino.codigo in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sqlStr.append(") or unidadeEnsino.codigo is null) ");
		}
		sqlStr.append(" ORDER BY funcionario.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase());
		FuncionarioVO obj = new FuncionarioVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
		}
		return obj;
	}

	public FuncionarioVO consultarFuncionarioParaGrupoDestinatarios(Integer codigoFuncionario, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT f.codigo, f.pessoa, p.nome, p.email, p.email2 FROM funcionario f INNER JOIN pessoa p ON f.pessoa = p.codigo WHERE f.codigo = " + codigoFuncionario;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new FuncionarioVO();
		}
		return montarDadosFuncionarioParaGrupoDestinatarios(tabelaResultado, usuario);
	}

	private FuncionarioVO montarDadosFuncionarioParaGrupoDestinatarios(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		FuncionarioVO funcionarioVO = new FuncionarioVO();
		funcionarioVO.setCodigo(dadosSQL.getInt("codigo"));
		funcionarioVO.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		funcionarioVO.getPessoa().setNome(dadosSQL.getString("nome"));
		funcionarioVO.getPessoa().setEmail(dadosSQL.getString("email"));
		funcionarioVO.getPessoa().setEmail2(dadosSQL.getString("email2"));
		return funcionarioVO;
	}

	public List<FuncionarioVO> consultaRapidaCoordenadorPorNomeApresentarModal(String valorConsulta, Boolean permissao, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), permissao, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT funcionario.codigo AS \"funcionario.codigo\", funcionario.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome, pessoa.cpf from cursocoordenador  ");
		sb.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sb.append(" WHERE LOWER(sem_acentos(pessoa.nome)) like(sem_acentos(?))");
		sb.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase()+"%");
		List vetResultado = new ArrayList(0);
		FuncionarioVO obj = null;
		while (tabelaResultado.next()) {
			obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("funcionario.codigo"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCPF(tabelaResultado.getString("cpf"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<FuncionarioVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, String listaCodigoUnidadeEnsino, String tipoPessoa, Boolean exerceCargoAdministrativo, Boolean ativo, int limit, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();

		sqlStr.append("select distinct funcionario.codigo, pessoa.nome, pessoa.codigo as \"pessoa.codigo\" ");
		sqlStr.append("from funcionario ");
		sqlStr.append("INNER JOIN pessoa ON funcionario.pessoa = pessoa.codigo ");
		sqlStr.append("INNER JOIN funcionarioCargo ON funcionarioCargo.funcionario = funcionario.codigo ");
		sqlStr.append("WHERE sem_acentos(UPPER(pessoa.nome)) ilike(?)");
		if (!listaCodigoUnidadeEnsino.isEmpty()) {
			sqlStr.append(" AND funcionarioCargo.unidadeEnsino in ( ").append(listaCodigoUnidadeEnsino).append(" )");
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" GROUP BY funcionario.codigo, pessoa.nome, pessoa.codigo ");
		sqlStr.append(" ORDER BY pessoa.nome ");
		if (limit != 0) {
			sqlStr.append(" limit ").append(limit);
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		sqlStr = null;
		List<FuncionarioVO> listaCurso = new ArrayList<FuncionarioVO>();
		
		while (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			listaCurso.add(obj);
		}
		return listaCurso;
	}

	public List<FuncionarioVO> consultaRapidaCoordenadorPorCPFApresentarModal(String valorConsulta, Boolean permissao, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), permissao, usuarioVO);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT funcionario.codigo AS \"funcionario.codigo\", funcionario.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome, pessoa.cpf from cursocoordenador  ");
		sb.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sb.append(" WHERE LOWER(sem_acentos(pessoa.cpf)) like(sem_acentos(?))");
		sb.append(" ORDER BY pessoa.cpf ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase()+"%");
		List vetResultado = new ArrayList(0);
		FuncionarioVO obj = null;
		while (tabelaResultado.next()) {
			obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("funcionario.codigo"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCPF(tabelaResultado.getString("cpf"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<FuncionarioVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
			throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
		}
		if (campoConsulta.equals("nome")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorNome(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("matricula")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(valorConsulta, 0, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("nomeCidade")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("CPF")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorCPF(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("cargo")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("departamento")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(valorConsulta, "", 0, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals("unidadeEnsino")) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(valorConsulta, "", 0, controlarAcesso, nivelMontarDados, usuario);
		}
		return new ArrayList(0);
	}
	
	public List<FuncionarioVO> consultarPorUnifificacao(String valorConsulta, String campoConsulta, boolean funcionarioAtivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		
		if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
			throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
		}
		if (campoConsulta.equals("nome")) {
			return consultarPorNome(valorConsulta, 0, controlarAcesso, nivelMontarDados, funcionarioAtivo, usuario);
		}
		if (campoConsulta.equals("matricula")) {
			return consultarPorMatricula(valorConsulta, 0, nivelMontarDados, funcionarioAtivo, usuario);
		}		
		if (campoConsulta.equals("CPF")) {
			return consultarPorCPF(valorConsulta, 0, controlarAcesso, nivelMontarDados, funcionarioAtivo, usuario);
		}
		return new ArrayList(0);
	}

	@Override
	public List<FuncionarioVO> consultarFuncionarioConsultorPorUnidade(Integer unidadeEnsino) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select funcionario.codigo,  pessoa.nome from funcionario");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" inner join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo and consultorvendas = true");
		sql.append(" where  funcionariocargo.unidadeensino   = ").append(unidadeEnsino);
		sql.append(" and pessoa.ativo  = true ");
		sql.append(" and funcionariocargo.ativo  = true ");
		sql.append(" group by funcionario.codigo, pessoa.nome");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<FuncionarioVO> funcionarioVOs = new ArrayList<FuncionarioVO>(0);
		while (rs.next()) {
			FuncionarioVO funcionarioVO = new FuncionarioVO();
			funcionarioVO.setCodigo(rs.getInt("codigo"));
			funcionarioVO.getPessoa().setNome(rs.getString("nome"));
			funcionarioVOs.add(funcionarioVO);
		}
		return funcionarioVOs;
	}

	@Override
	public FuncionarioVO consultarFuncionarioEConsultorDaUnidade(Integer unidadeEnsino, Integer consultor) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select funcionario.codigo,  pessoa.nome, pessoa.codigo as codPessoa from funcionario");
		sql.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo");
		sql.append(" inner join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo and consultorvendas = true");
		sql.append(" where  funcionariocargo.unidadeensino   = ").append(unidadeEnsino);
		sql.append(" and funcionario.codigo = ").append(consultor);
		sql.append(" group by funcionario.codigo, pessoa.nome, pessoa.codigo");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		if (rs.next()) {
			FuncionarioVO funcionarioVO = new FuncionarioVO();
			funcionarioVO.setCodigo(rs.getInt("codigo"));
			funcionarioVO.getPessoa().setCodigo(rs.getInt("codPessoa"));
			funcionarioVO.getPessoa().setNome(rs.getString("nome"));
			return funcionarioVO;
		}
		return null;
	}

	@Override
	public FuncionarioVO realizarDistribuicaoResponsavelRequerimento(Integer requerimento, TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavelEnum, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, TipoRequerimentoVO tipoRequerimentoVO) throws Exception {
		FuncionarioVO funcionarioVO = realizarVerificacaoExistenciaFuncionarioJaRealizouTramite(requerimento, tipoRequerimentoDepartamentoVO.getDepartamento().getCodigo(), tipoRequerimentoDepartamentoVO.getCargo().getCodigo(), tipoRequerimentoDepartamentoVO.getOrdemExecucao(), tipoDistribuicaoResponsavelEnum, usuario);
		if (Uteis.isAtributoPreenchido(funcionarioVO) && !tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO) && !tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO_NO_TRAMITE)) {
			return funcionarioVO;
		}
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select distinct funcionario.codigo as codigo, pessoa.codigo as pessoa, pessoa.nome as nome, pessoa.email as email  ");
		if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_DEPARTAMENTO)) {
			if (tipoRequerimentoDepartamentoVO.getTipoPoliticaDistribuicao().equals(TipoPoliticaDistribuicaoEnum.DISTRIBUICAO_CIRCULAR)) {
				sql.append(",  case when requerimentohistorico.dataentradadepartamento is null then current_timestamp - '100 year'::INTERVAL else requerimentohistorico.dataentradadepartamento end as dataentradadepartamento ");
			} else {
				sql.append(", case when requerimento.quantidade is null then 0 else requerimento.quantidade end as  quantidade ");
			}
		}
		sql.append(" from Pessoa ");
		sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO_NO_TRAMITE)) {
			sql.append(" where pessoa.codigo = ").append(tipoRequerimentoDepartamentoVO.getResponsavelRequerimentoDepartamento().getCodigo());
		}else if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO)) {
			sql.append(" inner join tiporequerimentodepartamentofuncionario on funcionario.codigo = tiporequerimentodepartamentofuncionario.funcionario ");
			sql.append(" and tiporequerimentodepartamentofuncionario.tiporequerimentodepartamento = ").append(tipoRequerimentoDepartamentoVO.getCodigo());
			sql.append(" and tiporequerimentodepartamentofuncionario.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
			sql.append(" inner join unidadeensino on unidadeensino.codigo = tiporequerimentodepartamentofuncionario.unidadeensino ");
			if (tipoRequerimentoDepartamentoVO.getTipoPoliticaDistribuicao().equals(TipoPoliticaDistribuicaoEnum.DISTRIBUICAO_CIRCULAR)) {
				sql.append(" left join requerimentohistorico as requerimentohistorico on responsavelrequerimentodepartamento = pessoa.codigo ");
				sql.append(" and requerimentohistorico.codigo = (select codigo from requerimentohistorico rh where  rh.responsavelrequerimentodepartamento = pessoa.codigo  order by dataentradadepartamento desc limit 1) ");
			} else {
				sql.append(" left join (select count(requerimento.codigo) as quantidade, requerimento.funcionario from requerimento where requerimento.situacao not in ('FD', 'FI', 'AP') group by requerimento.funcionario ) as requerimento on  requerimento.funcionario = funcionario.codigo ");
			}
		} else if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_DEPARTAMENTO)) {
			if (tipoRequerimentoDepartamentoVO.getTipoPoliticaDistribuicao().equals(TipoPoliticaDistribuicaoEnum.DISTRIBUICAO_CIRCULAR)) {
				sql.append(" left join requerimentohistorico as requerimentohistorico on responsavelrequerimentodepartamento = pessoa.codigo ");
				sql.append(" and requerimentohistorico.codigo = (select codigo from requerimentohistorico rh where  rh.responsavelrequerimentodepartamento = pessoa.codigo  order by dataentradadepartamento desc limit 1) ");
			} else {
				sql.append(" left join (select count(requerimento.codigo) as quantidade, requerimento.funcionario from requerimento where requerimento.situacao not in ('FD', 'FI', 'AP') group by requerimento.funcionario ) as requerimento on  requerimento.funcionario = funcionario.codigo ");
			}
		} else if ((tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE)) && !TiposRequerimento.TRANSF_INTERNA.getValor().equals(tipoRequerimentoVO.getTipo())) {
			sql.append(" inner join cursocoordenador on cursocoordenador.funcionario = funcionario.codigo ");
			sql.append(" and cursocoordenador.curso = (select case when matricula.curso is null then requerimento.curso else matricula.curso end from requerimento left join matricula on matricula.matricula = requerimento.matricula where requerimento.codigo = ").append(requerimento).append(") ");
			sql.append(" inner join unidadeensino on unidadeensino.codigo = cursocoordenador.unidadeensino ");
		} else if ((tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE)) && TiposRequerimento.TRANSF_INTERNA.getValor().equals(tipoRequerimentoVO.getTipo())) {
			sql.append("inner join cursocoordenador on cursocoordenador.funcionario = funcionario.codigo ");
			sql.append("and cursocoordenador.curso = (select cursoTransferenciaInterna from requerimento where codigo = ").append(requerimento).append(") ");
			sql.append("and cursocoordenador.unidadeensino = (select unidadeEnsinoTransferenciaInterna from requerimento where codigo = ").append(requerimento).append(") ");
			sql.append("inner join unidadeensino on unidadeensino.codigo = cursocoordenador.unidadeensino ");
		} else if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_TCC)) {
			sql.append(" inner join unidadeensinocurso uec on uec.coordenadortcc = funcionario.codigo ");
			sql.append(" and uec.curso = (select case when matricula.curso is null then requerimento.curso else matricula.curso end from requerimento left join matricula on matricula.matricula = requerimento.matricula where requerimento.codigo = ").append(requerimento).append(") ");
			sql.append(" inner join unidadeensino on unidadeensino.codigo = uec.unidadeensino ");
		}
		if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_DEPARTAMENTO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" and funcionariocargo.ativo ");
			sql.append(" inner join departamento on departamento.codigo = funcionariocargo.departamento ");
			sql.append(" inner join unidadeensino on unidadeensino.codigo = funcionariocargo.unidadeensino ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			
			if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
				sql.append(" and (funcionariocargo.gerente = true or funcionario.pessoa = departamento.responsavel ) 	");
			} else if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO)) {
				sql.append(" and funcionariocargo.cargo = ").append(tipoRequerimentoDepartamentoVO.getCargo().getCodigo());
			}
			
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento =  ")
			.append(tipoRequerimentoDepartamentoVO.getDepartamento().getCodigo())
			.append(") or (funcionariocargo.departamento is null and cargo.departamento = ")
			.append(tipoRequerimentoDepartamentoVO.getDepartamento().getCodigo()).append(")) ");
		}
		if (!tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO) && !tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO_NO_TRAMITE)) {
			sql.append(" where pessoa.ativo = true ");
			if (!(tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE)) || !TiposRequerimento.TRANSF_INTERNA.getValor().equals(tipoRequerimentoVO.getTipo())) {
				sql.append(" and unidadeensino.codigo = ").append(unidadeEnsinoVO.getCodigo());
			}
		}

		if (tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) || tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_DEPARTAMENTO)) {
			if (tipoRequerimentoDepartamentoVO.getTipoPoliticaDistribuicao().equals(TipoPoliticaDistribuicaoEnum.DISTRIBUICAO_CIRCULAR)) {
				sql.append(" order by (case when requerimentohistorico.dataentradadepartamento is null then current_timestamp - '100 year'::interval else requerimentohistorico.dataentradadepartamento end) ");
				sql.append(" ,pessoa.nome ");
			} else {
				sql.append(" order by quantidade, pessoa.nome ");
			}
		}

		sql.append(" limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			funcionarioVO = new FuncionarioVO();
			funcionarioVO.setNovoObj(false);
			funcionarioVO.setCodigo(rs.getInt("codigo"));
			funcionarioVO.getPessoa().setCodigo(rs.getInt("pessoa"));
			funcionarioVO.getPessoa().setNome(rs.getString("nome"));
			funcionarioVO.getPessoa().setEmail(rs.getString("email"));
			return funcionarioVO;
		}

		/**
		 * Busca responsavel gerente departamento de acordo com a unidade do aluno desde
		 * que ela não seja a matriz
		 */
		if (!unidadeEnsinoVO.getMatriz() && !tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
			return realizarDistribuicaoResponsavelRequerimento(requerimento, tipoRequerimentoDepartamentoVO, TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO, unidadeEnsinoVO, usuario, tipoRequerimentoVO);
		}
		/**
		 * Busca responsavel usando a unidade matriz de acordo com a politica de
		 * distribuição inicial
		 */
		if (!unidadeEnsinoVO.getMatriz() && tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
			tipoDistribuicaoResponsavelEnum = tipoRequerimentoDepartamentoVO.getTipoDistribuicaoResponsavel();
			UnidadeEnsinoVO unidadeMatriz = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			if (unidadeMatriz.getCodigo() > 0) {
				return realizarDistribuicaoResponsavelRequerimento(requerimento, tipoRequerimentoDepartamentoVO, tipoDistribuicaoResponsavelEnum, unidadeMatriz, usuario, tipoRequerimentoVO);
			}
		}
		/**
		 * Caso a unidade seja a matriz mas não esta utilizando a politica do gerente do
		 * dp busca então de acordo com o gerente do dp.
		 */
		if (unidadeEnsinoVO.getMatriz() && !tipoDistribuicaoResponsavelEnum.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
			return realizarDistribuicaoResponsavelRequerimento(requerimento, tipoRequerimentoDepartamentoVO, TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO, unidadeEnsinoVO, usuario, tipoRequerimentoVO);
		}
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_Requerimento_responsavelNaoLocalizado"));
		return new FuncionarioVO();
	}

	@Override
	public List<FuncionarioVO> consultaRapidaPorNomeNivelEducacional(String valorConsulta, String nivelEducacional, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct funcionario.*, pessoa.nome, pessoa.cpf ");
		sqlStr.append("from horarioturmaprofessordisciplina htpd ");
		sqlStr.append("inner join turma on turma.codigo = htpd.turma ");
		sqlStr.append("inner join curso on curso.codigo = turma.curso ");
		sqlStr.append("inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append("inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append("WHERE sem_acentos(lower(pessoa.nome)) ilike(sem_acentos(?))");
		if (!nivelEducacional.isEmpty() && !nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		montarSqlTipoPessoa(tipoPessoa, sqlStr);
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		sqlStr.append("union ");
		sqlStr.append("select distinct funcionario.*, pessoa.nome, pessoa.cpf ");
		sqlStr.append("from horarioturmaprofessordisciplina htpd ");
		sqlStr.append("inner join turma on turma.codigo = htpd.turma ");
		sqlStr.append("inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem ");
		sqlStr.append("inner join turma turma2 on turma2.codigo = turmaagrupada.turma ");
		sqlStr.append("inner join curso on curso.codigo = turma2.curso ");
		sqlStr.append("inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append("inner join funcionario on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE sem_acentos(lower(pessoa.nome)) ilike(sem_acentos(?))");
		if (!nivelEducacional.isEmpty() && !nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		montarSqlTipoPessoa(tipoPessoa, sqlStr);
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%", valorConsulta.toLowerCase()+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<FuncionarioVO> consultaRapidaPorCPFNivelEducacional(String valorConsulta, String nivelEducacional, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct funcionario.*, pessoa.nome, pessoa.cpf ");
		sqlStr.append("from horarioturmaprofessordisciplina htpd ");
		sqlStr.append("inner join turma on turma.codigo = htpd.turma ");
		sqlStr.append("inner join curso on curso.codigo = turma.curso ");
		sqlStr.append("inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append("inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		if (!nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		montarSqlTipoPessoa(tipoPessoa, sqlStr);
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		sqlStr.append("union ");
		sqlStr.append("select distinct funcionario.*, pessoa.nome, pessoa.cpf ");
		sqlStr.append("from horarioturmaprofessordisciplina htpd ");
		sqlStr.append("inner join turma on turma.codigo = htpd.turma ");
		sqlStr.append("inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem ");
		sqlStr.append("inner join turma turma2 on turma2.codigo = turmaagrupada.turma ");
		sqlStr.append("inner join curso on curso.codigo = turma2.curso ");
		sqlStr.append("inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append("inner join funcionario on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		if (!nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		montarSqlTipoPessoa(tipoPessoa, sqlStr);
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%", Uteis.retirarMascaraCPF(valorConsulta.toLowerCase())+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<FuncionarioVO> consultaRapidaPorMatriculaNivelEducacional(String valorConsulta, String nivelEducacional, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct funcionario.*, pessoa.nome, pessoa.cpf ");
		sqlStr.append("from horarioturmaprofessordisciplina htpd ");
		sqlStr.append("inner join turma on turma.codigo = htpd.turma ");
		sqlStr.append("inner join curso on curso.codigo = turma.curso ");
		sqlStr.append("inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append("inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append("WHERE lower (funcionario.matricula) like(?)");
		if (!nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		montarSqlTipoPessoa(tipoPessoa, sqlStr);
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		sqlStr.append("union ");
		sqlStr.append("select distinct funcionario.*, pessoa.nome, pessoa.cpf ");
		sqlStr.append("from horarioturmaprofessordisciplina htpd ");
		sqlStr.append("inner join turma on turma.codigo = htpd.turma ");
		sqlStr.append("inner join turmaagrupada on turma.codigo = turmaagrupada.turmaorigem ");
		sqlStr.append("inner join turma turma2 on turma2.codigo = turmaagrupada.turma ");
		sqlStr.append("inner join curso on curso.codigo = turma2.curso ");
		sqlStr.append("inner join pessoa on pessoa.codigo = htpd.professor ");
		sqlStr.append("inner join funcionario on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE lower (funcionario.matricula) like(?)");
		if (!nivelEducacional.equals("0")) {
			sqlStr.append(" AND curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		montarSqlTipoPessoa(tipoPessoa, sqlStr);
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%", valorConsulta.toLowerCase()+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public FuncionarioVO realizarVerificacaoExistenciaFuncionarioJaRealizouTramite(Integer requerimento, Integer departamento, Integer cargo, Integer ordemExecucaoTramite, TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavel, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select distinct funcionario.codigo as codigo, pessoa.codigo as pessoa, pessoa.nome as nome, pessoa.email as email ");
		sql.append("from (select dataentradadepartamento, responsavelRequerimentoDepartamento, departamento, requerimento, ordemexecucaotramite  from requerimentohistorico ");
		sql.append("where requerimento = ").append(requerimento);
		sql.append(" order by dataentradadepartamento desc) as t ");
		sql.append("inner join pessoa on pessoa.codigo = t.responsavelRequerimentoDepartamento ");
		sql.append("inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append("left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append("where t.requerimento = ").append(requerimento);
		sql.append(" and t.departamento = ").append(departamento);
		if (Uteis.isAtributoPreenchido(ordemExecucaoTramite)) {
			sql.append(" and t.ordemexecucaotramite = ").append(ordemExecucaoTramite);
		} else {
			sql.append(" and pessoa.codigo <> ").append(usuario.getPessoa().getCodigo());
		}
		if (TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO.equals(tipoDistribuicaoResponsavel)) {
			sql.append(" and funcionariocargo.cargo = ").append(cargo);
		}
		sql.append(" limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			FuncionarioVO funcionarioVO = new FuncionarioVO();
			funcionarioVO.setNovoObj(false);
			funcionarioVO.setCodigo(rs.getInt("codigo"));
			funcionarioVO.getPessoa().setCodigo(rs.getInt("pessoa"));
			funcionarioVO.getPessoa().setNome(rs.getString("nome"));
			funcionarioVO.getPessoa().setEmail(rs.getString("email"));
			return funcionarioVO;
		}
		return null;
	}

	public List<FuncionarioVO> consultaRapidaPorMatriculaProfessorTitularDisciplinaTurma(String valorConsulta, Integer unidadeEnsino, Integer curso, String semestre, String ano, Boolean exerceCargoAdministrativo, Boolean ativo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");

		sqlStr.append("SELECT distinct funcionario.codigo, funcionario.exerceCargoAdministrativo, funcionario.matricula, funcionario.observacao, funcionario.escolaridade, funcionario.empresaRecebimento, funcionario.cnpjEmpresaRecebimento, pessoa.gerenciaPreInscricao AS \"funcionario.gerenciaPreInscricao\",  ");
		sqlStr.append("pessoa.nome AS \"funcionario.nome\", pessoa.codigo AS \"funcionario.codigo\", cidade.nome AS \"cidade.nome\", pessoa.cpf AS \"pessoa.cpf\", pessoa.email AS \"pessoa.email\", ");
		sqlStr.append("funcionarioCargo.codigo AS \"funcionarioCargo.codigo\", departamento.codigo AS \"departamento.codigo\", departamento.nome AS \"departamento.nome\"  ");
		sqlStr.append(" FROM funcionario ");
		sqlStr.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("LEFT JOIN usuario on usuario.pessoa = pessoa.codigo ");
		sqlStr.append("LEFT JOIN usuarioPerfilAcesso on usuarioPerfilAcesso.usuario = usuario.codigo ");
		sqlStr.append("LEFT JOIN UnidadeEnsino on usuarioPerfilAcesso.UnidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sqlStr.append("LEFT JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario ");
		sqlStr.append("LEFT JOIN cargo on cargo.codigo = funcionarioCargo.cargo  ");
		sqlStr.append("LEFT JOIN departamento on cargo.departamento = departamento.codigo ");
		sqlStr.append("INNER JOIN professorTitularDisciplinaTurma ON professorTitularDisciplinaTurma.professor = funcionario.pessoa ");
		sqlStr.append("LEFT JOIN turma ON turma.codigo = professorTitularDisciplinaTurma.turma ");

		sqlStr.append("WHERE funcionario.matricula = ? ");

		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND turma.curso = ").append(curso);
		}

		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND professorTitularDisciplinaTurma.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND professorTitularDisciplinaTurma.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(exerceCargoAdministrativo)) {
			sqlStr.append(" AND funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (Uteis.isAtributoPreenchido(ativo)) {
			sqlStr.append(" AND pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY professorTitularDisciplinaTurma.ano, professorTitularDisciplinaTurma.semestre, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		return montarDadosConsultaRapida(tabelaResultado);
	}

	@Override
	public FuncionarioVO consultarConsultorUltimaInteracaoProspectPorProspect(Integer codigoPessoaProspect, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select funcionario.codigo, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" from interacaoworkflow ");
		sb.append(" inner join usuario on usuario.codigo = interacaoworkflow.responsavel ");
		sb.append(" inner join funcionario on funcionario.pessoa = usuario.pessoa ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" inner join prospects on prospects.codigo = interacaoworkflow.prospect ");
		sb.append(" where prospects.pessoa = ").append(codigoPessoaProspect);
		sb.append(" order by interacaoworkflow.datainicio desc, interacaoworkflow.codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		FuncionarioVO obj = new FuncionarioVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
		}
		return obj;
	}
	
	@Override
	public List<FuncionarioVO> consultarProfessoresCoordenadorPorPeriodo(String valorConsulta, String tipoConsulta, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct pessoa.codigo as pessoa_codigo, pessoa.nome, funcionarioprofessor.matricula as matriculafuncionarioprofessor, funcionarioprofessor.codigo AS codigofuncionarioprofessor, pessoa.cpf, coordenador.nome as coordenador, coordenador.codigo from turma ");
		sb.append("inner join horarioturma on horarioturma.turma = turma.codigo ");
		sb.append("inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sb.append("inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sb.append("inner join pessoa on horarioturmadiaitem.professor = pessoa.codigo ");
		sb.append("left join turmaagrupada on turma.turmaagrupada and turma.codigo = turmaagrupada.turmaorigem ");
		sb.append("left join turma tu on tu.codigo = turmaagrupada.turma ");
		sb.append("inner join curso on (turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo = tu.curso) ");
		sb.append("inner join funcionario as funcionarioprofessor on pessoa.codigo = funcionarioprofessor.pessoa ");
		sb.append("inner join cursocoordenador on (cursocoordenador.turma is null and cursocoordenador.curso = curso.codigo) or ");
		sb.append("(cursocoordenador.turma is not null and ( ");
		sb.append("(turma.turmaagrupada = false and cursocoordenador.turma = turma.codigo) ");
		sb.append("or (turma.turmaagrupada = false and turma.subturma and turma.turmaprincipal = cursocoordenador.turma) ");
		sb.append("or (turma.turmaagrupada and cursocoordenador.turma in (select t.codigo from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo)))) ");
		sb.append("inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sb.append("inner join pessoa as coordenador on funcionario.pessoa = coordenador.codigo ");
		sb.append("where coordenador.codigo = ").append(usuario.getPessoa().getCodigo());
		if (tipoConsulta.equals("nome")) {
			valorConsulta += PERCENT;
			sb.append(" and lower (sem_acentos(pessoa.nome)) like lower(sem_acentos(?)) ");
		} else {
			sb.append(" and funcionarioprofessor.matricula = ?");
		}
		sb.append(" order by pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta);
		List<FuncionarioVO> vetResultado = new ArrayList<FuncionarioVO>(0);
		
		while (tabelaResultado.next()) {
			FuncionarioVO obj = new FuncionarioVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigofuncionarioprofessor")));
			obj.setMatricula(new String(tabelaResultado.getString("matriculafuncionarioprofessor")));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			obj.getPessoa().setCPF(tabelaResultado.getString("cpf"));
			vetResultado.add(obj);
		}
		return vetResultado;
		
	}
	
	@Override
	public List<FuncionarioVO> consultaRapidaPorSecao(String valorConsulta, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(lower(secao.descricao)) like(sem_acentos(lower(?)))");
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public Integer consultaTotalDeRegistroRapidaPorSecao(String valorConsulta, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLCountPadraoConsultaBasica());
		sqlStr.append(" WHERE sem_acentos(lower(secao.descricao)) like(sem_acentos(lower(?)))");
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		if (resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;
	}

	@Override
	public List<FuncionarioVO> consultaRapidaPorFormaContratacao(String valorConsulta, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(funcionarioCargo.formacontratacao) ilike(sem_acentos(?))");
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		return montarDadosConsultaRapida(tabelaResultado);
	}
	
	public Integer consultaTotalDeRegistroRapidaPorFormaContratacao(String valorConsulta, Boolean ativo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(getSQLCountPadraoConsultaBasica());
		sqlStr.append(" WHERE sem_acentos(funcionarioCargo.formacontratacao) ilike(sem_acentos(?))");
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
			sqlStr.append(" and funcionarioCargo.ativo ");
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + "%");
		if (resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;
	}
	
	@Override
	public List<FuncionarioVO> consultarFuncionarioAtivoPorMatricula(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario, Boolean ativo) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
		StringBuffer sqlStr = new StringBuffer();
		if (unidadeEnsino.intValue() == 0) {
			sqlStr.append("SELECT funcionario.* FROM Funcionario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" WHERE lower (funcionario.matricula) like( '" + valorConsulta.toLowerCase() + "%')");
			if (ativo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");
			}
		} else {
			sqlStr.append("SELECT DISTINCT funcionario.* FROM Funcionario,Pessoa,FuncionarioCargo ");
			sqlStr.append(" WHERE (FuncionarioCargo.funcionario = Funcionario.codigo) AND lower (funcionario.matricula) like( '" + valorConsulta.toLowerCase() + "%') and pessoa.codigo=funcionario.pessoa ");
			sqlStr.append(" and FuncionarioCargo.unidadeEnsino = " + unidadeEnsino.intValue());
			if (ativo) {
				sqlStr.append(" and funcionariocargo.ativo = true ");
			}
			sqlStr.append(" ORDER BY funcionario.matricula");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public FuncionarioVO consultaCordenadorPorCurso(String codigoMatricula, Integer codigoCurso, String codigoTurma,Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuarioVO);		
		SqlRowSet tabelaResultado = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select * from ( ");
		sql.append("select 	case when ccor.unidadeensino is not null and ccor.curso is not null 	and ccor.turma is not null then 3 ");
		sql.append("when ccor.unidadeensino is not null and ccor.curso is not null 	and ccor.turma is null then 2 ");
		sql.append("when ccor.unidadeensino is null and ccor.curso is not null 	and ccor.turma is null then 1 ");
		sql.append("else 0 	end as nivelCoordenador, ");
		sql.append("case ");
		sql.append("when formacaoacademica.escolaridade = 'PD' then 9 ");
		sql.append("when formacaoacademica.escolaridade = 'DR' then 8 ");
		sql.append("when formacaoacademica.escolaridade = 'MS' then 7 ");
		sql.append("when formacaoacademica.escolaridade = 'PO' then 6 ");
		sql.append("when formacaoacademica.escolaridade = 'EP' then 5 ");
		sql.append("when formacaoacademica.escolaridade = 'GR' then 4 ");
		sql.append("when formacaoacademica.escolaridade = 'TE' then 3 ");
		sql.append("when formacaoacademica.escolaridade = 'EM' then 2 ");
		sql.append("when formacaoacademica.escolaridade = 'EF' then 1 ");
		sql.append("else 0	end as niveEscolaridade , ");
		sql.append("pessoa.nome as   nomefuncionario  , c.nome,fun.* from funcionario fun ");
		sql.append("inner join pessoa on pessoa.codigo = fun.pessoa ");
		sql.append("inner join cursocoordenador ccor on 	ccor.funcionario = fun.codigo ");
		sql.append("inner join curso c on 	c.codigo = ccor.curso ");
		sql.append("inner join matricula mat on mat.curso = c.codigo ");
		sql.append("left join formacaoacademica on 	formacaoacademica.pessoa = pessoa.codigo ");
		sql.append("where 	mat.matricula = ?  and ccor.tipocoordenadorcurso != 'ESTAGIO' and pessoa.ativo ");
		sql.append("and ( ");
		//if(Uteis.isAtributoPreenchido(codigoCurso)&& Uteis.isAtributoPreenchido(codigoUnidadeEnsino)&& Uteis.isAtributoPreenchido(codigoTurma)) {
		 sql.append("  ( ccor.unidadeensino = "+codigoUnidadeEnsino+" and ccor.curso = "+codigoCurso+"  and ccor.turma = "+codigoTurma+" )");
		//}
		//if(Uteis.isAtributoPreenchido(codigoCurso) && Uteis.isAtributoPreenchido(codigoUnidadeEnsino)&& !Uteis.isAtributoPreenchido(codigoTurma)) {
			sql.append(" or ( ccor.unidadeensino = "+codigoUnidadeEnsino+" and ccor.curso = "+codigoCurso+" and ccor.turma is null )");
		//}
		//if(Uteis.isAtributoPreenchido(codigoCurso)&& !Uteis.isAtributoPreenchido(codigoUnidadeEnsino)&& !Uteis.isAtributoPreenchido(codigoTurma)) {
			sql.append(" or ( ccor.unidadeensino is null and ccor.curso = "+codigoCurso+" and ccor.turma is null ) ");
		//}
		sql.append(" )");
		sql.append(" )");
		sql.append(" as t  order by   t.nivelCoordenador desc, t.niveEscolaridade desc , nomefuncionario   limit 1 ");		
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoMatricula);
		
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
	}
	
	public List<FuncionarioVO> consultarFuncionarioUnificar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
			throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
		}
		boolean campoConsultaEncontrado = false;
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("SELECT Funcionario.* FROM Funcionario ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (campoConsulta.equals("nome")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" WHERE ");
			sqlStr.append(" sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
		} else if (campoConsulta.equals("matricula")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" WHERE ");
			sqlStr.append("funcionario.matricula ilike(?)");
		} else if (campoConsulta.equals("CPF")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" WHERE ");
			sqlStr.append("(replace(replace(Pessoa.CPF,'.',''),'-','')) like(?) ");
			valorConsulta = Uteis.retirarMascaraCPF(valorConsulta);
		} else if (campoConsulta.equals("cargo")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" left join cargo on funcionariocargo.cargo = cargo.codigo  ");
			sqlStr.append(" WHERE ");
			sqlStr.append(" sem_acentos(Cargo.nome) ilike(sem_acentos(?)) ");
		} else if (campoConsulta.equals("departamento")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" left join cargo on funcionariocargo.cargo = cargo.codigo  ");
			sqlStr.append(" left join departamento on cargo.departamento = departamento.codigo  ");
			sqlStr.append(" WHERE ");
			sqlStr.append(" sem_acentos(Departamento.nome) ilike(sem_acentos(?)) ");
		} else if (campoConsulta.equals("unidadeEnsino")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo  ");
			sqlStr.append(" left join unidadeensino on unidadeensino.codigo = funcionariocargo.unidadeensino ");
			sqlStr.append(" WHERE sem_acentos(unidadeensino.nome) ilike(sem_acentos(?)) ");
		} else if (campoConsulta.equals("secao")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" left join secaofolhapagamento as secao on secao.codigo = funcionarioCargo.secaofolhapagamento ");
			sqlStr.append(" where sem_acentos(secao.descricao) ilike(sem_acentos(?)) ");
		} else if (campoConsulta.equals("formaContratacao")) {
			campoConsultaEncontrado = true;
			sqlStr.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sqlStr.append(" WHERE sem_acentos(funcionariocargo.formacontratacao) ilike(sem_acentos(?)) ");
		}
		if (campoConsultaEncontrado) {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta +"%");
			return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
		}
		return new ArrayList<>();
	}
	
	@Override
	public FuncionarioVO consultarProfessorTitularPorCursoDisciplinaAnoSemestre(Integer curso, Integer disciplina, String ano, String semestre, boolean verificarTitular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		SqlRowSet tabelaResultado = null;
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN professortitulardisciplinaturma ptdt ON pessoa.codigo = ptdt.professor");
		sqlStr.append(" LEFT JOIN formacaoacademica ON formacaoacademica.pessoa = pessoa.codigo");
		sqlStr.append(" WHERE ptdt.curso = ? AND ptdt.disciplina = ").append(disciplina.intValue()).append(" AND ptdt.titular = ").append(verificarTitular);
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND ptdt.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND ptdt.semestre = '").append(semestre).append("' ");
		}
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), curso);
		
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<FuncionarioVO> consultarProfessoresPorCursoDisciplinaAnoSemestre(Integer curso, Integer disciplina, String ano, String semestre,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		SqlRowSet tabelaResultado = null;
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT funcionario.* FROM funcionario");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN professortitulardisciplinaturma ptdt ON pessoa.codigo = ptdt.professor");
		sqlStr.append(" LEFT JOIN formacaoacademica ON formacaoacademica.pessoa = pessoa.codigo");
		sqlStr.append(" WHERE ptdt.curso = ? AND ptdt.disciplina = ").append(disciplina.intValue());
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND ptdt.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND ptdt.semestre = '").append(semestre).append("' ");
		}
		
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), curso);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public void adicionarTipoDocumento(List<TipoDocumentoVO> listaTipoDocumentoVOs, TipoDocumentoVO tipoDocumentoVO) {
		validarDados(listaTipoDocumentoVOs,tipoDocumentoVO);
		listaTipoDocumentoVOs.add(tipoDocumentoVO);
	}


	@Override
	public boolean realizarVerificacaoPessoaVinculadaFuncionario(Integer codigoPessoa, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT codigo FROM Funcionario WHERE pessoa = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPessoa });
		if(tabelaResultado.next()) {
			return true;
		}else {
			return false;
		}
	}
	
	private void validarDados(List<TipoDocumentoVO> listaTipoDocumentoVOs, TipoDocumentoVO tipoDocumentoVO) {
		Uteis.checkState(listaTipoDocumentoVOs.stream().anyMatch(tp -> tp.getCodigo().equals(tipoDocumentoVO.getCodigo())), "Esse Tipo de Documento já foi adicionado.");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void persistirFuncionarioComEmailInstituciona(final FuncionarioVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception{
		if(obj.isNovoObj()) {
			incluir(obj, usuario, configuracaoGeralSistema);
		} else {
			alterar(obj, usuario, configuracaoGeralSistema);
		}
//		if(obj.getPessoa().getListaPessoaGsuite().isEmpty()) {
//			getFacadeFactory().getPessoaGsuiteFacade().persistir(obj.getPessoa(), false, usuario);
//		}
		getFacadeFactory().getPessoaEmailInstitucionalFacade().persistir(obj.getPessoa(), false, usuario);
		UsuarioVO usuarioExistente = getFacadeFactory().getUsuarioFacade().validarUsuarioExistente(obj.getPessoa(), obj.getMatricula(), usuario);
		if(!obj.isNaoNotificarInclusaoUsuario() &&
				(usuarioExistente == null 
				|| !Uteis.isAtributoPreenchido(usuarioExistente.getCodigo()) 
				|| (Uteis.isAtributoPreenchido(usuarioExistente.getCodigo()) && usuarioExistente.getTipoUsuario().equals("AL")))) {
			obj.setCriarUsuario(true);	
		}else if(Uteis.isAtributoPreenchido(usuarioExistente)) {
			realizarVerificacaoUsuarioRegistradoLdap(obj, usuarioExistente, Uteis.removerMascara(obj.getPessoa().getCPF()), usuario);
		}
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarCriacaoUsuarioPorFuncionario(FuncionarioVO funcionarioVO, UsuarioPerfilAcessoVO usuarioPerfilAcessoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(usuarioPerfilAcessoVO.getPerfilAcesso()), "O campo Perfil Acesso deve ser informado.");
		UsuarioVO usuarioIncluir = new UsuarioVO();
		usuarioIncluir.setPessoa(funcionarioVO.getPessoa());
		usuarioIncluir.setNome(funcionarioVO.getPessoa().getNome());
		usuarioIncluir.setUsername(getFacadeFactory().getUsuarioFacade().executarVerificacaoDeUsernameValida(funcionarioVO.getPessoa(), 1, 40));
		usuarioIncluir.setSenha(Uteis.removerMascara(funcionarioVO.getPessoa().getCPF()));
		usuarioIncluir.setTipoUsuario("FU");
        UsuarioPerfilAcessoVO usuarioPerfilAcesso = new UsuarioPerfilAcessoVO();
        usuarioPerfilAcesso.getUnidadeEnsinoVO().setCodigo(usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo());
        usuarioPerfilAcesso.setPerfilAcesso(usuarioPerfilAcessoVO.getPerfilAcesso());
        usuarioIncluir.adicionarObjUsuarioPerfilAcessoAPartirMatriculaVOs(usuarioPerfilAcesso);
		String senhaAntesCriptografia = usuarioIncluir.getSenha();
		getFacadeFactory().getUsuarioFacade().incluir(usuarioIncluir, false, usuarioLogado);
		realizarVerificacaoUsuarioRegistradoLdap(funcionarioVO, usuarioIncluir, senhaAntesCriptografia, usuarioLogado);
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoUsuarioRegistradoLdap(FuncionarioVO obj, UsuarioVO usuarioLdap,String senhaAntesCriptografia, UsuarioVO usuarioLogado) throws Exception {
		Map<DepartamentoVO, List<FuncionarioCargoVO>> map = obj.getFuncionarioCargoVOs()
				.stream()
				.filter(p-> p.getAtivo())
			 	.collect(Collectors.groupingBy(p-> Uteis.isAtributoPreenchido(p.getDepartamento()) ? p.getDepartamento() : p.getCargo().getDepartamento()));
			 			
		for (Map.Entry<DepartamentoVO, List<FuncionarioCargoVO>> mapaDepartamentoVO : map.entrySet()) {
			if(Uteis.isAtributoPreenchido(mapaDepartamentoVO.getKey())) {
				ConfiguracaoLdapVO confLdap = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorCodigoDepartamento(mapaDepartamentoVO.getKey().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
				PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(usuarioLdap.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
				if(Uteis.isAtributoPreenchido(confLdap) && !getFacadeFactory().getLdapFacade().consultarSeUsuarioExisteLdap(confLdap, usuarioLdap)) {
					if(Uteis.isAtributoPreenchido(confLdap.getPrefixoSenha())) {
						senhaAntesCriptografia = confLdap.getPrefixoSenha() + senhaAntesCriptografia;	
					}
					if(!Uteis.isAtributoPreenchido(emailInstitucional)){
						emailInstitucional =  getFacadeFactory().getPessoaEmailInstitucionalFacade().incluirPessoaEmailInstitucional(confLdap, null , false,  usuarioLdap,usuarioLogado);
					}
					usuarioLdap.setPossuiCadastroLdap(false);
					getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoIncluirUsuario(confLdap, usuarioLdap, senhaAntesCriptografia, null ,emailInstitucional,usuarioLogado);
					obj.setPessoa(usuarioLdap.getPessoa());
				}else if(Uteis.isAtributoPreenchido(confLdap) 
						//&& getFacadeFactory().getLdapFacade().consultarSeUsuarioExisteLdap(confLdap, usuarioLdap) 
						&& obj.getPessoa().getListaPessoaEmailInstitucionalVO().isEmpty()) {
					if(!Uteis.isAtributoPreenchido(emailInstitucional)){
						getFacadeFactory().getPessoaEmailInstitucionalFacade().incluirPessoaEmailInstitucional(confLdap, null, true, usuarioLdap,usuarioLogado);
					}
					obj.setPessoa(usuarioLdap.getPessoa());
				}	
			}
		}
	}
	
	
	@Override                                         
	public List<FuncionarioVO> consultaRapidaResumidaPorEmailInstitucional(String valorConsulta,  Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados,DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);		
		SqlRowSet tabelaResultado = null ;	
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sqlStr.append(getSQLPadraoConsultaBasica());				
		if (valorConsulta.replace("%", "").trim().isEmpty()) {
			sqlStr.append("WHERE 1=1 ");
		} else {
			sqlStr.append("WHERE exists (select codigo from pessoaemailinstitucional where pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.email ilike  ? ) ");
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
	public List<FuncionarioVO> consultaRapidaCoordenadorPorCurso(Integer codigoCurso, Integer codigoUnidadeEnsino, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT funcionario.codigo AS \"funcionario.codigo\", funcionario.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome, pessoa.cpf from cursocoordenador ");
		sql.append("INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append("WHERE cursocoordenador.curso = ? AND cursocoordenador.turma IS NULL AND cursocoordenador.tipocoordenadorcurso IN ('AMBOS', 'GERAL') ");
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			sql.append("AND (cursocoordenador.unidadeensino = ").append(codigoUnidadeEnsino).append(" OR cursocoordenador.unidadeensino IS NULL) ");
		}
		sql.append("ORDER BY cursocoordenador.unidadeensino ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCurso);
		List vetResultado = new ArrayList(0);
		FuncionarioVO obj = null;
		while (tabelaResultado.next()) {
			obj = new FuncionarioVO();
			obj.setCodigo(tabelaResultado.getInt("funcionario.codigo"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCPF(tabelaResultado.getString("cpf"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Override
	public void carregarDadosSemExcecao(FuncionarioVO obj, UsuarioVO usuario) throws Exception {
		consultaRapidaPorChavePrimariaSemExcecao(obj.getCodigo(), Boolean.FALSE, usuario);
	}

	@Override
	public FuncionarioVO consultarProfessorComAulaProgramadaPorMatriculaDisciplinaNome(String matricula,
			Integer disciplina, String nomeProfessor, Integer unidadeEnsino, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
