package negocio.facade.jdbc.administrativo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;


import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.IntegracaoSymplictyInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoSymplicty extends ControleAcesso implements IntegracaoSymplictyInterfaceFacade{

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;


	public String gerarArquivoTxt(ConfiguracaoGeralSistemaVO config) throws Exception {
	    SqlRowSet rowSet = getFacadeFactory().getRelatorioSeiDecidirFacade().consultaIntegracaoSymplicty();
	    String nomeArquivo = "Univesp_alunos_exalunos.txt";
	    String diretorioPath = config.getLocalUploadArquivoTemp() + "/symplicty"; 
	    File diretorio = new File(diretorioPath);
	    if (!diretorio.exists()) {
	        diretorio.mkdirs();
	    }
	    File arquivo = new File(diretorio, nomeArquivo);
	    StringBuilder conteudoArquivo = new StringBuilder();
	    conteudoArquivo.append("ID do Aluno|Usuario|Nome completo|Primeiro Nome|Sobrenome|CPF|E-mail|E-mail Institucional|Telefone|Telefone Secundario|Data de Nascimento|G\u00eanero|Endere\u00e7o Atual|Cidade Atual|Estado Atual|CEP Atual|Pa\u00eds Atual|Egresso|Tipo de Candidato|Conta desativada|Institui\u00e7\u00e3o|Forma\u00e7\u00e3o Principal|ID \u00danico da Forma\u00e7\u00e3o|N\u00famero de matr\u00edcula|Situa\u00e7\u00e3o Acad\u00eamica|Semestre do Aluno|Campus|N\u00edvel de Forma\u00e7\u00e3o|Curso|\u00c1rea de conhecimento|Data de Formatura");
	    conteudoArquivo.append("\n");
	    while (rowSet.next()) {
	        String linha = 
	            (rowSet.getString("ID_ALUNO") == null ? "" : rowSet.getString("ID_ALUNO")) + "|" +
	            (rowSet.getString("USUARIO") == null ? "" : rowSet.getString("USUARIO")) + "|" +
	            (rowSet.getString("NOME_COMPLETO") == null ? "" : rowSet.getString("NOME_COMPLETO")) + "|" +
	            (rowSet.getString("PRIMEIRO_NOME") == null ? "" : rowSet.getString("PRIMEIRO_NOME")) + "|" +
	            (rowSet.getString("SOBRENOME") == null ? "" : rowSet.getString("SOBRENOME")) + "|" +
	            (rowSet.getString("CPF") == null ? "" : rowSet.getString("CPF")) + "|" +
	            (rowSet.getString("EMAIL") == null ? "" : rowSet.getString("EMAIL")) + "|" +
	            (rowSet.getString("EMAIL_INSTITUCIONAL") == null ? "" : rowSet.getString("EMAIL_INSTITUCIONAL")) + "|" +
	            (rowSet.getString("TELEFONE") == null ? "" : rowSet.getString("TELEFONE")) + "|" +
	            (rowSet.getString("TELEFONE_SECUNDARIO") == null ? "" : rowSet.getString("TELEFONE_SECUNDARIO")) + "|" +
	            (rowSet.getString("DATA_NASCIMENTO") == null ? "" : rowSet.getString("DATA_NASCIMENTO")) + "|" +
	            (rowSet.getString("GENERO") == null ? "" : rowSet.getString("GENERO")) + "|" +
	            (rowSet.getString("ENDERECO_ATUAL") == null ? "" : rowSet.getString("ENDERECO_ATUAL")) + "|" +
	            (rowSet.getString("CIDADE_ATUAL") == null ? "" : rowSet.getString("CIDADE_ATUAL")) + "|" +
	            (rowSet.getString("ESTADO_ATUAL") == null ? "" : rowSet.getString("ESTADO_ATUAL")) + "|" +
	            (rowSet.getString("CEP_ATUAL") == null ? "" : rowSet.getString("CEP_ATUAL")) + "|" +
	            (rowSet.getString("PAIS_ATUAL") == null ? "" : rowSet.getString("PAIS_ATUAL")) + "|" +
	            (rowSet.getString("EGRESSO") == null ? "" : rowSet.getString("EGRESSO")) + "|" +
	            (rowSet.getString("TIPO_CANDIDATO") == null ? "" : rowSet.getString("TIPO_CANDIDATO")) + "|" +
	            (rowSet.getString("CONTA_DESATIVADA") == null ? "" : rowSet.getString("CONTA_DESATIVADA")) + "|" +
	            (rowSet.getString("INSTITUICAO") == null ? "" : rowSet.getString("INSTITUICAO")) + "|" +
	            (rowSet.getString("FORMACAO_PRINCIPAL") == null ? "" : rowSet.getString("FORMACAO_PRINCIPAL")) + "|" +
	            (rowSet.getString("ID_UNICO_FORMACAO") == null ? "" : rowSet.getString("ID_UNICO_FORMACAO")) + "|" +
	            (rowSet.getString("NUMERO_MATRICULA") == null ? "" : rowSet.getString("NUMERO_MATRICULA")) + "|" +
	            (rowSet.getString("SITUACAO_ACADEMICA") == null ? "" : rowSet.getString("SITUACAO_ACADEMICA")) + "|" +
	            (rowSet.getString("SEMESTRE_ALUNO") == null ? "" : rowSet.getString("SEMESTRE_ALUNO")) + "|" +
	            (rowSet.getString("CAMPUS") == null ? "" : rowSet.getString("CAMPUS")) + "|" +
	            (rowSet.getString("NIVEL_FORMACAO") == null ? "" : rowSet.getString("NIVEL_FORMACAO")) + "|" +
	            (rowSet.getString("CURSO") == null ? "" : rowSet.getString("CURSO")) + "|" +
	            (rowSet.getString("AREA_CONHECIMENTO") == null ? "" : rowSet.getString("AREA_CONHECIMENTO")) + "|" +
	            (rowSet.getString("DATA_FORMATURA") == null ? "" : rowSet.getString("DATA_FORMATURA"));
	        conteudoArquivo.append(linha);
	        conteudoArquivo.append("\n");
	    }
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
	        writer.write(conteudoArquivo.toString());
	        return arquivo.getAbsolutePath(); 
	    } catch (IOException e) {
	        return null;
	    }
	}


	public Boolean enviarArquivoSFTP(String caminhoArquivo, ConfiguracaoGeralSistemaVO config) throws Exception {
		String pastaDestinoRemota = config.getPastaDestinoRemotaSymplicty();
		String host = config.getHostIntegracaoSistemaSymplicty();
		int port = config.getPortIntegracaoSistemaSymplicty();
		String usuario = config.getUserIntegracaoSistemaSymplicty();
		String senha = config.getPassIntegracaoSistemaSymplicty();
		com.jcraft.jsch.Session session = null;
		ChannelSftp sftpChannel = null;
		try {
			File arquivo = new File(caminhoArquivo);
			if (!arquivo.exists() || !arquivo.isFile()) {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty(
						"O arquivo '" + caminhoArquivo + "' Não existe ou não é válido.", false);
				return false;
			}
			JSch jsch = new JSch();
			session = jsch.getSession(usuario, host, port);
			session.setPassword(senha);

			Properties configuracao = new Properties();
			configuracao.put("StrictHostKeyChecking", "no");
			configuracao.put("PreferredAuthentications", "password");
			session.setConfig(configuracao);
			session.connect(5000);

			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;

			if (pastaDestinoRemota != null && !pastaDestinoRemota.isEmpty()) {
				try {
					sftpChannel.cd(pastaDestinoRemota);
				} catch (SftpException e) {
				}
			}

			String nomeArquivoRemoto = arquivo.getName();
			boolean arquivoExistente = false;
			try {
				sftpChannel.lstat(nomeArquivoRemoto);
				arquivoExistente = true;
			} catch (SftpException e) {
			}

			if (arquivoExistente) {
				try {
					sftpChannel.put(new FileInputStream(arquivo), nomeArquivoRemoto, ChannelSftp.OVERWRITE);
					getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty(
							"Arquivo '" + nomeArquivoRemoto + "' substituído com sucesso.", true);
				} catch (SftpException | FileNotFoundException e) {
					getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty(
							"Erro ao substituir o arquivo '" + nomeArquivoRemoto + "': " + e.getMessage(), false);
					return false;
				}
			} else {
				try {
					sftpChannel.put(new FileInputStream(arquivo), nomeArquivoRemoto);
					getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Arquivo '" + nomeArquivoRemoto + "' enviado com sucesso.", true);
				} catch (SftpException | FileNotFoundException e) {
					getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty(
							"Erro ao enviar o arquivo '" + nomeArquivoRemoto + "': " + e.getMessage(), false);
					return false;
				}
			}

			return true;
		} catch (JSchException e) {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Erro de conexão com o SFTP: " + e.getMessage(), false);
		} catch (Exception e) {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Erro desconhecido: " + e.getMessage(), false);
		} finally {
			try {
				if (sftpChannel != null) {
					sftpChannel.disconnect();
				}
				if (session != null) {
					session.disconnect();
				}
			} catch (Exception e) {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Erro ao desconectar do SFTP: " + e.getMessage(), false);
			}
		}

		return false;
	}

	public void executarJobSymplicty() {
        try {
            ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
            String arquivo = getFacadeFactory().getIntegracaoSymplictyInterfaceFacade().gerarArquivoTxt(config);

            if (arquivo != null) {
                boolean sucessoEnvio = getFacadeFactory().getIntegracaoSymplictyInterfaceFacade().enviarArquivoSFTP(arquivo, config);
                if (sucessoEnvio) {
                	getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("", true);
                } else {
                	getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Erro no envio do arquivo", false);
                }
            } else {
            	getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Falha ao gerar arquivo", false);
            }


        } catch (Exception e) {
            e.printStackTrace();
            try {
            	getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluirLogJobSymplicty("Erro inesperado: " + e.getMessage(), false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
