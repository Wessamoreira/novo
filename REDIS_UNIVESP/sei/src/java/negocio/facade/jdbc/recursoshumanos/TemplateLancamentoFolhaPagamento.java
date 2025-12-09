package negocio.facade.jdbc.recursoshumanos;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.PrestacaoConta;
import negocio.interfaces.recursoshumanos.TemplateLancamentoFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>GrupoLancamentoFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>GrupoLancamentoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class TemplateLancamentoFolhaPagamento extends ControleAcesso implements TemplateLancamentoFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public TemplateLancamentoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("TemplateLancamentoFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(TemplateLancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(TemplateLancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
			
		PrestacaoConta.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		
		incluir(obj, "TemplateLancamentoFolhaPagamento", new AtributoPersistencia()
				.add("lancarEventosFuncionario", obj.getLancarEventosFuncionario())
				.add("lancarEmprestimos", obj.getLancarEmprestimos())
				.add("lancarValeTransporte", obj.getLancarValeTransporte())
				.add("lancarSalarioMaternidade", obj.getLancarSalarioMaternidade())
				.add("funcionariocargo", obj.getFuncionarioCargoVO())
				.add("formaContratacaoFuncionario", obj.getFormaContratacaoFuncionario())
				.add("tipoRecebimento", obj.getTipoRecebimento())
				.add("situacaoFuncionario", obj.getSituacaoFuncionario())
				.add("tipoTemplateFolhaPagamento", obj.getTipoTemplateFolhaPagamento())
				.add("secaofolhapagamento", obj.getSecaoFolhaPagamento())
				.add("lancarEventosGrupo", obj.getLancarEventosGrupo())
				.add("lancarSalarioComposto", obj.getLancarSalarioComposto())
				.add("lancarFerias", obj.getLancarFerias())
				.add("lancarAdiantamentoFerias", obj.getLancarAdiantamentoFerias())
				.add("dataHora", obj.getDataHora())
				.add("lancarEventosFolhaNormal", obj.getLancarEventosFolhaNormal())
				.add("lancar13Parcela1", obj.getLancar13Parcela1())
				.add("lancar13Parcela2", obj.getLancar13Parcela2())
				.add("lancarRescisao", obj.getLancarRescisao())
				.add("lancarPensao", obj.getLancarPensao())
				, usuarioVO);
				
				obj.setNovoObj(Boolean.TRUE);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(TemplateLancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
			PrestacaoConta.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			
			alterar(obj, "TemplateLancamentoFolhaPagamento", new AtributoPersistencia()
					.add("lancarEventosFuncionario", obj.getLancarEventosFuncionario())
					.add("lancarEmprestimos", obj.getLancarEmprestimos())
					.add("lancarValeTransporte", obj.getLancarValeTransporte())
					.add("lancarSalarioMaternidade", obj.getLancarSalarioMaternidade())
					.add("funcionariocargo", obj.getFuncionarioCargoVO())
					.add("formaContratacaoFuncionario", obj.getFormaContratacaoFuncionario())
					.add("tipoRecebimento", obj.getTipoRecebimento())
					.add("situacaoFuncionario", obj.getSituacaoFuncionario())
					.add("tipoTemplateFolhaPagamento", obj.getTipoTemplateFolhaPagamento())
					.add("secaofolhapagamento", obj.getSecaoFolhaPagamento())
					.add("lancarEventosGrupo", obj.getLancarEventosGrupo())
					.add("lancarSalarioComposto", obj.getLancarSalarioComposto())
					.add("lancarFerias", obj.getLancarFerias())
					.add("lancarAdiantamentoFerias", obj.getLancarAdiantamentoFerias())
					.add("dataHora", obj.getDataHora())
					.add("lancarEventosFolhaNormal", obj.getLancarEventosFolhaNormal())
					.add("lancar13Parcela1", obj.getLancar13Parcela1())
					.add("lancar13Parcela2", obj.getLancar13Parcela2())
					.add("lancarRescisao", obj.getLancarRescisao())
					.add("lancarPensao", obj.getLancarPensao())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
					
					obj.setNovoObj(Boolean.FALSE);
					
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(TemplateLancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "DELETE FROM TemplateLancamentoFolhaPagamento WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public TemplateLancamentoFolhaPagamentoVO consultarTemplatePorLancamentoFolha(Integer codigo) throws Exception {
		String sql = " SELECT * FROM templatelancamentofolhapagamento "
				+ " INNER JOIN lancamentofolhapagamento ON templatelancamentofolhapagamento.codigo = lancamentofolhapagamento.templatelancamentofolhapagamento"
				+ " WHERE lancamentofolhapagamento.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
        }
		return null;
	}

	private TemplateLancamentoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados ) throws Exception {
		
		TemplateLancamentoFolhaPagamentoVO obj = new TemplateLancamentoFolhaPagamentoVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setFormaContratacaoFuncionario(tabelaResultado.getString("formacontratacaofuncionario"));
		obj.setTipoRecebimento(tabelaResultado.getString("tiporecebimento"));
		obj.setSituacaoFuncionario(tabelaResultado.getString("situacaofuncionario"));
		
		obj.setLancarEventosFuncionario(tabelaResultado.getBoolean("lancareventosfuncionario"));
		obj.setLancarEmprestimos(tabelaResultado.getBoolean("lancaremprestimos"));
		obj.setLancarValeTransporte(tabelaResultado.getBoolean("lancarvaletransporte"));
		obj.setLancarSalarioMaternidade(tabelaResultado.getBoolean("lancarsalariomaternidade"));
		
		obj.setLancarEventosGrupo(tabelaResultado.getBoolean("lancarEventosGrupo"));
		obj.setLancarSalarioComposto(tabelaResultado.getBoolean("lancarSalarioComposto"));
		
		obj.setLancarFerias(tabelaResultado.getBoolean("lancarFerias"));
		obj.setLancarAdiantamentoFerias(tabelaResultado.getBoolean("lancarAdiantamentoFerias"));

		obj.setDataHora(tabelaResultado.getDate("dataHora"));
		obj.setLancarEventosFolhaNormal(tabelaResultado.getBoolean("lancarEventosFolhaNormal"));

		obj.setLancar13Parcela1(tabelaResultado.getBoolean("lancar13Parcela1"));
		obj.setLancar13Parcela2(tabelaResultado.getBoolean("lancar13Parcela2"));

		obj.setLancarRescisao(tabelaResultado.getBoolean("lancarRescisao"));

		obj.setLancarPensao(tabelaResultado.getBoolean("lancarPensao"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariocargo"))) {
			obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("funcionariocargo"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
				
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipotemplateFolhaPagamento"))) {
			obj.setTipoTemplateFolhaPagamento(TipoTemplateFolhaPagamentoEnum.valueOf(tabelaResultado.getString("tipotemplateFolhaPagamento")));
		}

		if(Uteis.NIVELMONTARDADOS_DADOSMINIMOS == nivelMontarDados)
			return obj;
		
		obj.setSecaoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("secaofolhapagamento"), SecaoFolhaPagamentoVO.class, p -> getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		
		if(Uteis.NIVELMONTARDADOS_DADOSBASICOS == nivelMontarDados)
			return obj;
		
		
		if(Uteis.NIVELMONTARDADOS_TODOS == nivelMontarDados)
			obj.setFuncionarioCargoVO(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null)));
			
		return obj;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		TemplateLancamentoFolhaPagamento.idEntidade = idEntidade;
	}

	@Override
	public String getFiltrosDoTemplate(TemplateLancamentoFolhaPagamentoVO obj) {
		
		StringBuilder sql = new StringBuilder();
		
		if (Uteis.isAtributoPreenchido(obj.getFuncionarioCargoVO().getCodigo())) {
        	sql.append(" AND fc.codigo = ").append(obj.getFuncionarioCargoVO().getCodigo());
        } else {
        	
        	if(Uteis.isAtributoPreenchido(obj.getSecaoFolhaPagamento())) {
        		sql.append(" AND fc.secaoFolhaPagamento = ").append(obj.getSecaoFolhaPagamento().getCodigo());
        	}
        	
        	sql.append(UteisTexto.montarStringDeFiltroPorEnumerado(FormaContratacaoFuncionarioEnum.values(), obj.getFormaContratacaoFuncionario().split(";"), "fc.formacontratacao", Boolean.TRUE));
        	sql.append(UteisTexto.montarStringDeFiltroPorEnumerado(TipoRecebimentoEnum.values(), obj.getTipoRecebimento().split(";"), "fc.tiporecebimento", Boolean.TRUE));
    		sql.append(UteisTexto.montarStringDeFiltroPorEnumerado(SituacaoFuncionarioEnum.values(), obj.getSituacaoFuncionario().split(";"), "fc.situacaoFuncionario", Boolean.TRUE));        	
        }
		
		return sql.toString();
	}
	
	@Override
	public TemplateLancamentoFolhaPagamentoVO consultarPorChavePrimaria(Integer id, int nivelMontarDados) throws Exception {
		
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), (int) id);
        
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return montarDados(tabelaResultado, nivelMontarDados);
	}

	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder(" select * from templatelancamentofolhapagamento ");
		return sql.toString();
	}
	
	
}