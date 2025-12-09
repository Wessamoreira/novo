/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author RODRIGO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.LogVO; @Controller(value = "LogControle")
@Lazy
@Scope(value="session")
public class LogControle extends SuperControle implements Serializable {

    private List<File> arquivos;
    private List<LogVO> logVOs;
    private String textoLog;    
    private String nivelLog;
    private String entidade;
    private String nomeUsuario;
    private String data;

    public LogControle() {
    }

    public String obterCaminhoPastaLog() {
        return System.getProperty("user.home") + File.separator + "logs";
    }

    @PostConstruct
    public void montarListaArquivo() {
        setArquivos(new ArrayList<File>());
        File diretorioRaiz = new File(obterCaminhoPastaLog());
        if (diretorioRaiz.exists()) {
            File[] logs = diretorioRaiz.listFiles();
            for (File log : logs) {
                getArquivos().add(log);
            }
        }
    }

    public String getData() {
        if(data == null){
            data = "";
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEntidade() {
        if(entidade == null){
            entidade = "";
        }
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getNomeUsuario() {
        if(nomeUsuario == null){
            nomeUsuario = "";
        }
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }





    public String getNivelLog() {
        if(nivelLog == null){
            nivelLog = "";
        }
        return nivelLog;
    }

    public void setNivelLog(String nivelLog) {
        this.nivelLog = nivelLog;
    }



    public List<File> getArquivos() {
        if (arquivos == null) {
            arquivos = new ArrayList<File>();
        }
        return arquivos;
    }

    public void setArquivos(List<File> arquivos) {
        this.arquivos = arquivos;
    }

    public String getTextoLog() {
        if (textoLog == null) {
            textoLog = "";
        }
        return textoLog;
    }

    public void setTextoLog(String textoLog) {
        this.textoLog = textoLog;
    }

    public void realizarVisualizacaoArquivo() throws IOException {
        try {
            setLogVOs(null);
            File log = (File) context().getExternalContext().getRequestMap().get("log");
            textoLog = "";
            //File arquivoLog = new File(obterCaminhoPastaLog() + File.separator + log);
            if (log.exists()) {
                BufferedReader fr = new BufferedReader(new FileReader(log));
                int x = 0;
                String text = "";
                while (x == 0) {
                    text = fr.readLine();
                    if (text == null) {
                        x = 1;
                    } else {
                        String data = getDataTexto(text);
                        if (data == null) {
                            adicionarMensagemTexto(getMensagemTexto(text, false));
                        } else {
                            adicionarTexto(data, getTipoLogTexto(text), getEntidadeLogTexto(text), getUsuarioTexto(text), getMensagemTexto(text, true));
                        }
                        //textoLog += "\n" + text;
                    }
                }
                fr.close();
                fr = null;
            }
        } catch (FileNotFoundException ex) {
            setMensagemDetalhada("msg_erro", ex.getLocalizedMessage());
        }
    }

    public String getDataTexto(String texto) {
        try {
            String data = texto.substring(0, 15).trim();
            if (data.contains("Jan") || data.contains("Fev") || data.contains("Mar") || data.contains("Abr") || data.contains("Mai")
                    || data.contains("Jun") || data.contains("Jul") || data.contains("Ago") || data.contains("Set") || data.contains("Out") || data.contains("Nov")
                    || data.contains("Dez")) {
                return data;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getUsuarioTexto(String texto) {
        try {
            return texto.substring(texto.indexOf("Usuario - ")+10, texto.indexOf(")") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public String getTipoLogTexto(String texto) {
        try {
            return texto.substring(16, 21);
        } catch (Exception e) {
            return "";
        }
    }

    public String getEntidadeLogTexto(String texto) {
        try {
//            if (texto.contains("Usuário - ")) {
//                return texto.substring(22, texto.indexOf("Usuário - "));
//            }
            int y = 22;
            int x = 22;
            while (x <= y) {
                x += texto.substring(x).indexOf(" ");
                if (x > y + 1) {
                    return texto.substring(y, x);
                } else {
                    x++;
                    y = x;
                    
                }
            }
            return texto.substring(22);
        } catch (Exception e) {
            return "";
        }
    }

    public int getColumnLog(){
        if(getArquivos().size() > 10){
            return 10;
        }
        return getArquivos().size();
    }

    public int getElementLog(){
        if(getArquivos().size() > 10){
            return 10;
        }
        return getArquivos().size();
    }

    public String getMensagemTexto(String texto, boolean possuiData) {
        try {
            if (possuiData) {
                return texto.substring(texto.indexOf(")") + 1);
            }
            return texto;
        } catch (Exception e) {
            return "";
        }
    }

    public void adicionarTexto(String data, String nivelLog, String entidade, String usuario, String mensagem) {
        getLogVOs().add(new LogVO(data, entidade, usuario, nivelLog, mensagem));
    }

    public void adicionarMensagemTexto(String texto) {
        if (getLogVOs().size() > 0) {
            LogVO logVO = getLogVOs().get(getLogVOs().size() - 1);
            logVO.setMensagem(logVO.getMensagem() + "\n" + texto);
        }
    }

    public List<LogVO> getLogVOs() {
        if (logVOs == null) {
            logVOs = new ArrayList<LogVO>(0);
        }
        return logVOs;
    }

    public void setLogVOs(List<LogVO> logVOs) {
        this.logVOs = logVOs;
    }
}
