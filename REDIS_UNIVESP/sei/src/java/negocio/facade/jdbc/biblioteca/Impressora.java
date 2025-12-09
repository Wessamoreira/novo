package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.PoolImpressaoVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;
import negocio.comuns.biblioteca.enumeradores.TipoImpressoraEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ImpressoraInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class Impressora extends ControleAcesso implements ImpressoraInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1009591738780468119L;
	protected static String idEntidade;
	
	  public Impressora() throws Exception {
	        super();
	        setIdEntidade("Impressora");
	    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception {
		if(impressoraVO.isNovoObj()){
			incluir(impressoraVO, usuarioVO);
		}else{
			alterar(impressoraVO, usuarioVO);
		}
	}
	
	private void validarDados(ImpressoraVO impressoraVO) throws ConsistirException{
		if(impressoraVO.getNomeImpressora().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Impressora_nomeImpressora"));
		}
		if(impressoraVO.getIdIdentificacao().trim().isEmpty()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Impressora_idIdentificacao"));
		}
		if(!impressoraVO.getUsarBiblioteca() && !impressoraVO.getUsarFinanceiro() && !impressoraVO.getUsarRequerimento()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Impressora_uso"));
		}
		if(impressoraVO.getUsarBiblioteca() && !Uteis.isAtributoPreenchido(impressoraVO.getBibliotecaVO())){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Impressora_biblioteca"));
		}		
	}
	
	private void validarUnicidade(ImpressoraVO impressoraVO) throws ConsistirException{
		StringBuilder sql = new StringBuilder("select codigo from impressora where sem_acentos(idIdentificacao) ilike sem_acentos(?) and codigo != ? ");
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), impressoraVO.getIdIdentificacao(), impressoraVO.getCodigo());
		if(rs.next()){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Impressora_unicidade_idIdentificacao"));
		}
	}
		
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ImpressoraVO impressoraVO, final UsuarioVO usuarioVO) throws Exception {
		try {
		Impressora.incluir(getIdEntidade(), true, usuarioVO);
        validarDados(impressoraVO);
        validarUnicidade(impressoraVO);
        final String sql = "INSERT INTO Impressora( biblioteca, idIdentificacao, ipRedeInterna, nomeImpressora, tipoImpressora, usarBiblioteca, usarFinanceiro, unidadeEnsino, usarRequerimento ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        impressoraVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql);
                if(impressoraVO.getUsarBiblioteca()){
                	ps.setInt(1, impressoraVO.getBibliotecaVO().getCodigo());
                }else{
                	ps.setNull(1, 0);
                }
                ps.setString(2, impressoraVO.getIdIdentificacao());
                ps.setString(3, impressoraVO.getIpRedeInterna());
                ps.setString(4, impressoraVO.getNomeImpressora());
                ps.setString(5, impressoraVO.getTipoImpressora().name());
                ps.setBoolean(6, impressoraVO.getUsarBiblioteca());
                ps.setBoolean(7, impressoraVO.getUsarFinanceiro());
                if((impressoraVO.getUsarFinanceiro() || impressoraVO.getUsarRequerimento()) && Uteis.isAtributoPreenchido(impressoraVO.getUnidadeEnsinoVO().getCodigo())){
                	ps.setInt(8, impressoraVO.getUnidadeEnsinoVO().getCodigo());
                }else{
                	ps.setNull(8, 0);
                }
                ps.setBoolean(9, impressoraVO.getUsarRequerimento());
                return ps;
            }
        }, new ResultSetExtractor<Integer>() {

            public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                	impressoraVO.setNovoObj(Boolean.FALSE);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
    } catch (Exception e) {
    	impressoraVO.setNovoObj(true);
        throw e;
    }
	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ImpressoraVO impressoraVO, final UsuarioVO usuarioVO) throws Exception {
		 try {
		Impressora.alterar(getIdEntidade(), true, usuarioVO);
        validarDados(impressoraVO);
        validarUnicidade(impressoraVO);
        final String sql = "UPDATE Impressora set biblioteca = ?, idIdentificacao = ?, ipRedeInterna = ?, nomeImpressora = ?, tipoImpressora=?, usarBiblioteca = ?, usarFinanceiro = ?, unidadeEnsino = ?, usarRequerimento = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
            	 PreparedStatement ps = arg0.prepareStatement(sql);
            	 if(impressoraVO.getUsarBiblioteca()){
                 	ps.setInt(1, impressoraVO.getBibliotecaVO().getCodigo());
                 }else{
                 	ps.setNull(1, 0);
                 }
                 ps.setString(2, impressoraVO.getIdIdentificacao());
                 ps.setString(3, impressoraVO.getIpRedeInterna());
                 ps.setString(4, impressoraVO.getNomeImpressora());
                 ps.setString(5, impressoraVO.getTipoImpressora().name());
                 ps.setBoolean(6, impressoraVO.getUsarBiblioteca());
                 ps.setBoolean(7, impressoraVO.getUsarFinanceiro());
                 if((impressoraVO.getUsarFinanceiro() || impressoraVO.getUsarRequerimento()) && Uteis.isAtributoPreenchido(impressoraVO.getUnidadeEnsinoVO().getCodigo())){
                 	ps.setInt(8, impressoraVO.getUnidadeEnsinoVO().getCodigo());
                 }else{
                 	ps.setNull(8, 0);
                 }
                 ps.setBoolean(9, impressoraVO.getUsarRequerimento());
                 ps.setInt(10, impressoraVO.getCodigo());
                 return ps;
            }
        });
        
    } catch (Exception e) {
        throw e;
    }
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception {
		Impressora.excluir(getIdEntidade(), true, usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM Impressora where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), impressoraVO.getCodigo());
	}

	@Override
	public List<ImpressoraVO> consultar(String consultarPor, String valorConsulta, UnidadeEnsinoVO unidadeEnsinoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		Impressora.consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql  = new StringBuilder("select impressora.*, biblioteca.nome as biblioteca_nome, unidadeensino.nome as unidadeensino_nome ");
		sql.append(" from impressora ");
		sql.append(" left join biblioteca on biblioteca.codigo =  impressora.biblioteca ");
		sql.append(" left join unidadeensino on unidadeensino.codigo =  impressora.unidadeensino ");
		if(consultarPor.equals("nome")){
			sql.append(" where sem_acentos(impressora.nomeimpressora) ilike sem_acentos((?)) ");
		}else if(consultarPor.equals("idIdentificacao")){
			sql.append(" where sem_acentos(impressora.idIdentificacao) ilike sem_acentos((?)) ");
		}else if(consultarPor.equals("biblioteca")){
			sql.append(" where sem_acentos(biblioteca.nome) ilike sem_acentos((?)) and usarBiblioteca = true ");
		}else if(consultarPor.equals("unidadeEnsino")){
			sql.append(" where (((sem_acentos(unidadeensino.nome) ilike sem_acentos((?)) and usarFinanceiro = true) or (unidadeensino.codigo is null  and usarFinanceiro = true)) ");
			sql.append(" or (usarBiblioteca = true and exists (select unidadeensinobiblioteca.unidadeensino from unidadeensinobiblioteca inner join unidadeensino ue on ue.codigo = unidadeensinobiblioteca.unidadeensino where unidadeensinobiblioteca.biblioteca = biblioteca.codigo and sem_acentos(ue.nome) ilike sem_acentos((?)) limit 1 ))) ");
		}else if(consultarPor.equals("codigoUnidadeEnsino")){
			sql.append(" where ((unidadeensino.codigo = ? and usarFinanceiro = true) or (unidadeensino.codigo is null and usarFinanceiro = true))  ");
		}else if(consultarPor.equals("requerimento")){
			sql.append(" where ((unidadeensino.codigo = ? and usarrequerimento = true) or (unidadeensino.codigo is null and usarrequerimento = true))  ");
		}else if(consultarPor.equals("codigoBiblioteca")){
			sql.append(" where biblioteca.codigo = ? and usarBiblioteca = true ");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVO)){
			sql.append(" and (((usarFinanceiro = true or usarRequerimento = true) and (unidadeensino.codigo = ").append(unidadeEnsinoVO.getCodigo()).append(" or unidadeensino.codigo is null)) ");
			sql.append(" or (usarBiblioteca = true and exists( select unidadeensinobiblioteca.unidadeensino from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca = biblioteca.codigo  ");
			sql.append(" and unidadeensinobiblioteca.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" limit 1))) ") ;
		}
		if(consultarPor.equals("nome")){
			sql.append(" order by impressora.nomeimpressora ");
		}else if(consultarPor.equals("idIdentificacao")){
			sql.append(" order by impressora.idIdentificacao ");
		}else{
			sql.append(" order by biblioteca.nome, unidadeensino.nome, impressora.nomeimpressora ");
		}
		if(consultarPor.equals("codigoBiblioteca")){
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.valueOf(valorConsulta)));
		}else if(consultarPor.equals("codigoUnidadeEnsino") || consultarPor.equals("requerimento")){
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.valueOf(valorConsulta)));
		}else if(consultarPor.equals("unidadeEnsino")){
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%", valorConsulta+"%"));
		}else{
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%"));
		}
	}
	
	private List<ImpressoraVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<ImpressoraVO> impressoraVOs = new ArrayList<ImpressoraVO>(0);			
		while(rs.next()){
			impressoraVOs.add(montarDados(rs));
		}
		return impressoraVOs;
	}
	
	private ImpressoraVO montarDados(SqlRowSet rs) throws Exception{
		ImpressoraVO impressoraVO = new ImpressoraVO();
		impressoraVO.setNovoObj(false);
		impressoraVO.setCodigo(rs.getInt("codigo"));
		impressoraVO.setNomeImpressora(rs.getString("nomeImpressora"));
		impressoraVO.setIdIdentificacao(rs.getString("idIdentificacao"));
		impressoraVO.setIpRedeInterna(rs.getString("ipRedeInterna"));
		impressoraVO.setTipoImpressora(TipoImpressoraEnum.valueOf(rs.getString("tipoImpressora")));
		impressoraVO.getBibliotecaVO().setCodigo(rs.getInt("biblioteca"));
		impressoraVO.getBibliotecaVO().setNome(rs.getString("biblioteca_nome"));
		impressoraVO.setUsarBiblioteca(rs.getBoolean("usarBiblioteca"));
		impressoraVO.setUsarFinanceiro(rs.getBoolean("usarFinanceiro"));
		impressoraVO.setUsarRequerimento(rs.getBoolean("usarRequerimento"));
		impressoraVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeEnsino"));
		impressoraVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsino_nome"));
		return impressoraVO;
	}

	 /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return Impressora.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
    	Impressora.idEntidade = idEntidade;
    }
    
    @Override
    public void realizarImpressaoTeste(ImpressoraVO impressoraVO, UsuarioVO usuarioVO) throws Exception{
    	if (impressoraVO.getUsarFinanceiro() || impressoraVO.getUsarBiblioteca()) {
    	PoolImpressaoVO poolImpressaoVO = new PoolImpressaoVO();
    	poolImpressaoVO.setData(new Date());
    	poolImpressaoVO.setImpressoraVO(impressoraVO);
    	poolImpressaoVO.setFormatoImpressao(FormatoImpressaoEnum.TEXTO);
    	StringBuilder imprimir = new StringBuilder("");
    	if (impressoraVO.getUsarFinanceiro()) {    		
    		imprimir.append("                                >");
    		imprimir.append("NOME UNIDADE ENSINO").append(">");
    		imprimir.append("CEP: 000.000-000 END.: Endereço Unidade Ensino >");
    		imprimir.append("Bairro/Setor Unidade Ensino / ");
    		imprimir.append("Cidade Unidade Ensino >");
    		imprimir.append("TEL.: (00) 0000-0000  CNPJ: 00.000.000/0000.00 >");
    		imprimir.append("         					     >");
    		imprimir.append("Data Pgto: ").append(Uteis.getData(new Date())).append(">");
    		imprimir.append("Atendente: ").append(usuarioVO.getNome()).append(">");
    		imprimir.append("Cod Pgto: 0123456789>");
    		imprimir.append("         					     >");
    		imprimir.append("Recibo de Quitacao").append(">");    		
    		imprimir.append("Aluno: 000000 - Nome Aluno Recebimento >");    		
    		imprimir.append("Curso: Curso do Aluno >");    				
   			imprimir.append("Titulo: 1/6 ");
   			imprimir.append("Vcto.: ").append(Uteis.getData(new Date())).append("  ").append("Valor:         1.100,00>");
   			imprimir.append("               Desconto:          -100,00>");
   			imprimir.append("----------------------------------------->");
   			imprimir.append("Valor Total Pagamento  :         1.000,00>");
   			if(impressoraVO.getUsarBiblioteca()){
   				imprimir.append("                                >");
   				imprimir.append("                                >");
   				imprimir.append("                                >");
   				imprimir.append("                                >");   				
   			}
    	}
    	
		if (impressoraVO.getUsarBiblioteca()) {
			impressoraVO.setBibliotecaVO(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(
					impressoraVO.getBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			ConfiguracaoBibliotecaVO configuracaoBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade()
					.consultarConfiguracaoPorBiblioteca(impressoraVO.getBibliotecaVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			imprimir.append(
					Uteis.removeCaractersEspeciais(impressoraVO.getBibliotecaVO().getNome().toUpperCase()) + ">");
			imprimir.append("SOLICITANTE: TESTE IMPRESSAO>");
			imprimir.append("MATRICULA: 123456789>");
			imprimir.append("UNIDADE DE ENS.: UNIDADE TESTE>");
			imprimir.append("DATA: ").append(Uteis.getDataAtual()).append(">");
			imprimir.append("EMPRESTIMO(S):>");
			imprimir.append("1 - DT EMP: ").append(Uteis.getDataAtual()).append("");
			imprimir.append("    DT PREV DEV: ").append(Uteis.getData(Uteis.obterDataAvancada(new Date(), 10)))
					.append(">");
			imprimir.append("TITULO: TITULO DO CATALOGO DE TESTE>");
			imprimir.append("TOMBO: 000123456>");
			imprimir.append(" >");
			imprimir.append("VOL: 10  EX: 1>");
			imprimir.append("---------------------------------------->");
			if (!configuracaoBibliotecaVO.getCodigo().equals(0)
					&& !configuracaoBibliotecaVO.getTextoPadraoEmprestimo().equals("")) {
				imprimir.append(
						Uteis.removerAcentos(configuracaoBibliotecaVO.getTextoPadraoEmprestimo().toUpperCase() + ">"));
			}
			imprimir.append("                                               >");
			imprimir.append("DEVOLUCAO(OES):>");
			imprimir.append("1 - DT EMP: ").append(Uteis.getData(Uteis.obterDataAntiga(new Date(), 10))).append("");
			imprimir.append("    DT DEV: ").append(Uteis.getDataAtual()).append(">");
			imprimir.append("TITULO: TITULO DEVOLVIDO DO CATALOGO DE TESTE>");
			imprimir.append("TOMBO: 000123457>");
			imprimir.append(" >");
			imprimir.append("VOL: 10  EX: 1>");
			imprimir.append("---------------------------------------->");
			if (!configuracaoBibliotecaVO.getCodigo().equals(0)
					&& !configuracaoBibliotecaVO.getTextoPadraoDevolucao().equals("")) {
				imprimir.append(
						Uteis.removerAcentos(configuracaoBibliotecaVO.getTextoPadraoDevolucao().toUpperCase() + ">"));
			}
			imprimir.append("                                               >");
			imprimir.append("RENOVACAO(OES):>");
			imprimir.append("1 - DT EMP: ").append(Uteis.getData(Uteis.obterDataAntiga(new Date(), 10))).append("");
			imprimir.append("    DT PREV DEV: ").append(Uteis.getDataAtual()).append(">");
			imprimir.append("TITULO: TITULO RENOVADO DO CATALOGO DE TESTE>");
			imprimir.append("TOMBO: 000123458>");
			imprimir.append(" >");
			imprimir.append("VOL: 10  EX: 1>");
			imprimir.append("---------------------------------------->");
			imprimir.append("                                               >");
			imprimir.append("&&&&&_______________________________&&&&&>");
			String nomePessoa = Uteis.removeCaractersEspeciais(usuarioVO.getNome().toUpperCase());
			if (nomePessoa.length() < 42) {
				int espaco = (42 - nomePessoa.length()) / 2;
				for (int x = 1; x <= espaco; x++) {
					nomePessoa = "&" + nomePessoa + "&";
				}
			}
			imprimir.append(nomePessoa + ">");
			imprimir.append("                                               >");
			imprimir.append("&&&&&_______________________________&&&&&>");

			String nomeUsuario = Uteis.removeCaractersEspeciais(usuarioVO.getNome_Apresentar());
			if (nomeUsuario.length() < 42) {
				int espaco = (42 - nomeUsuario.length()) / 2;
				for (int x = 1; x <= espaco; x++) {
					nomeUsuario = "&" + nomeUsuario + "&";
				}
			}
			imprimir.append(nomeUsuario + ">");
			imprimir.append("                                >");
			imprimir.append("                                >");
			imprimir.append("                                >");
			imprimir.append("                                >");			
		}
		poolImpressaoVO.setImprimir(imprimir.toString());
		getFacadeFactory().getPoolImpressaoFacade().incluir(poolImpressaoVO, usuarioVO);
    	}
		if(impressoraVO.getUsarRequerimento()){
			RequerimentoVO requerimentoVO = new RequerimentoVO();
			requerimentoVO.setCodigo(123456789);
			requerimentoVO.getUnidadeEnsino().setNome("Nome Unidade Ensino");
			requerimentoVO.getPessoa().setNome("Nome Requerente");
			requerimentoVO.getPessoa().setCPF("999.999.999-99");
			requerimentoVO.getPessoa().setRG("99999-9");
			requerimentoVO.getPessoa().setEmail("email@email.com.br");
			requerimentoVO.getMatricula().setMatricula("9999999999");
			requerimentoVO.getCurso().setNome("Nome Curso Aluno");
			requerimentoVO.getTurno().setNome("Nome Turno Aluno");
			requerimentoVO.getTurma().setIdentificadorTurma("Turma do Aluno");
			requerimentoVO.getTurma().getPeridoLetivo().setPeriodoLetivo(0);
			requerimentoVO.setAno(Uteis.getAnoDataAtual4Digitos());
			requerimentoVO.setSemestre(Uteis.getSemestreAtual());
			requerimentoVO.getPessoa().setCelular("(99)99999-9999");
			requerimentoVO.getPessoa().setTelefoneRes("(99)99999-9999");
			requerimentoVO.getDisciplina().setCodigo(1);
			requerimentoVO.getDisciplina().setNome("Nome Disciplina Inclusao");
			requerimentoVO.getTipoRequerimento().setNome("Nome do Requerimento");
			requerimentoVO.getTipoRequerimento().setMensagemAlerta("Orientacoes de abertura do requerimento");
			requerimentoVO.setObservacao("Observacoes do requerimento");
			requerimentoVO.setNumeroVia(1);
			requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().setCodigo(1);
			requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().setNome("Nome Unidade Ensino Transf. Interna");
			requerimentoVO.getCursoTransferenciaInternaVO().setCodigo(1);
			requerimentoVO.getCursoTransferenciaInternaVO().setNome("Nome Curso Transf. Interna");
			requerimentoVO.getTurnoTransferenciaInternaVO().setCodigo(1);
			requerimentoVO.getTurnoTransferenciaInternaVO().setNome("Nome Turno Transf. Interna");
			getFacadeFactory().getRequerimentoFacade().imprimirComprovanteRequerimentoBemateck(requerimentoVO, impressoraVO, usuarioVO, "");
		}
    }
}
