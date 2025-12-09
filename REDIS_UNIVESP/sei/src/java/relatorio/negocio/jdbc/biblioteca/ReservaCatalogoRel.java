package relatorio.negocio.jdbc.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.biblioteca.ReservaCatalogoRelVO;
import relatorio.negocio.interfaces.biblioteca.ReservaCatalogoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ReservaCatalogoRel extends SuperRelatorio implements ReservaCatalogoRelInterfaceFacade {

	public List criarObjeto(Integer catalogo, Integer biblioteca, Date dataInicio, Date dataFim, Boolean prazoEncerrado, Boolean dentroPrazo, Boolean aguardandoExemplar, Boolean reservaConcluida, Boolean reservaCancelada) throws Exception {
		List<ReservaCatalogoRelVO> listaExemplaresRelVO = new ArrayList<ReservaCatalogoRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(catalogo, biblioteca, dataInicio, dataFim, prazoEncerrado, dentroPrazo, aguardandoExemplar, reservaConcluida, reservaCancelada);
		while (dadosSQL.next()) {
			listaExemplaresRelVO.add(montarDados(dadosSQL));
		}
		return listaExemplaresRelVO;
	}

	public List criarObjetoAnalitico(Integer catalogo, Integer biblioteca, Date dataInicio, Date dataFim, Boolean prazoEncerrado, Boolean dentroPrazo, Boolean aguardandoExemplar, Boolean reservaConcluida, Boolean reservaCancelada, UsuarioVO usuario) throws Exception {
		List<ReservaCatalogoRelVO> listaExemplaresRelVO = new ArrayList<ReservaCatalogoRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizadaAnalitico(catalogo, biblioteca, dataInicio, dataFim, prazoEncerrado, dentroPrazo, aguardandoExemplar, reservaConcluida, reservaCancelada);
		while (dadosSQL.next()) {
			listaExemplaresRelVO.add(montarDadosAnalitico(dadosSQL, usuario));
		}
		return listaExemplaresRelVO;
	}

	private SqlRowSet executarConsultaParametrizadaAnalitico(Integer catalogo, Integer biblioteca, Date dataInicio, Date dataFim, Boolean prazoEncerrado, Boolean dentroPrazo, Boolean aguardandoExemplar, Boolean reservaConcluida, Boolean reservaCancelada) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT reserva.datareserva, reserva.dataterminoreserva, reserva.pessoa, reserva.catalogo, catalogo.titulo, catalogo.edicao, catalogo.classificacaoBibliografica, ");
		sqlStr.append(" catalogo.cutterpha, pessoa.nome as solicitante, ");
		sqlStr.append(" case when dataterminoreserva is not null and dataterminoreserva < current_timestamp and emprestimo is null and situacao not in ('FI', 'CA', 'EM') then 'Prazo Encerrado' else ");
		sqlStr.append(" case when dataterminoreserva is not null and dataterminoreserva > current_timestamp and emprestimo is null and situacao not in ('FI', 'CA', 'EM') then 'Dentro do Prazo' else ");
		sqlStr.append(" case when dataterminoreserva is null and emprestimo is null and situacao not in ('FI', 'CA', 'EM') then 'Aguardando Exemplar' else ");
		sqlStr.append(" case when emprestimo is not null and situacao in ('EM') then 'Concluída' else ");
		sqlStr.append(" case when emprestimo is null and (situacao in ('CA') or situacao is null)  then 'Cancelada' else ");
		sqlStr.append(" case when (situacao in ('FI')) then 'Finalizado' end end end end end end as situacaoReserva, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.biblioteca = reserva.biblioteca and ex.catalogo = reserva.catalogo and ex.situacaoatual = 'DI' and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true and EXTRACT(DOW FROM current_date) in (0, 5, 6)))) as nrexemplaresdisponivel, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.biblioteca = reserva.biblioteca and ex.catalogo = reserva.catalogo and ex.situacaoatual in ('DI', 'CO', 'EM')) as nrexemplaresemprestimo, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.biblioteca = reserva.biblioteca and ex.catalogo = reserva.catalogo and ex.situacaoatual = 'EM') as nrexemplaresemprestado, ");
		sqlStr.append(" (select count(ex.codigo) from exemplar ex where ex.biblioteca = reserva.biblioteca and ex.catalogo = reserva.catalogo and ex.paraconsulta = true) as nrexemplaresparaconsulta, ");
		sqlStr.append(" (select count(r.codigo) from reserva r where r.biblioteca = reserva.biblioteca and r.catalogo = reserva.catalogo and (r.dataterminoreserva is not null AND r.dataterminoreserva > current_timestamp AND r.emprestimo is null and r.situacao != 'CA')) as nrexemplaresreservados ");
		sqlStr.append(" FROM reserva ");
		sqlStr.append(" inner join catalogo on catalogo.codigo = reserva.catalogo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = reserva.pessoa ");
		sqlStr.append(" WHERE reserva.biblioteca = ").append(biblioteca);		
		if (catalogo != null && !catalogo.equals(0)) {
			sqlStr.append(" AND reserva.catalogo = ").append(catalogo);
		}
		if (prazoEncerrado || dentroPrazo || aguardandoExemplar || reservaConcluida || reservaCancelada) {
			sqlStr.append(" AND ( ");
			String or = "";
			if (prazoEncerrado) {
				sqlStr.append(" (dataterminoreserva is not null AND dataterminoreserva < current_timestamp AND emprestimo is null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (dentroPrazo) {
				sqlStr.append(or).append(" (dataterminoreserva is not null AND dataterminoreserva > current_timestamp AND emprestimo is null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (aguardandoExemplar) {
				sqlStr.append(or).append(" (dataterminoreserva is null AND emprestimo is null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (reservaConcluida) {
				sqlStr.append(or).append(" (emprestimo is not null and reserva.situacao != 'CA') ");				
				or = " or  ";
			}
			if (reservaCancelada) {
				sqlStr.append(or).append(" (reserva.situacao = 'CA') ");				
			}
			sqlStr.append(" ) ");
		}

		sqlStr.append(" AND reserva.datareserva >=  '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sqlStr.append(" AND reserva.datareserva <=  '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sqlStr.append(" ORDER BY catalogo.titulo, catalogo.edicao, reserva.catalogo, reserva.datareserva, pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	private ReservaCatalogoRelVO montarDadosAnalitico(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		ReservaCatalogoRelVO obj = new ReservaCatalogoRelVO();
		obj.getPessoaVO().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getPessoaVO().setNome(dadosSQL.getString("solicitante"));
		obj.getCatalogoVO().setCodigo(dadosSQL.getInt("catalogo"));
		obj.getCatalogoVO().setTitulo(dadosSQL.getString("titulo"));
		obj.getCatalogoVO().setCutterPha(dadosSQL.getString("cutterPha"));
		obj.getCatalogoVO().setEdicao(dadosSQL.getString("edicao"));
		obj.getCatalogoVO().setClassificacaoBibliografica(dadosSQL.getString("classificacaoBibliografica"));
		obj.getCatalogoVO().setNrExemplaresDisponivel(dadosSQL.getInt("nrexemplaresdisponivel"));
		obj.getCatalogoVO().setNrExemplaresEmprestado(dadosSQL.getInt("nrexemplaresemprestado"));
		obj.getCatalogoVO().setNrExemplaresParaEmprestimo(dadosSQL.getInt("nrexemplaresemprestimo"));
		obj.getCatalogoVO().setNrExemplaresReservados(dadosSQL.getInt("nrexemplaresreservados"));
		obj.getCatalogoVO().setNrExemplaresParaConsulta(dadosSQL.getInt("nrexemplaresparaconsulta"));
		obj.getCatalogoVO().setAutorVOs(getFacadeFactory().getAutorFacade().consultarPorCodigoCatalogo(dadosSQL.getInt("catalogo"), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));		
		obj.setDataReserva(dadosSQL.getDate("datareserva"));
		obj.setDataTerminoReserva(dadosSQL.getDate("dataterminoreserva"));
		obj.setSituacaoReserva(dadosSQL.getString("situacaoReserva"));
		return obj;
	}

	private SqlRowSet executarConsultaParametrizada(Integer catalogo, Integer biblioteca, Date dataInicio, Date dataFim, Boolean prazoEncerrado, Boolean dentroPrazo, Boolean aguardandoExemplar, Boolean reservaConcluida, Boolean reservaCancelada) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT  catalogo.codigo, catalogo.titulo as catalogo, catalogo.edicao, editora.nome as editora, catalogo.anopublicacao, catalogo.cutterpha as cutterpha, count(distinct reserva.codigo) as reservas ");
		sqlStr.append(" FROM reserva ");
		sqlStr.append(" INNER JOIN catalogo ON catalogo.codigo = reserva.catalogo  ");
		sqlStr.append(" left JOIN editora ON editora.codigo = catalogo.editora ");		
		sqlStr.append(" WHERE reserva.biblioteca = ").append(biblioteca);		
		if (catalogo != null && !catalogo.equals(0)) {
			sqlStr.append(" AND catalogo.codigo = ").append(catalogo);
		}
		if (prazoEncerrado || dentroPrazo || aguardandoExemplar || reservaConcluida || reservaCancelada) {
			sqlStr.append(" AND ( ");
			String or = "";
			if (prazoEncerrado) {
				sqlStr.append(" (dataterminoreserva is not null AND dataterminoreserva < current_timestamp AND emprestimo is null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (dentroPrazo) {
				sqlStr.append(or).append(" (dataterminoreserva is not null AND dataterminoreserva > current_timestamp AND emprestimo is null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (aguardandoExemplar) {
				sqlStr.append(or).append(" (dataterminoreserva is null AND emprestimo is null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (reservaConcluida) {
				sqlStr.append(or).append(" (emprestimo is not null and reserva.situacao != 'CA') ");
				or = " or  ";
			}
			if (reservaCancelada) {
				sqlStr.append(or).append(" (reserva.situacao = 'CA') ");				
			}
			sqlStr.append(" ) ");
		}
		sqlStr.append(" AND reserva.datareserva >=  '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sqlStr.append(" AND reserva.datareserva <=  '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sqlStr.append(" group by catalogo.edicao, catalogo.titulo, editora.nome, catalogo.anopublicacao, cutterpha ");
		sqlStr.append(" order by catalogo.titulo, catalogo.codigo ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	private ReservaCatalogoRelVO montarDados(SqlRowSet dadosSQL) {
		ReservaCatalogoRelVO obj = new ReservaCatalogoRelVO();
		obj.setCatalogo(dadosSQL.getString("catalogo"));
		obj.setEdicao(dadosSQL.getString("edicao"));
		obj.setEditora(dadosSQL.getString("editora"));
		obj.setAnoPublicacao(dadosSQL.getString("anopublicacao"));
		obj.setReservas(dadosSQL.getInt("reservas"));
		obj.getCatalogoVO().setCutterPha(dadosSQL.getString("cutterpha"));
		return obj;
	}

	public void validarDados(Integer biblioteca, Date dataInicio, Date dataFim) throws Exception {
		if (biblioteca == null || biblioteca.equals(0)) {
			throw new Exception("Para Emissão do Relatório é necessário informar a Biblioteca.");
		}
//		if (dataInicio.before(new Date()) && !Uteis.compararDatasSemConsiderarHoraMinutoSegundo(dataInicio, new Date())) {
//			throw new Exception("Não é possível emitir o relatório de um período passado. ");
//		}
	}

	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + "Sintetico.jrxml");
	}

	public String designIReportRelatorioAnalitico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + "Analitico.jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
	}

	public static String getIdEntidade() {
		return "ReservaCatalogoRel";
	}
}
