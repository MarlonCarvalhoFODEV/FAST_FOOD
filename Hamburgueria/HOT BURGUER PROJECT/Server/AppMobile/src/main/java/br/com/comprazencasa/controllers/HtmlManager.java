package br.com.comprazencasa.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("Pages")
public class HtmlManager {

    private Gson gson ;

    @Autowired
    public HtmlManager(){
        this.gson = new Gson();
    }

    @GetMapping(path = "/politica-de-privacidade")
    public String getPoliticadePrivacidade(){
        return " \n" +
                "<h1><span style=\"color: #000000;\">Política de privacidade</span></h1>\n" +
                "<h1><span style=\"color: #666699;\">Senha</span></h1>\n" +
                "<h3><strong>Toda conta criada no C.Z.C tem sua senha criptografada, verificação de segurnça de acesso e nossos servidores, tecnologia contra fraudes.</strong></h3>\n" +
                "<h1><img class=\"  wp-image-22 aligncenter\" style=\"font-size: 16px; font-weight: 400;\" src=\"https://desenhosdanetblog.files.wordpress.com/2019/02/passoword.png\" alt=\"passoword.png\" width=\"163\" height=\"57\" /></h1>\n" +
                "<h1><span style=\"color: #666699;\">Endereço</span></h1>\n" +
                "<h3><strong>O endereço cadastrado é utilizado somente na czc, não é passada para sistemas de terceiros ou empresas externas (Exceto CPF). Dessa forma, garantimos a confidencialidade não só do endereço, como as demais informações.</strong></h3>\n" +
                "<img class=\"  wp-image-23 aligncenter\" style=\"font-size: 16px; font-weight: 400;\" src=\"https://desenhosdanetblog.files.wordpress.com/2019/02/untitled-3.png\" alt=\"Untitled-3.png\" width=\"57\" height=\"72\" />\n" +
                "<h1><span style=\"color: #666699;\">CPF</span></h1>\n" +
                "<h3><strong>Seu cadastro de pessoa fisica será passado para outra empresa caso queira efetuar o pagamento dos seus pedidos Pelo APP CompraZen Casa, é necessário para segurança dos nossos clientes. </strong></h3>\n" +
                "<h1><img class=\"  wp-image-24 aligncenter\" style=\"font-size: 16px; font-weight: 400;\" src=\"https://desenhosdanetblog.files.wordpress.com/2019/02/untitled-4.png\" alt=\"Untitled-4.png\" width=\"113\" height=\"84\" /></h1>\n" +
                "<h1><span style=\"color: #666699;\">Sistema de Pagamento Online</span></h1>\n" +
                "<h3><strong>O sistema de pagamento implementado é de alta segurança por ser de uma empresa que atua há mais de 17 anos no mercado além de possuir mais de 96 milhões de usuários ativos em seus sistemas. Buscamos o melhor de fora pra dentro do seu Smartphone.</strong></h3>\n" +
                "<img class=\"  wp-image-26 aligncenter\" src=\"https://desenhosdanetblog.files.wordpress.com/2019/02/fgsgfsgfdfdg.png\" alt=\"fgsgfsgfdfdg.png\" width=\"118\" height=\"80\" />\n" +
                "<h2 style=\"text-align: left;\"><span style=\"color: #666699;\"><strong>Exportação de Informações</strong></span></h2>\n" +
                "<ul>\n" +
                "\t<li>Nossa plataforma contem um sistema integrado para avaliar o desempenho do usuário em nossa loja. Por isso é necessário que o GOOGLE faça a coleta de alguns dados do dispositivo, não só para análise, como também para obter informações do desempenho do usuário em nossa loja. Dessa forma está assegurada todas as informações requeridas pelo Google. Algumas das informações requeridas pelo Google são para fins publicitário, por isso, ao concordar com os termos de uso e políticas de privacidade o usuário declara estar ciente dessas informações.</li>\n" +
                "</ul>\n" +
                " ";
    }
}
