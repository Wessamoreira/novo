package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DataComemorativaVO;
import negocio.comuns.basico.TipoDestinatarioDataComemorativaEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.DataComemorativaInterfaceFacade;

@Repository
@Lazy
public class DataComemorativa extends ControleAcesso implements DataComemorativaInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -399437303211845282L;
    protected static String idEntidade;

    public DataComemorativa() throws Exception {
        super();
        setIdEntidade("DataComemorativa");
    }

    @Override
    public void persistir(DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        validarDados(dataComemorativaVO);
        if (dataComemorativaVO.isNovoObj()) {
            incluir(dataComemorativaVO, usuarioLogado, configuracaoGeralSistema);
        } else {
            alterar(dataComemorativaVO, usuarioLogado, configuracaoGeralSistema);
        }

    }

    @SuppressWarnings({"unchecked"})
    private void incluir(final DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {

        try {
            DataComemorativa.incluir(getIdEntidade(), true, usuarioLogado);
            if (dataComemorativaVO.getArquivoBannerImagem().getPastaBaseArquivoEnum() != null) {
                getFacadeFactory().getArquivoFacade().incluir(dataComemorativaVO.getArquivoBannerImagem(), false, usuarioLogado, configuracaoGeralSistema);
            }
            if (dataComemorativaVO.getArquivoImagemTopo().getPastaBaseArquivoEnum() != null) {
                getFacadeFactory().getArquivoFacade().incluir(dataComemorativaVO.getArquivoImagemTopo(), false, usuarioLogado, configuracaoGeralSistema);
            }
            if (dataComemorativaVO.getDiasApresentarAntecendencia() > 0) {
                dataComemorativaVO.setDataDiaAntecedencia(Uteis.obterDataAntiga(dataComemorativaVO.getDataComemorativa(), dataComemorativaVO.getDiasApresentarAntecendencia()));
            } else {
                dataComemorativaVO.setDataDiaAntecedencia(dataComemorativaVO.getDataComemorativa());
            }
            final String sql = "INSERT INTO DataComemorativa( dataComemorativa, assunto, mensagem, tipoDestinatarioDataComemorativa,"
                    + " cargo, areaProfissional, areaConhecimento, unidadeEnsino, departamento, status, comunicadoInterno, bannerComunicado, mensagemTopo, arquivoBannerImagem, bannerMensagem, diasApresentarAntecendencia, mensagemTopoTexto, arquivoImagemTopo, dataDiaAntecedencia ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?) returning codigo";
            dataComemorativaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(dataComemorativaVO.getDataComemorativa()));
                    sqlInserir.setString(2, dataComemorativaVO.getAssunto());
                    sqlInserir.setString(3, dataComemorativaVO.getMensagem());
                    sqlInserir.setString(4, dataComemorativaVO.getTipoDestinatarioDataComemorativa().toString());
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.CARGO)) {
                        sqlInserir.setInt(5, dataComemorativaVO.getCargo().getCodigo());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL)) {
                        sqlInserir.setInt(6, dataComemorativaVO.getAreaProfissional().getCodigo());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_CONHECIMENTO)) {
                        sqlInserir.setInt(7, dataComemorativaVO.getAreaConhecimento().getCodigo());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.UNIDADE_ENSINO)) {
                        sqlInserir.setInt(8, dataComemorativaVO.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.DEPARTAMENTO)) {
                        sqlInserir.setInt(9, dataComemorativaVO.getDepartamento().getCodigo());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    sqlInserir.setString(10, dataComemorativaVO.getStatus().toString());
                    sqlInserir.setBoolean(11, dataComemorativaVO.getComunicadoInterno().booleanValue());
                    sqlInserir.setBoolean(12, dataComemorativaVO.getBannerComunicado().booleanValue());
                    sqlInserir.setBoolean(13, dataComemorativaVO.getMensagemTopo().booleanValue());
                    if (dataComemorativaVO.getArquivoBannerImagem().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(14, dataComemorativaVO.getArquivoBannerImagem().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(14, 0);
                    }
                    sqlInserir.setString(15, dataComemorativaVO.getBannerMensagem());
                    sqlInserir.setInt(16, dataComemorativaVO.getDiasApresentarAntecendencia());
                    sqlInserir.setString(17, dataComemorativaVO.getMensagemTopoTexto());
                    if (dataComemorativaVO.getArquivoImagemTopo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(18, dataComemorativaVO.getArquivoImagemTopo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(18, 0);
                    }
                    if (dataComemorativaVO.getDataDiaAntecedencia() != null) {
                        sqlInserir.setDate(19, Uteis.getDataJDBC(dataComemorativaVO.getDataDiaAntecedencia()));
                    } else {
                        sqlInserir.setNull(19, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        dataComemorativaVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            dataComemorativaVO.setNovoObj(true);
            throw e;
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            DataComemorativa.alterar(getIdEntidade(), true, usuarioLogado);
            if (dataComemorativaVO.getArquivoBannerImagem().getPastaBaseArquivoEnum() != null) {
                if (dataComemorativaVO.getArquivoBannerImagem().getCodigo() == 0) {
                    getFacadeFactory().getArquivoFacade().incluir(dataComemorativaVO.getArquivoBannerImagem(), false, usuarioLogado, configuracaoGeralSistema);
                } else {
                    getFacadeFactory().getArquivoFacade().alterar(dataComemorativaVO.getArquivoBannerImagem(), false, usuarioLogado, configuracaoGeralSistema);
                }
            }
            if (dataComemorativaVO.getArquivoImagemTopo().getPastaBaseArquivoEnum() != null) {
                if (dataComemorativaVO.getArquivoImagemTopo().getCodigo() == 0) {
                    getFacadeFactory().getArquivoFacade().incluir(dataComemorativaVO.getArquivoImagemTopo(), false, usuarioLogado, configuracaoGeralSistema);
                } else {
                    getFacadeFactory().getArquivoFacade().alterar(dataComemorativaVO.getArquivoImagemTopo(), false, usuarioLogado, configuracaoGeralSistema);
                }
            }
            if (dataComemorativaVO.getDiasApresentarAntecendencia() > 0) {
                dataComemorativaVO.setDataDiaAntecedencia(Uteis.obterDataAntiga(dataComemorativaVO.getDataComemorativa(), dataComemorativaVO.getDiasApresentarAntecendencia()));
            } else {
                dataComemorativaVO.setDataDiaAntecedencia(dataComemorativaVO.getDataComemorativa());
            }
            final String sql = "UPDATE DataComemorativa set dataComemorativa=?, assunto=?, mensagem=?, tipoDestinatarioDataComemorativa=?,"
                    + " cargo=?, areaProfissional=?, areaConhecimento=?, unidadeEnsino=?, departamento=?, status=?, "
                    + " comunicadoInterno=?, bannerComunicado=?, mensagemTopo=?, arquivoBannerImagem=?, bannerMensagem=?, diasApresentarAntecendencia=?, mensagemTopoTexto=?, arquivoImagemTopo=?, dataDiaAntecedencia=? "
                    + " WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(dataComemorativaVO.getDataComemorativa()));
                    sqlAlterar.setString(2, dataComemorativaVO.getAssunto());
                    sqlAlterar.setString(3, dataComemorativaVO.getMensagem());
                    sqlAlterar.setString(4, dataComemorativaVO.getTipoDestinatarioDataComemorativa().toString());
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.CARGO)) {
                        sqlAlterar.setInt(5, dataComemorativaVO.getCargo().getCodigo());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL)) {
                        sqlAlterar.setInt(6, dataComemorativaVO.getAreaProfissional().getCodigo());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_CONHECIMENTO)) {
                        sqlAlterar.setInt(7, dataComemorativaVO.getAreaConhecimento().getCodigo());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.UNIDADE_ENSINO)) {
                        sqlAlterar.setInt(8, dataComemorativaVO.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.DEPARTAMENTO)) {
                        sqlAlterar.setInt(9, dataComemorativaVO.getDepartamento().getCodigo());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setString(10, dataComemorativaVO.getStatus().toString());
                    sqlAlterar.setBoolean(11, dataComemorativaVO.getComunicadoInterno().booleanValue());
                    sqlAlterar.setBoolean(12, dataComemorativaVO.getBannerComunicado().booleanValue());
                    sqlAlterar.setBoolean(13, dataComemorativaVO.getMensagemTopo().booleanValue());
                    if (dataComemorativaVO.getArquivoBannerImagem().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(14, dataComemorativaVO.getArquivoBannerImagem().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(14, 0);
                    }
                    sqlAlterar.setString(15, dataComemorativaVO.getBannerMensagem());
                    sqlAlterar.setInt(16, dataComemorativaVO.getDiasApresentarAntecendencia());
                    sqlAlterar.setString(17, dataComemorativaVO.getMensagemTopoTexto());
                    if (dataComemorativaVO.getArquivoImagemTopo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(18, dataComemorativaVO.getArquivoImagemTopo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(18, 0);
                    }
                    if (dataComemorativaVO.getDataDiaAntecedencia() != null) {
                        sqlAlterar.setDate(19, Uteis.getDataJDBC(dataComemorativaVO.getDataDiaAntecedencia()));
                    } else {
                        sqlAlterar.setNull(19, 0);
                    }
                    sqlAlterar.setInt(20, dataComemorativaVO.getCodigo());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDados(DataComemorativaVO dataComemorativaVO) throws ConsistirException {
        if (dataComemorativaVO.getDataComemorativa() == null) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_dataComemorativa"));
        }
        if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.UNIDADE_ENSINO)
                && dataComemorativaVO.getUnidadeEnsino().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_unidadeEnsino"));
        }
        if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_CONHECIMENTO)
                && dataComemorativaVO.getAreaConhecimento().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_areaConhecimento"));
        }
        if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL)
                && dataComemorativaVO.getAreaProfissional().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_areaProfissional"));
        }
        if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.CARGO)
                && dataComemorativaVO.getCargo().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_cargo"));
        }
        if (dataComemorativaVO.getTipoDestinatarioDataComemorativa().equals(TipoDestinatarioDataComemorativaEnum.DEPARTAMENTO)
                && dataComemorativaVO.getDepartamento().getCodigo() == 0) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_departamento"));
        }
        if (dataComemorativaVO.getAssunto().trim().isEmpty()) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_assunto"));
        }
        if (dataComemorativaVO.getMensagem().trim().isEmpty()) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_DataComemorativa_mensagem"));
        }
    }

    @Override
    public List<DataComemorativaVO> consultar(Date dataInicio, Date dataTermino, String assunto, TipoDestinatarioDataComemorativaEnum tipoDestinatarioDataComemorativaEnum,
            StatusAtivoInativoEnum status, Integer unidadeEnsino, Integer departamento, Integer cargo, Integer areaConhecimento,
            Integer areaProfissional, boolean validarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), validarAcesso, usuario);
        StringBuilder sb = new StringBuilder(getSqlSelectConsulta());
        sb.append(" WHERE 1=1 ");
        if (dataInicio != null && dataTermino != null) {
            sb.append(" and extract(month from dataComemorativa.dataComemorativa) >= ").append(Uteis.getMesData(dataInicio));
            sb.append(" and extract(day from dataComemorativa.dataComemorativa) >= ").append(Uteis.getDiaMesData(dataInicio));
            sb.append(" and extract(month from dataComemorativa.dataComemorativa) <= ").append(Uteis.getMesData(dataTermino));
            sb.append(" and extract(day from dataComemorativa.dataComemorativa) <= ").append(Uteis.getDiaMesData(dataTermino));
        } else if (dataInicio != null) {
            sb.append(" and extract(month from dataComemorativa.dataComemorativa) <= ").append(Uteis.getMesData(dataTermino));
            sb.append(" and extract(day from dataComemorativa.dataComemorativa) <= ").append(Uteis.getDiaMesData(dataTermino));
        } else if (dataTermino != null) {
            sb.append(" and extract(month from dataComemorativa.dataComemorativa) <= ").append(Uteis.getMesData(dataTermino));
            sb.append(" and extract(day from dataComemorativa.dataComemorativa) <= ").append(Uteis.getDiaMesData(dataTermino));
        }
        if (assunto != null && !assunto.trim().isEmpty()) {
            sb.append(" and upper(sem_acentos(assunto)) like(upper(sem_acentos('%").append(assunto).append("%'))) ");
        }
        if (tipoDestinatarioDataComemorativaEnum != null) {
            sb.append(" and tipoDestinatarioDataComemorativa = '").append(tipoDestinatarioDataComemorativaEnum.toString()).append("' ");
            if (tipoDestinatarioDataComemorativaEnum.equals(TipoDestinatarioDataComemorativaEnum.UNIDADE_ENSINO)
                    && unidadeEnsino != null && unidadeEnsino >= 0) {
                sb.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
            }
            if (tipoDestinatarioDataComemorativaEnum.equals(TipoDestinatarioDataComemorativaEnum.AREA_CONHECIMENTO)
                    && areaConhecimento != null && areaConhecimento > 0) {
                sb.append(" and areaConhecimento.codigo = ").append(areaConhecimento);
            }
            if (tipoDestinatarioDataComemorativaEnum.equals(TipoDestinatarioDataComemorativaEnum.AREA_PROFISSIONAL)
                    && areaProfissional != null && areaProfissional > 0) {
                sb.append(" and areaProfissional.codigo = ").append(areaProfissional);
            }
            if (tipoDestinatarioDataComemorativaEnum.equals(TipoDestinatarioDataComemorativaEnum.CARGO)
                    && cargo != null && cargo > 0) {
                sb.append(" and cargo.codigo = ").append(cargo);
            }
            if (tipoDestinatarioDataComemorativaEnum.equals(TipoDestinatarioDataComemorativaEnum.DEPARTAMENTO)
                    && departamento != null && departamento > 0) {
                sb.append(" and departamento.codigo = ").append(departamento);
            }
        }
        if (status != null) {
            sb.append(" and DataComemorativa.status = '").append(status.toString()).append("' ");
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtivacao(DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        dataComemorativaVO.setStatus(StatusAtivoInativoEnum.ATIVO);
        alterar(dataComemorativaVO, usuarioLogado, configuracaoGeralSistema);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarInativacao(DataComemorativaVO dataComemorativaVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        dataComemorativaVO.setStatus(StatusAtivoInativoEnum.INATIVO);
        alterar(dataComemorativaVO, usuarioLogado, configuracaoGeralSistema);
    }

    private List<DataComemorativaVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<DataComemorativaVO> dataComemorativaVOs = new ArrayList<DataComemorativaVO>(0);
        while (rs.next()) {
            dataComemorativaVOs.add(montarDados(rs));
        }
        return dataComemorativaVOs;
    }

    private DataComemorativaVO montarDados(SqlRowSet rs) throws Exception {
        DataComemorativaVO dataComemorativaVO = new DataComemorativaVO();
        dataComemorativaVO.setCodigo(rs.getInt("codigo"));
        dataComemorativaVO.getAreaConhecimento().setCodigo(rs.getInt("areaConhecimento"));
        dataComemorativaVO.getAreaConhecimento().setNome(rs.getString("areaConhecimento.nome"));
        dataComemorativaVO.getAreaProfissional().setCodigo(rs.getInt("areaProfissional"));
        dataComemorativaVO.getAreaProfissional().setDescricaoAreaProfissional(rs.getString("areaProfissional.descricaoAreaProfissional"));
        dataComemorativaVO.setAssunto(rs.getString("assunto"));
        dataComemorativaVO.setComunicadoInterno(rs.getBoolean("comunicadoInterno"));
        dataComemorativaVO.setBannerComunicado(rs.getBoolean("bannerComunicado"));
        dataComemorativaVO.setMensagemTopo(rs.getBoolean("mensagemTopo"));
        dataComemorativaVO.setBannerMensagem(rs.getString("bannerMensagem"));
        dataComemorativaVO.setDiasApresentarAntecendencia(rs.getInt("diasApresentarAntecendencia"));
        dataComemorativaVO.setMensagemTopoTexto(rs.getString("mensagemTopoTexto"));
        dataComemorativaVO.getCargo().setCodigo(rs.getInt("cargo"));
        dataComemorativaVO.getCargo().setNome(rs.getString("cargo.nome"));
        dataComemorativaVO.getDepartamento().setCodigo(rs.getInt("departamento"));
        dataComemorativaVO.getDepartamento().setNome(rs.getString("departamento.nome"));
        dataComemorativaVO.setDataComemorativa(rs.getDate("dataComemorativa"));
        dataComemorativaVO.setDataDiaAntecedencia(rs.getDate("dataDiaAntecedencia"));
        dataComemorativaVO.setMensagem(rs.getString("mensagem"));
        dataComemorativaVO.setStatus(StatusAtivoInativoEnum.valueOf(rs.getString("status")));
        dataComemorativaVO.setTipoDestinatarioDataComemorativa(TipoDestinatarioDataComemorativaEnum.valueOf(rs.getString("tipoDestinatarioDataComemorativa")));
        dataComemorativaVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
        dataComemorativaVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
        dataComemorativaVO.getArquivoBannerImagem().setNome(rs.getString("arqImagemBanner.nome"));
        dataComemorativaVO.getArquivoBannerImagem().setCodigo(rs.getInt("arqImagemBanner.codigo"));
        dataComemorativaVO.getArquivoBannerImagem().setPastaBaseArquivo(rs.getString("arqImagemBanner.pastaBaseArquivo"));
        dataComemorativaVO.getArquivoImagemTopo().setNome(rs.getString("arqImagemTopo.nome"));
        dataComemorativaVO.getArquivoImagemTopo().setCodigo(rs.getInt("arqImagemTopo.codigo"));
        dataComemorativaVO.getArquivoImagemTopo().setPastaBaseArquivo(rs.getString("arqImagemTopo.pastaBaseArquivo"));
        dataComemorativaVO.setNovoObj(false);
        return dataComemorativaVO;
    }

    private String getSqlSelectConsulta() {
        StringBuilder sb = new StringBuilder("");
        sb.append(" SELECT DataComemorativa.*, unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
        sb.append(" cargo.nome as \"cargo.nome\", departamento.nome as \"departamento.nome\", ");
        sb.append(" arqImagemTopo.codigo as \"arqImagemTopo.codigo\", arqImagemTopo.pastaBaseArquivo as \"arqImagemTopo.pastaBaseArquivo\", arqImagemTopo.nome as \"arqImagemTopo.nome\", ");
        sb.append(" arqImagemBanner.codigo as \"arqImagemBanner.codigo\", arqImagemBanner.pastaBaseArquivo as \"arqImagemBanner.pastaBaseArquivo\", arqImagemBanner.nome as \"arqImagemBanner.nome\", ");
        sb.append(" areaConhecimento.nome as \"areaConhecimento.nome\", areaProfissional.descricaoAreaProfissional as \"areaProfissional.descricaoAreaProfissional\" ");
        sb.append(" from DataComemorativa ");
        sb.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = DataComemorativa.unidadeEnsino  ");
        sb.append(" left join Cargo on Cargo.codigo = DataComemorativa.cargo  ");
        sb.append(" left join Departamento on Departamento.codigo = DataComemorativa.departamento  ");
        sb.append(" left join areaConhecimento on areaConhecimento.codigo = DataComemorativa.areaConhecimento  ");
        sb.append(" left join areaProfissional on areaProfissional.codigo = DataComemorativa.areaProfissional  ");
        sb.append(" left join arquivo arqImagemTopo on arqImagemTopo.codigo = DataComemorativa.arquivoImagemTopo  ");
        sb.append(" left join arquivo arqImagemBanner on arqImagemBanner.codigo = DataComemorativa.arquivoBannerImagem  ");
        return sb.toString();
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PaizVO</code> através de sua chave primária.
     * 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DataComemorativaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = getSqlSelectConsulta() + " WHERE DataComemorativa.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados(Data Comemorativa).");
        }
        return (montarDados(tabelaResultado));
    }

    public List consultarDataComemorativaDataAtualTipoMensagemTopo() throws Exception {
        String sql = getSqlSelectConsulta() + " WHERE mensagemTopo = true and datacomemorativa.status = 'ATIVO' and DataComemorativa.dataComemorativa >= '" + Uteis.getDataJDBC(Uteis.gerarDataDiaMesAno( Uteis.getDiaMesData(new Date()), Uteis.getMesData(new Date()), 1970)) + "' and DataComemorativa.dataDiaAntecedencia <= '" +  Uteis.getDataJDBC(Uteis.gerarDataDiaMesAno( Uteis.getDiaMesData(new Date()), Uteis.getMesData(new Date()), 1970)) + "' order by dataComemorativa.dataComemorativa";
        List lista = montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
        DataComemorativaVO dt = new DataComemorativaVO();
        dt.setArquivoImagemTopo(null);
        dt.setCodigo(0);
        dt.setAssunto("resources/V2/imagens/logo-sei-otimize.png");
        dt.setDataComemorativa(new Date());
        lista.add(dt);
        return lista;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DataComemorativa.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com
     * objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        DataComemorativa.idEntidade = idEntidade;
    }
}
