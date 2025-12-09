package negocio.facade.jdbc.estagio;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioFuncionarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.ConfiguracaoEstagioObrigatorioInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoEstagioObrigatorio  extends ControleAcesso implements ConfiguracaoEstagioObrigatorioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3609808227295170072L;
	
	private static String idEntidade = "ConfiguracaoEstagioObrigatorio";

	public static String getIdEntidade() {
		return ConfiguracaoEstagioObrigatorio.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(ConfiguracaoEstagioObrigatorioVO obj) {
//		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), UteisJSF.internacionalizar("msg_ConfiguracaoEstagioObrigatorio_nome"));
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoEstagioObrigatorioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			valiarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConfiguracaoEstagioObrigatorioFuncionarioVO(), "configuracaoEstagioObrigatorioFuncionario", idEntidade, obj.getCodigo(), usuarioVO);
			getFacadeFactory().getConfiguracaoEstagioObrigatorioFuncionarioFacade().persistir(obj.getListaConfiguracaoEstagioObrigatorioFuncionarioVO(), false, usuarioVO);			
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoEstagioObrigatorioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "configuracaoEstagioObrigatorio", new AtributoPersistencia()
					.add("fonteDeDadosBlackboardEstagio", obj.getFonteDeDadosBlackboardEstagio())
					.add("fonteDeDadosBlackboardComponenteEstagio", obj.getFonteDeDadosBlackboardComponenteEstagio())
					.add("textoOrientacaoAberturaTermo", obj.getTextoOrientacaoAberturaTermo())
					.add("textoOrientacaoEntregaRelatorioFinal", obj.getTextoOrientacaoEntregaRelatorioFinal())
					.add("funcionarioTestemunhaAssinatura1", obj.getFuncionarioTestemunhaAssinatura1())
					.add("funcionarioTestemunhaAssinatura2", obj.getFuncionarioTestemunhaAssinatura2())
					.add("qtdDiasMaximoParaAssinaturaEstagio", obj.getQtdDiasMaximoParaAssinaturaEstagio())
					.add("qtdDiasNotificacaoAssinaturaEstagio", obj.getQtdDiasNotificacaoAssinaturaEstagio())
					.add("qtdMinimaMantidoPorFacilitador", obj.getQtdMinimaMantidoPorFacilitador())
					.add("qtdVagasPorSalaEstagio", obj.getQtdVagasPorSalaEstagio())
					.add("qtdDiasMaximoParaAnaliseRelatoriofinal", obj.getQtdDiasMaximoParaAnaliseRelatoriofinal())
					.add("periodicidadeParaNotificacaoEntregaRelatorioFinal", obj.getPeriodicidadeParaNotificacaoEntregaRelatorioFinal())
					.add("textoOrientacaoAnaliseRelatorioFinal", obj.getTextoOrientacaoAnaliseRelatorioFinal())
					.add("qtdDiasMaximoParaCorrecaoRelatorioFinal", obj.getQtdDiasMaximoParaCorrecaoRelatorioFinal())
					.add("periodicidadeParaNotificacaoEntregaNovoRelatorioFinal", obj.getPeriodicidadeParaNotificacaoEntregaNovoRelatorioFinal())
					.add("textoOrientacaoSolicitacaoAproveitamento", obj.getTextoOrientacaoSolicitacaoAproveitamento())
					.add("qtdDiasMaximoParaRespostaAnaliseAproveitamento", obj.getQtdDiasMaximoParaRespostaAnaliseAproveitamento())
					.add("periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento())
					.add("qtdDiasMaximoRespostaRetornoAnaliseAproveitamento", obj.getQtdDiasMaximoRespostaRetornoAnaliseAproveitamento())
					.add("periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento())
					.add("textoOrientacaoSolicitacaoEquivalencia", obj.getTextoOrientacaoSolicitacaoEquivalencia())
					.add("qtdDiasMaximoParaRespostaAnaliseEquivalencia", obj.getQtdDiasMaximoParaRespostaAnaliseEquivalencia())
					.add("periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia())
					.add("qtdDiasMaximoRespostaRetornoAnaliseEquivalencia", obj.getQtdDiasMaximoRespostaRetornoAnaliseEquivalencia())
					.add("periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoEstagioObrigatorioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "configuracaoEstagioObrigatorio", new AtributoPersistencia()
					.add("fonteDeDadosBlackboardEstagio", obj.getFonteDeDadosBlackboardEstagio())
					.add("fonteDeDadosBlackboardComponenteEstagio", obj.getFonteDeDadosBlackboardComponenteEstagio())
					.add("textoOrientacaoAberturaTermo", obj.getTextoOrientacaoAberturaTermo())
					.add("textoOrientacaoEntregaRelatorioFinal", obj.getTextoOrientacaoEntregaRelatorioFinal())
					.add("funcionarioTestemunhaAssinatura1", obj.getFuncionarioTestemunhaAssinatura1())
					.add("funcionarioTestemunhaAssinatura2", obj.getFuncionarioTestemunhaAssinatura2())
					.add("qtdDiasMaximoParaAssinaturaEstagio", obj.getQtdDiasMaximoParaAssinaturaEstagio())
					.add("qtdDiasNotificacaoAssinaturaEstagio", obj.getQtdDiasNotificacaoAssinaturaEstagio())
					.add("qtdMinimaMantidoPorFacilitador", obj.getQtdMinimaMantidoPorFacilitador())
					.add("qtdVagasPorSalaEstagio", obj.getQtdVagasPorSalaEstagio())
					.add("qtdDiasMaximoParaAnaliseRelatoriofinal", obj.getQtdDiasMaximoParaAnaliseRelatoriofinal())
					.add("periodicidadeParaNotificacaoEntregaRelatorioFinal", obj.getPeriodicidadeParaNotificacaoEntregaRelatorioFinal())
					.add("textoOrientacaoAnaliseRelatorioFinal", obj.getTextoOrientacaoAnaliseRelatorioFinal())
					.add("qtdDiasMaximoParaCorrecaoRelatorioFinal", obj.getQtdDiasMaximoParaCorrecaoRelatorioFinal())
					.add("periodicidadeParaNotificacaoEntregaNovoRelatorioFinal", obj.getPeriodicidadeParaNotificacaoEntregaNovoRelatorioFinal())
					.add("textoOrientacaoSolicitacaoAproveitamento", obj.getTextoOrientacaoSolicitacaoAproveitamento())
					.add("qtdDiasMaximoParaRespostaAnaliseAproveitamento", obj.getQtdDiasMaximoParaRespostaAnaliseAproveitamento())
					.add("periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento())
					.add("qtdDiasMaximoRespostaRetornoAnaliseAproveitamento", obj.getQtdDiasMaximoRespostaRetornoAnaliseAproveitamento())
					.add("periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento())
					.add("textoOrientacaoSolicitacaoEquivalencia", obj.getTextoOrientacaoSolicitacaoEquivalencia())
					.add("qtdDiasMaximoParaRespostaAnaliseEquivalencia", obj.getQtdDiasMaximoParaRespostaAnaliseEquivalencia())
					.add("periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia())
					.add("qtdDiasMaximoRespostaRetornoAnaliseEquivalencia", obj.getQtdDiasMaximoRespostaRetornoAnaliseEquivalencia())
					.add("periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia", obj.getPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoEstagioObrigatorioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM configuracaoEstagioObrigatorio WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	
	
	@Override
	
	public void adicionarConfiguracaoEstagioObrigatorioFuncionarioVO(ConfiguracaoEstagioObrigatorioVO ceo,  ConfiguracaoEstagioObrigatorioFuncionarioVO ceoFuncionario, UsuarioVO usuario) {
		ceoFuncionario.setConfiguracaoEstagioObrigatorioVO(ceo);
		if(ceo.getListaConfiguracaoEstagioObrigatorioFuncionarioVO().stream().noneMatch(p-> p.getFuncionarioVO().getCodigo().equals(ceoFuncionario.getFuncionarioVO().getCodigo()))) {
			ceo.getListaConfiguracaoEstagioObrigatorioFuncionarioVO().add(ceoFuncionario);	
		}
	}
	
	@Override	
	public void removerConfiguracaoEstagioObrigatorioFuncionarioVO(ConfiguracaoEstagioObrigatorioVO ceo,  ConfiguracaoEstagioObrigatorioFuncionarioVO ceoFuncionario, UsuarioVO usuario) {		
		ceo.getListaConfiguracaoEstagioObrigatorioFuncionarioVO().removeIf(p-> p.getFuncionarioVO().getCodigo().equals(ceoFuncionario.getFuncionarioVO().getCodigo()));
	}
	

	@Override
	public ConfiguracaoEstagioObrigatorioVO consultarPorConfiguracaoEstagioPadrao(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		return getAplicacaoControle().getConfiguracaoEstagioObrigatorioPadraoVO();
	}
	
	@Override
	
	public ConfiguracaoEstagioObrigatorioVO consultarPorConfiguracaoEstagioPadraoUnico(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return new ConfiguracaoEstagioObrigatorioVO ();
			}
			tabelaResultado.beforeFirst();
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}	
	}

	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(" select ");		
		sql.append(" configuracaoEstagioObrigatorio.codigo, ");
		sql.append(" fonteDeDadosBlackboardEstagio, "); 
		sql.append(" fonteDeDadosBlackboardComponenteEstagio, "); 
		sql.append(" textoOrientacaoAberturaTermo, "); 
		sql.append(" textoOrientacaoEntregaRelatorioFinal ,");		
		sql.append(" qtdDiasMaximoParaAssinaturaEstagio, ");
		sql.append(" qtdMinimaMantidoPorFacilitador, ");
		sql.append(" qtdVagasPorSalaEstagio, ");
		sql.append(" qtdDiasMaximoParaAnaliseRelatoriofinal, ");
		sql.append(" qtdDiasNotificacaoAssinaturaEstagio, ");
		sql.append(" periodicidadeParaNotificacaoEntregaRelatorioFinal, ");
		sql.append(" textoOrientacaoAnaliseRelatorioFinal, ");
		sql.append(" qtdDiasMaximoParaCorrecaoRelatorioFinal, ");
		sql.append(" periodicidadeParaNotificacaoEntregaNovoRelatorioFinal, ");
		sql.append(" textoOrientacaoSolicitacaoAproveitamento, ");
		sql.append(" qtdDiasMaximoParaRespostaAnaliseAproveitamento, ");
		sql.append(" periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento, ");
		sql.append(" qtdDiasMaximoRespostaRetornoAnaliseAproveitamento, ");
		sql.append(" periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento as \"periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAprovei\", ");
		sql.append(" textoOrientacaoSolicitacaoEquivalencia, ");
		sql.append(" qtdDiasMaximoParaRespostaAnaliseEquivalencia, ");
		sql.append(" periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia, ");
		sql.append(" qtdDiasMaximoRespostaRetornoAnaliseEquivalencia, ");
		sql.append(" periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia as \"periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquival\", ");		
		sql.append(" funcionarioTestemunhaAssinatura1, pessoa1.codigo as \"pessoa1.codigo\", pessoa1.nome as \"pessoa1.nome\", pessoa1.cpf as \"pessoa1.cpf\", pessoa1.tipoAssinaturaDocumentoEnum as \"pessoa1.tipoAssinaturaDocumentoEnum\", ");
		sql.append(" funcionarioTestemunhaAssinatura2, pessoa2.codigo as \"pessoa2.codigo\", pessoa2.nome as \"pessoa2.nome\", pessoa2.cpf as \"pessoa2.cpf\", pessoa2.tipoAssinaturaDocumentoEnum as \"pessoa2.tipoAssinaturaDocumentoEnum\", ");
		
		sql.append(" ceof.codigo as \"ceof.codigo\",  ");
		sql.append(" ceof.funcionario as \"ceof.funcionario\", ceoFun.matricula as \"ceoFun.matricula\", ");
		sql.append(" ceopessoa.codigo as \"ceopessoa.codigo\", ceopessoa.nome as \"ceopessoa.nome\", ceopessoa.cpf as \"ceopessoa.cpf\", ceopessoa.tipoAssinaturaDocumentoEnum as \"ceopessoa.tipoAssinaturaDocumentoEnum\", ");
		
		sql.append(" ceopessoaemailinstitucional.codigo as \"ceopessoaemailinstitucional.codigo\", ");
		sql.append(" ceopessoaemailinstitucional.email as \"ceopessoaemailinstitucional.email\", ");
		sql.append(" ceopessoaemailinstitucional.statusAtivoInativoEnum as \"ceopessoaemailinstitucional.statusAtivoInativoEnum\" ");
		
		sql.append(" FROM configuracaoEstagioObrigatorio ");
		sql.append(" left join funcionario fun1 on fun1.codigo = configuracaoEstagioObrigatorio.funcionarioTestemunhaAssinatura1 ");
		sql.append(" left join pessoa pessoa1 on pessoa1.codigo = fun1.pessoa ");
		sql.append(" left join funcionario fun2 on fun2.codigo = configuracaoEstagioObrigatorio.funcionarioTestemunhaAssinatura2 ");
		sql.append(" left join pessoa pessoa2 on pessoa2.codigo = fun2.pessoa ");
		sql.append(" left  join configuracaoEstagioObrigatorioFuncionario ceof on ceof.configuracaoEstagioObrigatorio = configuracaoEstagioObrigatorio.codigo ");
		sql.append(" left  join funcionario ceoFun on ceoFun.codigo = ceof.funcionario ");
		sql.append(" left  join pessoa ceopessoa on ceopessoa.codigo = ceoFun.pessoa ");
		sql.append(" left  join pessoaemailinstitucional ceopessoaemailinstitucional on ceopessoaemailinstitucional.pessoa = ceopessoa.codigo ");
		

		return sql;
	}	

	private ConfiguracaoEstagioObrigatorioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		ConfiguracaoEstagioObrigatorioVO obj = new ConfiguracaoEstagioObrigatorioVO();
		while (dadosSQL.next()) {
			if(!Uteis.isAtributoPreenchido(obj)){
				obj.setNovoObj(Boolean.FALSE);
				obj.setCodigo(dadosSQL.getInt("codigo"));
				obj.setFonteDeDadosBlackboardEstagio(dadosSQL.getString("fonteDeDadosBlackboardEstagio"));
				obj.setFonteDeDadosBlackboardComponenteEstagio(dadosSQL.getString("fonteDeDadosBlackboardComponenteEstagio"));
				obj.setTextoOrientacaoAberturaTermo(dadosSQL.getString("textoOrientacaoAberturaTermo"));
				obj.setTextoOrientacaoEntregaRelatorioFinal(dadosSQL.getString("textoOrientacaoEntregaRelatorioFinal"));
				obj.setQtdMinimaMantidoPorFacilitador(dadosSQL.getInt("qtdMinimaMantidoPorFacilitador"));
				obj.setQtdVagasPorSalaEstagio(dadosSQL.getInt("qtdVagasPorSalaEstagio"));
				obj.setQtdDiasMaximoParaAssinaturaEstagio(dadosSQL.getInt("qtdDiasMaximoParaAssinaturaEstagio"));
				obj.setQtdDiasNotificacaoAssinaturaEstagio(dadosSQL.getInt("qtdDiasNotificacaoAssinaturaEstagio"));
				obj.setQtdDiasMaximoParaAnaliseRelatoriofinal(dadosSQL.getInt("qtdDiasMaximoParaAnaliseRelatoriofinal"));
				obj.setPeriodicidadeParaNotificacaoEntregaRelatorioFinal(dadosSQL.getInt("periodicidadeParaNotificacaoEntregaRelatorioFinal"));
				obj.setTextoOrientacaoAnaliseRelatorioFinal(dadosSQL.getString("textoOrientacaoAnaliseRelatorioFinal"));
				obj.setQtdDiasMaximoParaCorrecaoRelatorioFinal(dadosSQL.getInt("qtdDiasMaximoParaCorrecaoRelatorioFinal"));
				obj.setPeriodicidadeParaNotificacaoEntregaNovoRelatorioFinal(dadosSQL.getInt("periodicidadeParaNotificacaoEntregaNovoRelatorioFinal"));
				obj.setTextoOrientacaoSolicitacaoAproveitamento(dadosSQL.getString("textoOrientacaoSolicitacaoAproveitamento"));
				obj.setQtdDiasMaximoParaRespostaAnaliseAproveitamento(dadosSQL.getInt("qtdDiasMaximoParaRespostaAnaliseAproveitamento"));
				obj.setPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento(dadosSQL.getInt("periodicidadeParaNotificacaoAtrasoRespostaAnaliseAproveitamento"));
				obj.setQtdDiasMaximoRespostaRetornoAnaliseAproveitamento(dadosSQL.getInt("qtdDiasMaximoRespostaRetornoAnaliseAproveitamento"));
				obj.setPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAproveitamento(dadosSQL.getInt("periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseAprovei"));
				obj.setTextoOrientacaoSolicitacaoEquivalencia(dadosSQL.getString("textoOrientacaoSolicitacaoEquivalencia"));
				obj.setQtdDiasMaximoParaRespostaAnaliseEquivalencia(dadosSQL.getInt("qtdDiasMaximoParaRespostaAnaliseEquivalencia"));
				obj.setPeriodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia(dadosSQL.getInt("periodicidadeParaNotificacaoAtrasoRespostaAnaliseEquivalencia"));
				obj.setQtdDiasMaximoRespostaRetornoAnaliseEquivalencia(dadosSQL.getInt("qtdDiasMaximoRespostaRetornoAnaliseEquivalencia"));
				obj.setPeriodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquivalencia(dadosSQL.getInt("periodicidadeParaNotificacaoAtrasoRespostaRetornoAnaliseEquival"));
				
				obj.getFuncionarioTestemunhaAssinatura1().setCodigo(dadosSQL.getInt("funcionarioTestemunhaAssinatura1"));
				obj.getFuncionarioTestemunhaAssinatura1().getPessoa().setCodigo(dadosSQL.getInt("pessoa1.codigo"));
				obj.getFuncionarioTestemunhaAssinatura1().getPessoa().setNome(dadosSQL.getString("pessoa1.nome"));
				obj.getFuncionarioTestemunhaAssinatura1().getPessoa().setCPF(dadosSQL.getString("pessoa1.cpf"));
				if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoa1.tipoAssinaturaDocumentoEnum"))){
					obj.getFuncionarioTestemunhaAssinatura1().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("pessoa1.tipoAssinaturaDocumentoEnum")));	
				}
				obj.getFuncionarioTestemunhaAssinatura2().setCodigo(dadosSQL.getInt("funcionarioTestemunhaAssinatura2"));
				obj.getFuncionarioTestemunhaAssinatura2().getPessoa().setCodigo(dadosSQL.getInt("pessoa2.codigo"));
				obj.getFuncionarioTestemunhaAssinatura2().getPessoa().setNome(dadosSQL.getString("pessoa2.nome"));
				obj.getFuncionarioTestemunhaAssinatura2().getPessoa().setCPF(dadosSQL.getString("pessoa2.cpf"));
				obj.getFuncionarioTestemunhaAssinatura2().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("pessoa2.tipoAssinaturaDocumentoEnum")));
			}
			if(Uteis.isAtributoPreenchido(dadosSQL.getInt("ceof.codigo"))) {
				ConfiguracaoEstagioObrigatorioFuncionarioVO ceof = buscarConfiguracaoEstagioObrigatorioFuncionarioVO(dadosSQL.getInt("ceof.codigo"), obj);
				if(!Uteis.isAtributoPreenchido(ceof)) {
					ceof.setNovoObj(Boolean.FALSE);
					ceof.setCodigo(dadosSQL.getInt("ceof.codigo"));
					ceof.setConfiguracaoEstagioObrigatorioVO(obj);
					ceof.getFuncionarioVO().setCodigo(dadosSQL.getInt("ceof.funcionario"));
					ceof.getFuncionarioVO().setMatricula(dadosSQL.getString("ceoFun.matricula"));
					ceof.getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("ceopessoa.codigo"));
					ceof.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("ceopessoa.nome"));
					ceof.getFuncionarioVO().getPessoa().setCPF(dadosSQL.getString("ceopessoa.cpf"));
					ceof.getFuncionarioVO().getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("ceopessoa.tipoAssinaturaDocumentoEnum")));
				}
				
				PessoaEmailInstitucionalVO pei = new PessoaEmailInstitucionalVO();
				pei.setCodigo((dadosSQL.getInt("ceopessoaemailinstitucional.codigo")));
				pei.setEmail(dadosSQL.getString("ceopessoaemailinstitucional.email"));
				if (Uteis.isAtributoPreenchido(dadosSQL.getString("ceopessoaemailinstitucional.statusAtivoInativoEnum"))) {
					pei.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("ceopessoaemailinstitucional.statusAtivoInativoEnum")));
				}
				ceof.getFuncionarioVO().getPessoa().getListaPessoaEmailInstitucionalVO().add(pei);
				adicionarConfiguracaoEstagioObrigatorioFuncionarioVO(ceof, obj);
			}
		}		
		return obj;

	}
	
	private ConfiguracaoEstagioObrigatorioFuncionarioVO buscarConfiguracaoEstagioObrigatorioFuncionarioVO(Integer codigo, ConfiguracaoEstagioObrigatorioVO obj) {
		return obj.getListaConfiguracaoEstagioObrigatorioFuncionarioVO()
				.stream()
				.filter(objsExistente -> objsExistente.getCodigo().equals(codigo))
				.findFirst()
				.orElse(new ConfiguracaoEstagioObrigatorioFuncionarioVO());
		
	}
	
	private void adicionarConfiguracaoEstagioObrigatorioFuncionarioVO(ConfiguracaoEstagioObrigatorioFuncionarioVO ceof, ConfiguracaoEstagioObrigatorioVO obj) {
		int index = 0;
		for (ConfiguracaoEstagioObrigatorioFuncionarioVO objsExistente : obj.getListaConfiguracaoEstagioObrigatorioFuncionarioVO()) {
			if (objsExistente.getCodigo().equals(ceof.getCodigo())) {
				obj.getListaConfiguracaoEstagioObrigatorioFuncionarioVO().set(index, ceof);
				return;
			}
			index++;
		}
		obj.getListaConfiguracaoEstagioObrigatorioFuncionarioVO().add(ceof);
	}

}
