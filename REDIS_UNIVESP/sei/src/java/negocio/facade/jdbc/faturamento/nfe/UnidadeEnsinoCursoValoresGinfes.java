package negocio.facade.jdbc.faturamento.nfe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class UnidadeEnsinoCursoValoresGinfes extends ControleAcesso implements UnidadeEnsinoCursoValoresGinfesInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6733147500633772981L;
	public static final String idEntidade = "ValoresCursoGinfes";
	
	public UnidadeEnsinoCursoValoresGinfes() {
		super();
	}

	public void validarDados(UnidadeEnsinoCursoValoresGinfesVO obj) {		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCursoVO()), "O campo Unidade Ensino Curso não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCodigoCursoGinfes()), "O campo Código Curso Ginfes não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorMensalidade()), "O campo Valor Mensalidade não foi informado.");
		Uteis.checkState(!obj.getUnidadeEnsinoCursoVO().getCurso().getIntegral() && !Uteis.isAtributoPreenchido(obj.getAnoIngresso()), "O campo Ano Ingresso não foi informado.");
		Uteis.checkState(obj.getUnidadeEnsinoCursoVO().getCurso().getSemestral() && !Uteis.isAtributoPreenchido(obj.getSemestreIngresso()), "O campo Semestre Ingresso não foi informado.");
		Uteis.checkState(!obj.getUnidadeEnsinoCursoVO().getCurso().getIntegral() && !Uteis.isAtributoPreenchido(obj.getAnoCompetenciaParcela()), "O campo Ano Competência Parcela não foi informado.");
		Uteis.checkState(obj.getUnidadeEnsinoCursoVO().getCurso().getSemestral() && !Uteis.isAtributoPreenchido(obj.getSemestreCompetenciaParcela()), "O campo Semestre Competência Parcela não foi informado.");		
		Uteis.checkState((Uteis.isAtributoPreenchido(obj.getAnoIngresso()) && obj.getAnoIngresso().toString().length() != 4 ), "O campo Ano Ingresso não esta no formato correto  YYYY.");
		Uteis.checkState((Uteis.isAtributoPreenchido(obj.getSemestreIngresso()) && obj.getSemestreIngresso() > 3), "O campo Semestre Ingresso não pode ter o valor maior que 3.");
		Uteis.checkState((Uteis.isAtributoPreenchido(obj.getAnoCompetenciaParcela()) && obj.getAnoCompetenciaParcela().toString().length() != 4 ), "O campo Ano Competência Parcela não esta no formato correto  YYYY.");
		Uteis.checkState((Uteis.isAtributoPreenchido(obj.getSemestreCompetenciaParcela()) && obj.getSemestreCompetenciaParcela() > 3), "O campo Semestre Competência Parcela não pode ter o valor maior que 3.");
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void reajustarValoresCursoGinfes(UnidadeEnsinoCursoVO obj, Integer anoCompetenciaAtual, Integer semestreCompetenciaAtual, BigDecimal percentualReajustePorCompetencia, String periodicidadeCurso, boolean reajustePorCompetenciaGeral, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(anoCompetenciaAtual), "O campo Ano Competência Atual não foi informado.");
		Uteis.checkState(periodicidadeCurso.equals("SE") && !Uteis.isAtributoPreenchido(semestreCompetenciaAtual), "O campo Semestre Competência Atual não foi informado.");
		Uteis.checkState((Uteis.isAtributoPreenchido(anoCompetenciaAtual) && anoCompetenciaAtual.toString().length() != 4 ), "O campo Ano Competência Atual não esta no formato correto  YYYY.");
		Uteis.checkState((Uteis.isAtributoPreenchido(semestreCompetenciaAtual) && semestreCompetenciaAtual > 3), "O campo Semestre Competência Atual não pode ter o valor maior que 3.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(percentualReajustePorCompetencia), "O campo Valor do Percentual Reajuste não foi informado.");
		Uteis.checkState(Uteis.isAtributoPreenchido(percentualReajustePorCompetencia) && percentualReajustePorCompetencia.compareTo(new BigDecimal("100.00")) > 0, "O campo Valor do Percentual Reajuste não pode ser maior que 100,00%.");
		if(!reajustePorCompetenciaGeral) {
			reajustarValoresCursoGinfesPorUnidadeEnsinoCurso(obj, anoCompetenciaAtual, semestreCompetenciaAtual, percentualReajustePorCompetencia, periodicidadeCurso, verificarAcesso, usuarioVO);
		}else {
			reajustarValoresCursoGinfesGeral(obj, anoCompetenciaAtual, semestreCompetenciaAtual, percentualReajustePorCompetencia, periodicidadeCurso, verificarAcesso, usuarioVO);
		}
		
	}

	private void reajustarValoresCursoGinfesPorUnidadeEnsinoCurso(UnidadeEnsinoCursoVO obj, Integer anoCompetenciaAtual, Integer semestreCompetenciaAtual, BigDecimal percentualReajustePorCompetencia, String periodicidadeCurso, boolean verificarAcesso, UsuarioVO usuarioVO) {
		List<UnidadeEnsinoCursoValoresGinfesVO> listaNovosUecvg = new ArrayList<>();
		for (UnidadeEnsinoCursoValoresGinfesVO uecvg : obj.getListaUnidadeEnsinoCursoValoresGinfes()) {
			if(uecvg.getAnoCompetenciaParcela().equals(anoCompetenciaAtual) &&
					((periodicidadeCurso.equals("SE") && uecvg.getSemestreCompetenciaParcela().equals(semestreCompetenciaAtual))
					|| (periodicidadeCurso.equals("AN") && uecvg.getSemestreCompetenciaParcela().equals(0)))
					) {
				UnidadeEnsinoCursoValoresGinfesVO novoUecvg  = new UnidadeEnsinoCursoValoresGinfesVO();
				novoUecvg.setUnidadeEnsinoCursoVO(uecvg.getUnidadeEnsinoCursoVO());
				novoUecvg.setCodigoCursoGinfes(uecvg.getCodigoCursoGinfes());					
				novoUecvg.setAnoIngresso(uecvg.getAnoIngresso());
				novoUecvg.setSemestreIngresso(uecvg.getSemestreIngresso());	
				novoUecvg.setNumeroPeriodoLetivoCompetenciaParcela(uecvg.getNumeroPeriodoLetivoCompetenciaParcela());	
				novoUecvg.setValorMensalidade(uecvg.getValorMensalidade().add(Uteis.porcentagemComBigDecimal(uecvg.getValorMensalidade(), percentualReajustePorCompetencia)));
				if(periodicidadeCurso.equals("AN")) {
					novoUecvg.setAnoCompetenciaParcela(anoCompetenciaAtual + 1);
				}else if(periodicidadeCurso.equals("SE") && semestreCompetenciaAtual == 2) {
					novoUecvg.setAnoCompetenciaParcela(anoCompetenciaAtual + 1);
					novoUecvg.setSemestreCompetenciaParcela(1);
				}else if(periodicidadeCurso.equals("SE") && semestreCompetenciaAtual == 1) {
					novoUecvg.setAnoCompetenciaParcela(anoCompetenciaAtual);
					novoUecvg.setSemestreCompetenciaParcela(2);
				}
				incluir(novoUecvg, verificarAcesso, usuarioVO);
				listaNovosUecvg.add(novoUecvg);
			}
		}
		obj.getListaUnidadeEnsinoCursoValoresGinfes().addAll(listaNovosUecvg);
	}	
	
	private void reajustarValoresCursoGinfesGeral(UnidadeEnsinoCursoVO obj, Integer anoCompetenciaAtual, Integer semestreCompetenciaAtual, BigDecimal percentualReajustePorCompetencia, String periodicidadeCurso, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder(" INSERT INTO unidadeensinocursovaloresginfes ");
		sql.append(" (unidadeensinocurso, codigocursoginfes, anoingresso, semestreingresso, anocompetenciaparcela, semestrecompetenciaparcela, numeroPeriodoLetivoCompetenciaParcela, valormensalidade) ");
		sql.append(" (select");
		sql.append(" uecvf.unidadeensinocurso, uecvf.codigocursoginfes, uecvf.anoingresso, uecvf.semestreingresso,");
		sql.append(" case when (curso.periodicidade = 'AN' or (curso.periodicidade = 'SE' and uecvf.semestrecompetenciaparcela = 2)) then uecvf.anocompetenciaparcela + 1 else uecvf.anocompetenciaparcela end as anocompetenciaparcela,");
		sql.append(" case when curso.periodicidade = 'AN' then 0 when curso.periodicidade = 'SE' and uecvf.semestrecompetenciaparcela = 2 then 1 else 2 end as semestrecompetenciaparcela,");
		sql.append(" uecvf.numeroPeriodoLetivoCompetenciaParcela, ");
		sql.append(" (uecvf.valormensalidade + ((uecvf.valormensalidade * ").append(percentualReajustePorCompetencia).append(" )/100))::numeric(20,2) as valormensalidade");
		sql.append(" from unidadeensino");
		sql.append(" inner join unidadeensinocurso  on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" inner join curso on curso.codigo = unidadeensinocurso.curso ");
		sql.append(" inner join turno on turno.codigo = unidadeensinocurso.turno ");
		sql.append(" inner join unidadeensinocursovaloresginfes uecvf on unidadeensinocurso.codigo = uecvf.unidadeensinocurso ");
		sql.append(" where  unidadeensino.codigo = ").append(obj.getUnidadeEnsino());
		sql.append(" and  uecvf.anocompetenciaparcela = ").append(anoCompetenciaAtual);
		if (Uteis.isAtributoPreenchido(obj.getCurso().getNome())) {
			sql.append(" and lower(curso.nome) like('").append(obj.getCurso().getNome().toLowerCase()).append("%')");
		}
		if (Uteis.isAtributoPreenchido(obj.getTurno().getNome())) {
			sql.append(" and lower(turno.nome) like('").append(obj.getTurno().getNome().toLowerCase()).append("%')");
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigoCursoUnidadeEnsinoGinfes())) {
			sql.append(" and unidadeEnsinoCurso.codigoCursoUnidadeEnsinoGinfes = ").append(obj.getCodigoCursoUnidadeEnsinoGinfes());
		}
		if (Uteis.isAtributoPreenchido(obj.getValorMensalidade())) {
			sql.append(" and unidadeEnsinoCurso.valorMensalidade = ").append(obj.getValorMensalidade());
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigoItemListaServico())) {
			sql.append(" and unidadeEnsinoCurso.codigoItemListaServico like('").append(obj.getCodigoItemListaServico()).append("%')");
		}	
		if(periodicidadeCurso.equals("SE")) {
			sql.append("and curso.periodicidade = 'SE' and uecvf.semestrecompetenciaparcela = ").append(semestreCompetenciaAtual);	
		}else if(periodicidadeCurso.equals("AN")) {
			sql.append("and curso.periodicidade = 'AN' and uecvf.semestrecompetenciaparcela = 0 ");
		}
		sql.append(" and not exists ( ");
		sql.append("  select unidadeensinocursovaloresginfes.codigo from unidadeensinocursovaloresginfes  ");
		sql.append("  where unidadeensinocursovaloresginfes.codigocursoginfes = uecvf.codigocursoginfes ");
		sql.append("  and unidadeensinocursovaloresginfes.anoingresso = uecvf.anoingresso ");
		sql.append("  and unidadeensinocursovaloresginfes.semestreingresso = uecvf.semestreingresso ");
		sql.append("  and unidadeensinocursovaloresginfes.anocompetenciaparcela = case when curso.periodicidade = 'AN' or (curso.periodicidade = 'SE' and uecvf.semestrecompetenciaparcela = 2) then uecvf.anocompetenciaparcela + 1 else uecvf.anocompetenciaparcela end ");
		sql.append("  and unidadeensinocursovaloresginfes.semestrecompetenciaparcela = case when curso.periodicidade = 'AN' then 0 when curso.periodicidade = 'SE' and uecvf.semestrecompetenciaparcela = 1 then 2 else 1 end ");
		sql.append(")");
		sql.append(") ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().execute(sql.toString());
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(UnidadeEnsinoCursoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getUnidadeEnsinoCursoFacade().alterarValoresCursoGinfes(obj, verificarAcesso, usuarioVO);
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaUnidadeEnsinoCursoValoresGinfes(), "unidadeEnsinoCursoValoresGinfes", "unidadeEnsinoCurso", obj.getCodigo(), usuarioVO);
		for (UnidadeEnsinoCursoValoresGinfesVO uecvg : obj.getListaUnidadeEnsinoCursoValoresGinfes()) {
			if (uecvg.getCodigo() == 0) {
				incluir(uecvg, verificarAcesso, usuarioVO);
			} else {
				alterar(uecvg, verificarAcesso, usuarioVO);
			}
		}
	}	

	
	
	private void incluir(final UnidadeEnsinoCursoValoresGinfesVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			UnidadeEnsinoCursoValoresGinfes.incluir(getIdEntidade(), verificarAcesso, usuario);
			validarDados(obj);
			incluir(obj, "unidadeEnsinoCursoValoresGinfes", new AtributoPersistencia()
					.add("unidadeEnsinoCurso", obj.getUnidadeEnsinoCursoVO())
					.add("codigoCursoGinfes", obj.getCodigoCursoGinfes())
					.add("anoIngresso", obj.getAnoIngresso())
					.add("semestreIngresso", obj.getSemestreIngresso())
					.add("anoCompetenciaParcela", obj.getAnoCompetenciaParcela())
					.add("semestreCompetenciaParcela", obj.getSemestreCompetenciaParcela())
					.add("numeroPeriodoLetivoCompetenciaParcela", obj.getNumeroPeriodoLetivoCompetenciaParcela())
					.add("valorMensalidade", obj.getValorMensalidade())
					,usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	
	
	private void alterar(final UnidadeEnsinoCursoValoresGinfesVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			UnidadeEnsinoCursoValoresGinfes.alterar(getIdEntidade(), verificarAcesso, usuario);
			validarDados(obj);
			alterar(obj, "unidadeEnsinoCursoValoresGinfes", new AtributoPersistencia()
					.add("codigoCursoGinfes", obj.getCodigoCursoGinfes())
					.add("anoIngresso", obj.getAnoIngresso())
					.add("semestreIngresso", obj.getSemestreIngresso())
					.add("anoCompetenciaParcela", obj.getAnoCompetenciaParcela())
					.add("semestreCompetenciaParcela", obj.getSemestreCompetenciaParcela())
					.add("numeroPeriodoLetivoCompetenciaParcela", obj.getNumeroPeriodoLetivoCompetenciaParcela())
					.add("valorMensalidade", obj.getValorMensalidade())
					, new AtributoPersistencia().add("codigo", obj.getCodigo())
					, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoCursoValoresGinfesVO obj, boolean verificarAcesso, UsuarioVO usuario)  {
		try {
			UnidadeEnsinoCursoValoresGinfes.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM unidadeEnsinoCursoValoresGinfes WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarUnidadeEnsinoCursoValoresGinfes(List<UnidadeEnsinoCursoValoresGinfesVO> listaUnidadeEnsinoCursoValoresGinfesVOs, UnidadeEnsinoCursoValoresGinfesVO obj, UsuarioVO usuario) {
		try {
			validarDados(obj);
			int index = 0;
			for (UnidadeEnsinoCursoValoresGinfesVO objsExistente : listaUnidadeEnsinoCursoValoresGinfesVOs) {
				if (objsExistente.equalsCampoSelecaoLista(obj)) {
					listaUnidadeEnsinoCursoValoresGinfesVOs.set(index, obj);
					return;
				}
				index++;
			}
			listaUnidadeEnsinoCursoValoresGinfesVOs.add(obj);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerUnidadeEnsinoCursoValoresGinfes(List<UnidadeEnsinoCursoValoresGinfesVO> listaUnidadeEnsinoCursoValoresGinfesVOs, UnidadeEnsinoCursoValoresGinfesVO obj, UsuarioVO usuario) {
		try {
			final Iterator<UnidadeEnsinoCursoValoresGinfesVO> i = listaUnidadeEnsinoCursoValoresGinfesVOs.iterator();
	        while (i.hasNext()) {
	        	UnidadeEnsinoCursoValoresGinfesVO objExistente = i.next();
	            if (objExistente.equalsCampoSelecaoLista(obj)) {
	                i.remove();
	                return;
	            }
	        }
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT uecvg.codigo as \"uecvg.codigo\",  ");
		sql.append(" uecvg.codigoCursoGinfes as \"uecvg.codigoCursoGinfes\", ");
		sql.append(" uecvg.anoIngresso as \"uecvg.anoIngresso\", ");
		sql.append(" uecvg.semestreIngresso as \"uecvg.semestreIngresso\", ");
		sql.append(" uecvg.anoCompetenciaParcela as \"uecvg.anoCompetenciaParcela\", ");
		sql.append(" uecvg.semestreCompetenciaParcela as \"uecvg.semestreCompetenciaParcela\", ");
		sql.append(" uecvg.numeroPeriodoLetivoCompetenciaParcela as \"uecvg.numeroPeriodoLetivoCompetenciaParcela\", ");
		sql.append(" uecvg.valorMensalidade as \"uecvg.valorMensalidade\", ");

		sql.append(" unidadeEnsinoCurso.codigo as \"unidadeEnsinoCurso.codigo\",  ");

		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", curso.periodicidade as \"curso.periodicidade\"   ");

		sql.append(" FROM unidadeEnsinoCursoValoresGinfes as uecvg ");
		sql.append(" inner join unidadeEnsinoCurso on unidadeEnsinoCurso.codigo = uecvg.unidadeEnsinoCurso");
		sql.append(" inner join curso on curso.codigo = unidadeEnsinoCurso.curso");
		return sql;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesInterfaceFacade#consultaRapidaPorNotaFiscalEntrada(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<UnidadeEnsinoCursoValoresGinfesVO> consultaRapidaPorUnidadeEnsinoCurso(Integer unidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE uecvg.unidadeEnsinoCurso = ").append(unidadeEnsinoCurso).append(" ");
			sqlStr.append(" ORDER BY uecvg.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.UnidadeEnsinoCursoValoresGinfesInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public UnidadeEnsinoCursoValoresGinfesVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE uecvg.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( UnidadeEnsinoCursoValoresGinfesVO ).");
			}
			UnidadeEnsinoCursoValoresGinfesVO obj = new UnidadeEnsinoCursoValoresGinfesVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<UnidadeEnsinoCursoValoresGinfesVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<UnidadeEnsinoCursoValoresGinfesVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			UnidadeEnsinoCursoValoresGinfesVO obj = new UnidadeEnsinoCursoValoresGinfesVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(UnidadeEnsinoCursoValoresGinfesVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("uecvg.codigo"));
		obj.setCodigoCursoGinfes(dadosSQL.getInt("uecvg.codigoCursoGinfes"));
		obj.setAnoIngresso(dadosSQL.getInt("uecvg.anoIngresso"));
		obj.setSemestreIngresso(dadosSQL.getInt("uecvg.semestreIngresso"));
		obj.setAnoCompetenciaParcela(dadosSQL.getInt("uecvg.anoCompetenciaParcela"));
		obj.setSemestreCompetenciaParcela(dadosSQL.getInt("uecvg.semestreCompetenciaParcela"));
		obj.setNumeroPeriodoLetivoCompetenciaParcela(dadosSQL.getInt("uecvg.numeroPeriodoLetivoCompetenciaParcela"));
		obj.setValorMensalidade(dadosSQL.getBigDecimal("uecvg.valorMensalidade"));
		obj.getUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("unidadeEnsinoCurso.codigo"));
		obj.getUnidadeEnsinoCursoVO().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getUnidadeEnsinoCursoVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getUnidadeEnsinoCursoVO().getCurso().setPeriodicidade(dadosSQL.getString("curso.periodicidade"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return UnidadeEnsinoCursoValoresGinfes.idEntidade;
	}

}
