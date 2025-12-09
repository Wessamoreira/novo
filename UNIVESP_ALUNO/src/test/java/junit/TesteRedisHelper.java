package junit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import controle.arquitetura.RedisService;
import negocio.comuns.basico.CidadeVO;

@Component
public class TesteRedisHelper {

    @Autowired
    private RedisService redisService;

    public void gravarCidade9999() {
        CidadeVO cidade = new CidadeVO();
        cidade.setCodigo(99988);
        cidade.setNome("Cidade Teste 99988");

        String chave = "app:cidade:" + cidade.getCodigo();
        redisService.setObjeto(chave, cidade); 

        System.out.println("Gravou no Redis a chave " + chave);
    }
    
    
    
    
}