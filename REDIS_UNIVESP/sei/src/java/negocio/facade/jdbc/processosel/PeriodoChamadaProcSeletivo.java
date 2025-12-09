package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

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

import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.PeriodoChamadaProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.ProvisaoCusto;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.ItemProcSeletivoDataProvaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PeriodoChamadaProcSeletivoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>PeriodoChamadaProcSeletivoVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see PeriodoChamadaProcSeletivoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class PeriodoChamadaProcSeletivo extends ControleAcesso implements PeriodoChamadaProcSeletivoInterfaceFacade {

	protected static String idEntidade;

	public PeriodoChamadaProcSeletivo() throws Exception {
		super();
		setIdEntidade("PeriodoChamadaProcSeletivo");
	}


	public static String getIdEntidade() {
		return ItemProcSeletivoDataProva.idEntidade;
	}

	
	public void setIdEntidade(String idEntidade) {
		ItemProcSeletivoDataProva.idEntidade = idEntidade;
	}
	
	public PeriodoChamadaProcSeletivoVO novo() throws Exception {
		PeriodoChamadaProcSeletivo.incluir(getIdEntidade());
		PeriodoChamadaProcSeletivoVO obj = new PeriodoChamadaProcSeletivoVO();
		return obj;
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPeriodoChamadaProcSeletivo(Integer procSeletivo, List<PeriodoChamadaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		try {
			for (PeriodoChamadaProcSeletivoVO obj : objetos) {				
				obj.getProcSeletivoVO().setCodigo(procSeletivo);
				incluir(obj, usuarioVO);
			}
		} catch (Exception e) {
			for (PeriodoChamadaProcSeletivoVO obj : objetos) {
				obj.setNovoObj(true);
				obj.setCodigo(0);
			}
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final PeriodoChamadaProcSeletivoVO obj,UsuarioVO usuario) {
		try {
			 
			incluir(obj, "PeriodoChamadaProcSeletivo", new AtributoPersistencia()
					.add("nrChamada", obj.getNrChamada())					
					.add("periodoInicialChamada", obj.getPeriodoInicialChamada())
					.add("periodoFinalChamada", obj.getPeriodoFinalChamada())
					.add("periodoInicialUploadDocumentoIndeferido", obj.getPeriodoInicialUploadDocumentoIndeferido())
					.add("periodoFinalUploadDocumentoIndeferido", obj.getPeriodoFinalUploadDocumentoIndeferido())
					.add("dataenviomensagemativacaomatricula", obj.getDataEnvioMensagemAtivacaoMatricula())
					.add("procSeletivo", obj.getProcSeletivoVO()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final PeriodoChamadaProcSeletivoVO obj, UsuarioVO usuario) {
		try {
			alterar(obj, "PeriodoChamadaProcSeletivo", new AtributoPersistencia()
					.add("nrChamada", obj.getNrChamada())					
					.add("periodoInicialChamada", obj.getPeriodoInicialChamada())
					.add("periodoFinalChamada", obj.getPeriodoFinalChamada())
					.add("periodoInicialUploadDocumentoIndeferido", obj.getPeriodoInicialUploadDocumentoIndeferido())
					.add("periodoFinalUploadDocumentoIndeferido", obj.getPeriodoFinalUploadDocumentoIndeferido())
					.add("dataenviomensagemativacaomatricula", obj.getDataEnvioMensagemAtivacaoMatricula())
					.add("procSeletivo", obj.getProcSeletivoVO()),
					new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPeriodoChamadaProcSeletivo(Integer procSeletivo, List<PeriodoChamadaProcSeletivoVO> objetos, UsuarioVO usuarioVO) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(objetos, "periodoChamadaProcSeletivo", "procseletivo", procSeletivo, usuarioVO);
		persistir(objetos, usuarioVO);	
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<PeriodoChamadaProcSeletivoVO> lista,UsuarioVO usuarioVO) {		
		for (PeriodoChamadaProcSeletivoVO obj : lista) {			
			if (!Uteis.isAtributoPreenchido(obj)) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		
		}
	}	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(PeriodoChamadaProcSeletivoVO obj, UsuarioVO usuarioLogado) throws Exception {		
		excluir("PeriodoChamadaProcSeletivo", new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioLogado);
	}
	
	@Override
	public PeriodoChamadaProcSeletivoVO consultarPorCodigoProcessoSeletivoNumeroChamada(Integer procSeletivo,Integer nrChamada, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT PeriodoChamadaProcSeletivo.* FROM PeriodoChamadaProcSeletivo ");
		sb.append("INNER JOIN procSeletivo ON procSeletivo.codigo = PeriodoChamadaProcSeletivo.procSeletivo ");
		sb.append("WHERE procSeletivo.codigo = ? ");
		sb.append(" AND PeriodoChamadaProcSeletivo.nrChamada = ? ");
		sb.append(" ORDER BY PeriodoChamadaProcSeletivo.periodoInicialChamada ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(),procSeletivo.intValue() , nrChamada);
		if (!tabelaResultado.next()) {
			return new PeriodoChamadaProcSeletivoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados));

	}
	
	public  static List<PeriodoChamadaProcSeletivoVO> consultarPeriodoChamadaProcSeletivoPorCodigoProcessoSeletivo(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT PeriodoChamadaProcSeletivo.* FROM PeriodoChamadaProcSeletivo ");
		sb.append("INNER JOIN procSeletivo ON procSeletivo.codigo = PeriodoChamadaProcSeletivo.procSeletivo ");
		sb.append("WHERE procSeletivo.codigo = ? ").append(" ORDER BY PeriodoChamadaProcSeletivo.periodoInicialChamada ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(),procSeletivo.intValue());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}
	

	
	@Override
	public PeriodoChamadaProcSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM PeriodoChamadaProcSeletivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( PeriodoChamadaProcSeletivo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}	
	
	public static List<PeriodoChamadaProcSeletivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<PeriodoChamadaProcSeletivoVO> vetResultado = new ArrayList<PeriodoChamadaProcSeletivoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		tabelaResultado = null;
		return vetResultado;
	}
	
	public static PeriodoChamadaProcSeletivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		PeriodoChamadaProcSeletivoVO obj = new PeriodoChamadaProcSeletivoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNrChamada(dadosSQL.getInt("nrChamada"));
		obj.setPeriodoInicialChamada(dadosSQL.getTimestamp("periodoInicialChamada"));
		obj.setPeriodoFinalChamada(dadosSQL.getTimestamp("periodoFinalChamada"));
		obj.setPeriodoInicialUploadDocumentoIndeferido(dadosSQL.getTimestamp("periodoInicialUploadDocumentoIndeferido"));
		obj.setPeriodoFinalUploadDocumentoIndeferido(dadosSQL.getTimestamp("periodoFinalUploadDocumentoIndeferido"));
		obj.setDataEnvioMensagemAtivacaoMatricula(dadosSQL.getDate("dataenviomensagemativacaomatricula"));
		obj.getProcSeletivoVO().setCodigo(new Integer(dadosSQL.getInt("procSeletivo")));		
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	
	@Override
	public void adicionarPeriodoChamadaProcSeletivoVO(PeriodoChamadaProcSeletivoVO  obj, ProcSeletivoVO procSeletivoVO) throws Exception {
		        obj.setProcSeletivoVO(procSeletivoVO);
			    PeriodoChamadaProcSeletivoVO.validarDados(obj);
			    PeriodoChamadaProcSeletivoVO.validarDadosAdicionarObjLista(obj, procSeletivoVO.getPeriodoChamadaProcSeletivoVOs());
		        int index = 0;
		        Iterator<PeriodoChamadaProcSeletivoVO> i = procSeletivoVO.getPeriodoChamadaProcSeletivoVOs().iterator();
		        while (i.hasNext()) {
		        	PeriodoChamadaProcSeletivoVO objExistente = (PeriodoChamadaProcSeletivoVO) i.next();
		            if (objExistente.getNrChamada().equals(obj.getNrChamada())) {
		            	procSeletivoVO.getPeriodoChamadaProcSeletivoVOs().set(index, obj);
		                return;
		            }
		            index++;
		        }
		        procSeletivoVO.getPeriodoChamadaProcSeletivoVOs().add(obj);		        
    }

	@Override
	public void removerPeriodoChamadaProcSeletivoVO(PeriodoChamadaProcSeletivoVO obj, ProcSeletivoVO procSeletivoVO) throws Exception {
		int x = 0;
		for (PeriodoChamadaProcSeletivoVO objExist : procSeletivoVO.getPeriodoChamadaProcSeletivoVOs()) {
			if (objExist.getNrChamada().equals(obj.getNrChamada())) {
				procSeletivoVO.getPeriodoChamadaProcSeletivoVOs().remove(x);
				return;
			}
			x++;
		}
		
	}
	
	@Override
	public List<SelectItem>  getConsultaComboNrChamadaProcSeletivo(){
		List<SelectItem> tipoConsultaComboNrChamadaProcSeletivo = new ArrayList<SelectItem>(0);
		tipoConsultaComboNrChamadaProcSeletivo.add(new SelectItem("", ""));
		for(int i = 1 ; i <=100; i++) {
			tipoConsultaComboNrChamadaProcSeletivo.add(new SelectItem(i, i+"ª Chamada"));
		}
		return tipoConsultaComboNrChamadaProcSeletivo;
	}
	
}
