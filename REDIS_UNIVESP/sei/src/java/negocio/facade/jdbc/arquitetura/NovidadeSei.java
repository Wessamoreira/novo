package negocio.facade.jdbc.arquitetura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.NovidadeSeiVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoNovidadeEnum;
import negocio.comuns.basico.enumeradores.TipoArtefatoAjudaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.NovidadeSeiInterfaceFacade;

@Repository
@Lazy
public class NovidadeSei extends ControleAcesso implements NovidadeSeiInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -7538183098284023957L;

    @Override
    public List<NovidadeSeiVO> consultarNovidades(Integer limit, Integer pagina, boolean trazerApenasNaoVisualizadas, TipoNovidadeEnum tipoNovidadeEnum, String palavraChave, boolean trazerApenasDestaque, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder("SELECT NovidadeSei.*  ");
        if(usuarioVO != null && usuarioVO.getCodigo() > 0){
            sb.append(", (select count(NovidadeSeiAcesso.*) from NovidadeSeiAcesso where NovidadeSeiAcesso.NovidadeSei = NovidadeSei.codigo and  NovidadeSeiAcesso.usuarioAcesso = ").append(usuarioVO.getCodigo()).append(" limit 1 ) > 0 as visualizado ");
        }else{
            sb.append(", true as visualizado ");
        }
        sb.append(" FROM NovidadeSei where (dataLimiteDisponibilidade is null or dataLimiteDisponibilidade >= now()) and (dataInicioDisponibilidade is null or dataInicioDisponibilidade <= now() ) ");
        if(tipoNovidadeEnum != null) {
        	sb.append(" and tipoNovidade = '").append(tipoNovidadeEnum.name()).append("' ");
        }
        if(trazerApenasDestaque) {
        	sb.append(" and coalesce(destaque, false) ");
        }
        if(Uteis.isAtributoPreenchido(palavraChave)) {
        	sb.append(" and (sem_acentos(palavrasChaves) ilike sem_acentos(?) or  sem_acentos(descricao) ilike sem_acentos(?))  ");
        }
        if(trazerApenasNaoVisualizadas) {
        	sb.append(" and not exists (select NovidadeSeiAcesso.usuarioAcesso from NovidadeSeiAcesso where NovidadeSeiAcesso.NovidadeSei = NovidadeSei.codigo and  NovidadeSeiAcesso.usuarioAcesso = ").append(usuarioVO.getCodigo()).append(" limit 1)");
        	sb.append(" and data >= (now() - '30 days'::interval) ");
        }
        sb.append(" order by data desc");
        if(limit != null && limit>0){
            sb.append(" limit ").append(limit).append(" offset ").append(pagina);
        }
        if(Uteis.isAtributoPreenchido(palavraChave)) {
        	return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT+palavraChave+PERCENT, PERCENT+palavraChave+PERCENT));
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
    }
    
    private List<NovidadeSeiVO>  montarDadosConsulta(SqlRowSet rs){
        List<NovidadeSeiVO> objs =  new ArrayList<NovidadeSeiVO>(0);
        while(rs.next()){
            objs.add(montarDados(rs));
        }
        return objs;
    }
    
    private NovidadeSeiVO montarDados(SqlRowSet rs){
        NovidadeSeiVO obj = new NovidadeSeiVO(rs.getInt("codigo"), rs.getString("versao"), rs.getString("url"), rs.getDate("data"), rs.getString("descricao"), rs.getBoolean("visualizado"));
        obj.setTipoArtefato(TipoArtefatoAjudaEnum.valueOf(rs.getString("tipoArtefato")));
        obj.setPalavrasChaves(rs.getString("palavrasChaves"));
        obj.setTextoInformativo(rs.getString("textoInformativo"));
        obj.setDestaque(rs.getBoolean("destaque"));        
        obj.setDataLimiteDisponibilidade(rs.getDate("dataLimiteDisponibilidade"));
        obj.setDataInicioDisponibilidade(rs.getDate("dataInicioDisponibilidade"));
        obj.setTipoNovidade(TipoNovidadeEnum.valueOf(rs.getString("tipoNovidade")));
        return obj;
    }

    @Override
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)    
    public void registrarVisualizacao(final NovidadeSeiVO novidadeSeiVO, final UsuarioVO usuarioVO) {
        
        try {            
            final StringBuilder sql = new StringBuilder("INSERT INTO NovidadeSeiAcesso ");
            sql.append(" (novidadeSei, dataAcesso, usuarioAcesso) ");
            sql.append(" VALUES ( ?, ?, ?) ");
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(x++, novidadeSeiVO.getCodigo());
                    sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlInserir.setInt(x++, usuarioVO.getCodigo());                                       
                    return sqlInserir;
                }
            });            
        } catch (Exception e) {            
            
        }

    }

    @Override
    public Integer consultarTotalRegistroNovidades(boolean trazerApenasNaoVisualizadas, TipoNovidadeEnum tipoNovidadeEnum, String palavraChave, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder("SELECT count(codigo) as qtde  ");        
        sb.append(" FROM NovidadeSei where (dataLimiteDisponibilidade is null or dataLimiteDisponibilidade >= now()) and (dataInicioDisponibilidade is null or dataInicioDisponibilidade <= now() ) ");
        if(tipoNovidadeEnum != null) {
        	sb.append(" and tipoNovidade = '").append(tipoNovidadeEnum.name()).append("' ");
        }
        if(Uteis.isAtributoPreenchido(palavraChave)) {
        	sb.append(" and (sem_acentos(palavrasChaves) ilike sem_acentos(?) or  sem_acentos(descricao) ilike sem_acentos(?))  ");
        }
        if(trazerApenasNaoVisualizadas) {
        	sb.append(" and not exists (select NovidadeSeiAcesso.usuarioAcesso from NovidadeSeiAcesso where NovidadeSeiAcesso.NovidadeSei = NovidadeSei.codigo and  NovidadeSeiAcesso.usuarioAcesso = ").append(usuarioVO.getCodigo()).append(" limit 1)");
        	sb.append(" and data >= (now() - '30 days'::interval) ");
        }
        SqlRowSet rs = null;
        if(Uteis.isAtributoPreenchido(palavraChave)) {
        	rs = (getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT+palavraChave+PERCENT, PERCENT+palavraChave+PERCENT));
        }else {
        	rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        }
        if(rs.next()){
            return rs.getInt("qtde");
        }
        return 0;
    }

    @Override
    public Boolean realizarValidacaoNovidadeSemVisualizacaoUsuario(Integer usuario) {
        StringBuilder sb = new StringBuilder("SELECT codigo as qtde  ");
        sb.append(" FROM NovidadeSei where (dataLimiteDisponibilidade is null or dataLimiteDisponibilidade >= now()) and (dataInicioDisponibilidade is null or dataInicioDisponibilidade <= now() ) and codigo not in (");
        sb.append(" (select NovidadeSeiAcesso.NovidadeSei from NovidadeSeiAcesso ");
        sb.append(" where NovidadeSeiAcesso.NovidadeSei = NovidadeSei.codigo and  NovidadeSeiAcesso.usuarioAcesso = ");
        sb.append(usuario).append(" ) ) ");
        sb.append(" and data >= (now() - '30 days'::interval) ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return rs.next();
    }

    @Override
    public NovidadeSeiVO consultarNovidadeUsuarioApresentar(UsuarioVO usuario) {
        StringBuilder sb = new StringBuilder("SELECT NovidadeSei.*, false as visualizado");
        sb.append(" FROM NovidadeSei where (dataLimiteDisponibilidade is null or dataLimiteDisponibilidade >= now()) and (dataInicioDisponibilidade is null or dataInicioDisponibilidade <= now() ) and codigo not in (");
        sb.append(" (select NovidadeSeiAcesso.NovidadeSei from NovidadeSeiAcesso ");
        sb.append(" where NovidadeSeiAcesso.NovidadeSei = NovidadeSei.codigo and  NovidadeSeiAcesso.usuarioAcesso = ");
        sb.append(usuario.getCodigo()).append(") ) order by data desc limit 1 ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            NovidadeSeiVO obj = montarDados(rs);
            registrarVisualizacao(obj, usuario);
            obj.setVisualizado(true);
            return obj;
        }
        sb = new StringBuilder(" SELECT NovidadeSei.*, true as visualizado");
        sb.append(" FROM NovidadeSei where(dataLimiteDisponibilidade is null or dataLimiteDisponibilidade >= now()) and (dataInicioDisponibilidade is null or dataInicioDisponibilidade <= now() ) order by data desc limit 1 ");
        rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            return montarDados(rs);
        }
        return new NovidadeSeiVO();
    }
    
    @Override
    public Integer realizarContagemNovidadeSemVisualizacaoUsuario(Integer usuario) {
        StringBuilder sb = new StringBuilder("SELECT count (codigo) as qtde  ");
        sb.append(" FROM NovidadeSei where (dataLimiteDisponibilidade is null or dataLimiteDisponibilidade >= now()) and (dataInicioDisponibilidade is null or dataInicioDisponibilidade <= now() )  and codigo not in (");
        sb.append(" (select NovidadeSeiAcesso.NovidadeSei from NovidadeSeiAcesso ");
        sb.append(" where NovidadeSeiAcesso.NovidadeSei = NovidadeSei.codigo and  NovidadeSeiAcesso.usuarioAcesso = ");
        sb.append(usuario).append(" ) ) ");
        sb.append(" and data >= (now() - '30 days'::interval) ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if(rs.next()){
            return rs.getInt("qtde");
        }
        return 0;
    }
    
    @Override
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)    
    public void persistir(NovidadeSeiVO novidadeSeiVO) throws Exception{
    	alterar(novidadeSeiVO, "NovidadeSei", new AtributoPersistencia().add("codigo", novidadeSeiVO.getCodigo())
    			.add("descricao", novidadeSeiVO.getDescricao())
    			.add("textoInformativo", novidadeSeiVO.getTextoInformativo())
    			.add("palavrasChaves", novidadeSeiVO.getPalavrasChaves())
    			.add("url", novidadeSeiVO.getUrl())
    			.add("versao", novidadeSeiVO.getVersao())
    			.add("data", novidadeSeiVO.getData())
    			.add("dataInicioDisponibilidade", Uteis.getDataJDBCTimestamp(novidadeSeiVO.getDataInicioDisponibilidade()))
    			.add("dataLimiteDisponibilidade",  Uteis.getDataJDBCTimestamp(novidadeSeiVO.getDataLimiteDisponibilidade()))
    			.add("tipoNovidade", novidadeSeiVO.getTipoNovidade().name())
    			.add("tipoArtefato", novidadeSeiVO.getTipoArtefato().name())
    			.add("destaque", novidadeSeiVO.getDestaque()), new AtributoPersistencia().add("codigo", novidadeSeiVO.getCodigo()), null);
    }

    @Override
    @Transactional(readOnly=false, isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)    
    public void excluir(NovidadeSeiVO novidadeSeiVO) throws Exception{
    	getConexao().getJdbcTemplate().update("delete from NovidadeSei where codigo = ? ", novidadeSeiVO.getCodigo());    	
    }

}
