/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.comprazencasa.database;

import apis.pagarme.transaction.Payment;
import br.com.comprazencasa.util.RequestWS;
import br.com.comprazencasa.util.SendImage;
import br.com.hotburguer.daos.DaoHotAddress;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlon
 */
public class NewClass {

    public static void main(String args[]) throws Exception {

        //DaoOrder dao = new DaoOrder();

        //dao.saveOrderProducts2(5,"dsadasddsa");
        //DaoRegions dao = new DaoRegions();
        //Street street = dao.getStreetFromCEP("25540020");
        //System.out.println(street.getName());

        //CommunicationServer server = new CommunicationServer(5589);
        //server.start();
        //CollectProducts col = new CollectProducts();
        //col.getAllProductsFromMarket("Guanabara/");
        //String json = requestJson();
        //System.out.println(json);

        /*CollaboratorAccount account = new CollaboratorAccount();
        account.setCpf("00000000");
        account.setDate_birth("0000");
        account.setName("000");
        account.setLastName("00000");
        account.getLogin().setEmail("000000");
        account.getLogin().setPassword(CryptoUtil.encryptMD5("00000".getBytes()));
        account.getContact().setCel("");
        account.getContact().setEmail(account.getLogin().getEmail());
        account.getAddress().setState("Rio de Janeiro");
        account.getAddress().setCity("São João de Meriti");
        account.getAddress().setNeighborhood("Grande Rio");
        account.getAddress().setStreet("Rua João Alberto");
        account.getAddress().setResidenceNumber(00);
        account.getAddress().setComplement("000");
        account.getFunction().setCompanyFunction(CompanyFunction.BUYER);
        account.getFunction().setSalary(1000);
        account.getFunction().setResponsability("Responsável por ativar produtos inativos, comprar pedidos e empacotalos. Além da responsabilidade de manter as sacolas.");

        new DaoCollaborators().register(account);*/

        /*try {
            String sql = "SELECT * FROM czc_products prod " +
            "                    jOIN rate_services rate " +
                    "                    WHERE prod.id > 0 " +
                    "                    AND prod.categorie!='' " +
                    "                    AND status ='inactive'" +
                    "                    AND rate.date=(select date from rate_services order by date DESC limit 1) "+
                    "                    ORDER BY prod.id ASC LIMIT 7000 ";

            Connection con = ConnectionDB_APP.connectIntern();
            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            int counter  = 0 ;
            DaoCzc dao = new DaoCzc();
            while(rs.next()){
                final ITProduct p = new ITProduct();
                p.setId(rs.getInt("prod.id"));
                p.setPathImg(rs.getString("prod.path_img"));
                p.setPathPromo(rs.getString("prod.path_promo"));
                p.setTitle(rs.getString("prod.title"));
                p.setInPrice(rs.getDouble("prod.in_price"));
                p.setPerPrice(rs.getDouble("prod.per_price"));
                p.setInstalments(rs.getString("prod.instalments"));
                p.setCategorie(rs.getString("prod.categorie"));

                if(dao.existProductDB(p.getTitle()) == null && !p.getTitle().contains("carrefour")
                        && !p.getTitle().contains("Carrefour")){
                    System.out.println("PRODUTO NÂO EXISTE: "+p.getTitle()+" - CADASTRADO");

                    p.setStatus("inactive");
                    dao.insertProductTESSSSSS(p);
                }

                counter++;
                System.out.println("CONTADOR: "+counter);
            }

            pst.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DaoCzc.class.getName()).log(Level.SEVERE, null, ex);
        */

        //List<DownImg> products = new ArrayList<>();
        //products.add(new DownImg(7471,"Leite em Pó Molico Desnatado Vit A&D 280g","https://www.paodeacucar.com/img/uploads/1/898/541898x200x200.jpg"));
        //products.add(new DownImg(7474,"Leite em Pó Int Inst Ninho Lata 400g","https://cdn1.staticpanvel.com.br/produtos/15/128198-15.jpg?"));
        //products.add(new DownImg(7476,"Cereal Snow Flakes Nestlé 300g","https://www.deliveryextra.com.br/img/uploads/1/958/571958.jpg"));
        //products.add(new DownImg(7478,"Leite longa vida Italac 1l","https://http2.mlstatic.com/atacado-c6-leite-uht-integral-italac-1lt-D_NQ_NP_730954-MLB28056179484_082018-F.jpg"));
        //products.add(new DownImg(7683,"Achocolatado em Pó Nescau 2.0 Sachê 800g","https://static.carrefour.com.br/medias/sys_master/images/images/h7e/h5f/h00/h00/9234120146974.jpg"));
        //products.add(new DownImg(7696,"Leite em Pó Ninho Instantâneo Sachê 800g","https://http2.mlstatic.com/leite-ninho-sache-800g-promoco-D_NQ_NP_652952-MLB26923780372_022018-F.jpg"));
        //products.add(new DownImg(7690,"Café Pimpinela Golden 500g","https://www.zonasul.com.br/Content/images/imagem536x536/2018119_76893.jpg"));
        //products.add(new DownImg(7546,"Fralda Babysec Ultra Jumbinho (Exc. RN)","https://imgmanagercb-a.akamaihd.net/fralda-para-bebe/babysec-galinha-pintadinha-ultrasec-g-descartavel-38-unidades_300x300-PUa16da_1.jpg"));
        //products.add(new DownImg(7548,"Fralda Desc. Pampers Supersec Econômina","https://drogariasp.vteximg.com.br/arquivos/ids/203490-500-500/Fralda-Descartavel-Pampers-Supersec-Pacotao-G-26-Unidades-Drogaria-SP-609080.jpg"));
        //products.add(new DownImg(7552,"Batata Palha Lets Trad 120g","http://img.sitemercado.com.br/produtos/f064840803bffe234a62c367cf53d14a0456d6684767cf2f23748761120e5617_full.jpg"));
        //products.add(new DownImg(7553,"Azeite Extra Virgem Andorinha 500ml","https://mambo.vteximg.com.br/arquivos/ids/208746/139877.jpg?"));
        //products.add(new DownImg(7558,"Caldo Maggi 57g","https://www.nestle.com.br/images/default-source/produtos/maggi-caldo-sabor-galinha8b7c578ac0bf49649584881749eccd4c.png"));
        //products.add(new DownImg(7559,"Creme de Leite Nestlé TP 200g","https://www.deliveryextra.com.br/img/uploads/1/436/521436.jpg"));
        //products.add(new DownImg(7560,"Leite Condensado Italac TP 395g","https://savegnago.vteximg.com.br/arquivos/ids/285052-1000-1000/LEITE-CONDENSADO-ITALAC-395G-TP.jpg"));
        //products.add(new DownImg(7561,"Leite Condensado Moça Nestle Lata 395g","https://http2.mlstatic.com/18-leite-condensado-moca-nestle-lata-395g-D_NQ_NP_980077-MLB26340309344_112017-F.jpg"));
        //products.add(new DownImg(7562,"Leite Condensado Moça Nestle TP 395g","https://savegnago.vteximg.com.br/arquivos/ids/278143-1000-1000/LEITE-CONDENSADO-MOCA-395G-TP.jpg0"));
        //products.add(new DownImg(7564,"maionese hellmann\\'s tradicional 500g","https://redeultramercado3.index.com.br/redeultramercado/fotos/g331875.jpg"));
        //products.add(new DownImg(7566,"polpa de tomate pomodoro tp 520","https://www.paodeacucar.com/img/uploads/1/189/565189.png"));
        //products.add(new DownImg(7696,"Leite em Pó Ninho Instantâneo Sachê 800g","https://coopsp.vteximg.com.br/arquivos/ids/160621-804-804/7891000071397_Leite-em-po-instantaneo-Ninho-sache---800g.jpg?"));
       // products.add(new DownImg(14253,"Leite Longa Vida UHT Inte Ninho TP Litro","https://cdn-cosmos.bluesoft.com.br/products/7891000066560"));
        //products.add(new DownImg(7474,"Leite em Pó Int Inst Ninho Lata 400g","https://coopsp.vteximg.com.br/arquivos/ids/160620-804-804/7891000142202_Leite-em-po-instantaneo-Ninho---400g-copiar.jpg?"));
        //products.add(new DownImg(7476,"Cereal Snow Flakes Nestlé 300g","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1j6m1ZNNMyFjrHPk0VSJ829RphlJ10rMpVqwjLEEC1YVRhz_f"));
        //products.add(new DownImg(14247,"toalhas umedecidos baby looney tunes c/48","https://a-static.mlcdn.com.br/618x463/toalhas-umedecidas-looney-tunes-c-48/ruthfraldas/90737/05e6f6cb3ef97a2d9bbc048712e04394.jpg"));
        //products.add(new DownImg(7429,"biscoito richester cream cracker 200g","http://jtgalimentos.com.br/loja/img/p/5/3/9/539-large_default.jpg"));
        //products.add(new DownImg(7558,"Caldo Maggi 27g","https://www.nestle.com.br/images/default-source/produtos/maggi-caldo-sabor-galinha8b7c578ac0bf49649584881749eccd4c.png"));
        //roducts.add(new DownImg(7565,"Molho refogado pramesa sachê 340g","https://superprix.vteximg.com.br/arquivos/ids/176096-600-600/Molho-de-Tomate-Pramesa-Refogado-340g--Sache-.png?v=636421849688070000"));
        //products.add(new DownImg(7567,"Azeite Gallo Extra Virgem 500ml","https://www.deliveryextra.com.br/img/uploads/1/664/570664.png?type=product"));
        //products.add(new DownImg(7583,"Arroz parboilizado maximo 5kg","https://img.angeloni.com.br/files/produtos/4030616/4030616_1_large.jpg"));
        //products.add(new DownImg(7582,"Arroz agulhinha maximo 5kg","https://redeultramercado3.index.com.br/redeultramercado/fotos/g340990.jpg"));
        //products.add(new DownImg(7592,"ketchup pramesa 400g","http://img.sitemercado.com.br/produtos/ecbcb2c5017171ef69cb92082403cea74923e8f6f58ef18c110d1aa303d3845e_full.jpg"));
        //products.add(new DownImg(7899,"Atum ralado gomes da costa 170g","https://www.deliveryextra.com.br/img/uploads/1/827/565827.png"));
        //products.add(new DownImg(7913,"Mistura lactea triangulo mineiro lt 395g","https://muffatosupermercados.vteximg.com.br/arquivos/ids/241250-400-400/7896434920778.jpg?v=636704388780200000"));
        //products.add(new DownImg(14248,"molho de tomate heinz trad. sachê 340g","https://mambo.vteximg.com.br/arquivos/ids/218896/190371_Molho-de-Tomate-Tradicional-Heinz-340g.jpg?v=636531838025030000"));
        //products.add(new DownImg(14250,"mostarda pramesa 180g","http://img.sitemercado.com.br/produtos/ad1d623da4212b9f898c39983ee823bbdb01f59b8a52c062ca405c382619963e_full.jpg"));
        //products.add(new DownImg(7543,"lasanha rezende 600g","https://savegnago.vteximg.com.br/arquivos/ids/267373-1000-1000/figura-1frente.jpg"));
        //products.add(new DownImg(7577,"Iogurte grego danone 100g","https://www.paodeacucar.com/img/uploads/1/405/551405.png?type=product"));
        //products.add(new DownImg(7824,"hamburguer texas burger seara 672g","https://superprix.vteximg.com.br/arquivos/ids/168061-600-600/Hamburguer-Seara-Texas-Burger-672g.jpg?v=636093984436830000"));
        //products.add(new DownImg(7838,"batata palito lar 2kg","http://img.sitemercado.com.br/produtos/a173bb5972b35ae07397d68a56d8be1ebb07deb10703b7b4f5f0e3529f3bb6dd_full.jpg"));
        //products.add(new DownImg(14251,"queijo muçarela peça ou pedaço","https://www.carrinhodepromocoes.com.br/img-prod/120717145528707.jpg"));
        //products.add(new DownImg(7532,"refrigerante pepsi 2+250ml","https://www.paodeacucar.com/img/uploads/1/776/486776.jpg"));
        //products.add(new DownImg(7534,"Concentrado Xarope Guaracamp","https://http2.mlstatic.com/xarope-de-guarana-natural-concentrado-guaracamp-1-litro-D_NQ_NP_753388-MLB26778859855_022018-F.jpg"));
        //products.add(new DownImg(7542,"refresco em pó tang 25g ou 30g","https://coopsp.vteximg.com.br/arquivos/ids/158996-804-804/7622300861872_Refresco-em-po-Tang-laranja-com-mamao---25g.jpg"));
        //products.add(new DownImg(7766,"agua mineral da montanha 1,5l s/gás","https://www.redeultramercado.com.br/redeultramercado/fotos/Agua-mineral-da-montanha-510ml-1516268998__334724.jpg"));
        //products.add(new DownImg(7799,"vodka smirnoff red 998ml","https://static.carrefour.com.br/medias/sys_master/images/images/h1c/h97/h00/h00/9224718516254.jpg"));
        //products.add(new DownImg(14243,"cerveja therezópolis gold 600ml","https://cdn.cervejaexpress.com.br/media/catalog/product/cache/2/image/1000x1000/9df78eab33525d08d6e5fb8d27136e95/t/h/therezopolis_gold.jpg"));
        //products.add(new DownImg(14244,"cerveja therezópolis weiss 600ml","https://static.carrefour.com.br/medias/sys_master/images/images/h5d/h8d/h00/h00/9259438899230.jpg"));
        //products.add(new DownImg(14245,"cerveja therezópolis rubine 600ml","https://superprix.vteximg.com.br/arquivos/ids/172173-600-600/Cerveja-Therezopolis-Rubine-600ml.jpg"));
        //products.add(new DownImg(14246,"Cerveja bohemia latão 473ml","https://http2.mlstatic.com/cerveja-bohemia-pack-com-12-latas-de-473ml-lato-barato-D_NQ_NP_691435-MLB27694922097_072018-F.jpg"));
        //products.add(new DownImg(7463,"linguiça suina alegra","http://www.alegrafoods.com.br/wp-content/uploads/2017/10/linguica-toscana.png"));
        //products.add(new DownImg(7668,"alcatra bovina a vacuo","http://img.sitemercado.com.br/produtos/5ab9eec0035a21351476b255655e5b83efc0b8e0ca458de1e59334b9d451986c_full.jpg"));
        //products.add(new DownImg(14241,"frango a passarinho seara pacote","https://www.conectacompras.com.br/up_produtos/prod__20180515232025_11jpg.jpg"));
        //products.add(new DownImg(7440,"Agua sanitaria brilux 2 litros","https://redeultramercado3.index.com.br/redeultramercado/fotos/g341179.jpg"));
        //products.add(new DownImg(7441,"Alcool mega 1lt","https://www.confianca.com.br/media/catalog/product/cache/1/image/1200x630/9df78eab33525d08d6e5fb8d27136e95/3/4/343706.jpg"));
        //products.add(new DownImg(7442,"alvejante vanish sachê 500ml","https://patiogourmet.com.br/online/api/image/2ee26819434bdbb3754bcd4bc3b93437?width=376&height=376"));
        //products.add(new DownImg(7448,"difusor de aromas secar 100ml","https://www.paodeacucar.com/img/uploads/1/761/482761.jpg"));
        //products.add(new DownImg(7446,"amaciante de roupas ypê 2l","https://www.extra-imagens.com.br/Control/ArquivoExibir.aspx?IdArquivo=114790219"));
        //products.add(new DownImg(7453,"limpador multiuso ypê 500ml","https://http2.mlstatic.com/S_754861-MLB27212000542_042018-O.jpg"));
        //products.add(new DownImg(7458,"tira manchas vanish poder o2 450g","https://superprix.vteximg.com.br/arquivos/ids/176423-292-292/Tira-Manchas-Vanish-Poder-O2-450g---50--de-desconto-no-Vanish-Crystal-White-450g.png"));
        //products.add(new DownImg(7459,"detergente ypê 500ml","https://http2.mlstatic.com/detergente-ype-500-ml-unidade-D_NQ_NP_622066-MLB25886814688_082017-F.jpg"));
        //products.add(new DownImg(7461,"sabão em pó brilhante 2kg","https://static.carrefour.com.br/medias/sys_master/images/images/hc1/h48/h00/h00/9213047767070.jpg"));
        //products.add(new DownImg(7479,"absorvente intimus gel noturno c8 un","https://drogariasp.vteximg.com.br/arquivos/ids/186319-500-500/223000.jpg"));
        //products.add(new DownImg(7480,"absorvente sempre livre especial c8 unids","https://ikesaki.vteximg.com.br/arquivos/ids/164905-1000-1000/Absorvente-Sempre-Livre-Especial-Com-Abas-C-8-Adapt-S-8492.00.JPG.jpg"));
        //products.add(new DownImg(7483,"Ampola pantene 15ml","https://hiperideal.vteximg.com.br/arquivos/ids/169571-1000-1000/1910230.jpg?"));
        //products.add(new DownImg(7484,"aparelho gillete mach3 reg.","https://www.extra-imagens.com.br/BelezaSaude/Barba-e-Depilacao/BarbeadoresDescartaveis/399770/4427291/Aparelho-de-Barbear-Gillette-Mach3-Sensitive-399770.jpg"));
        //products.add(new DownImg(7467,"achocolatado nescau 2.0 lata 400g","https://www.paodeacucar.com/img/uploads/1/755/536755.jpg"));
        //products.add(new DownImg(7469,"café sul de minas 500g","https://assets.xtechcommerce.com/uploads/images/medium/420f1e824427db507512168469e23f85.png"));
        //products.add(new DownImg(7439,"arroz bom no prato 5kg","http://www.arrozeirabomjesus.com.br/images/produto/1.png"));
       //products.add(new DownImg(7712,"absorvente intimus days protetor diario l80p60","https://savegnago.vteximg.com.br/arquivos/ids/273459-1000-1000/figura-1frente.jpg"));
        //products.add(new DownImg(14201,"alcatra suina fatiada sadia kg","http://img.sitemercado.com.br/produtos/99e2964e8bf15adb3ed8834d9afcb1526a1845009508c43cbe831d0bded73b1d_full.jpg"));
        //products.add(new DownImg(14259,"Acucar Guarani Natural Demerara sache 500g","https://www.sondadelivery.com.br/img.aspx/sku/1592891/530/1592891.jpg"));
        //products.add(new DownImg(7594,"Biscoito Belvita 75g","https://savegnago.vteximg.com.br/arquivos/ids/277331-1000-1000/figura-1frente.jpg"));
        //products.add(new DownImg(7429,"Biscoito Richester Cream Cracker 200g","https://araujo.vteximg.com.br/arquivos/ids/3740766-1000-1000/07891152110128.jpg"));
        //products.add(new DownImg(7434,"Arroz Agulhinha ou Parboilizado Carreteiro 5kg","https://i1.wp.com/poddi.com.br/wp-content/uploads/2019/02/ArrozCarreteiro5kg.jpg"));
        //products.add(new DownImg(7555,"Caldo knorr 114g","https://savegnago.vteximg.com.br/arquivos/ids/270892-1000-1000/figrura-1frente.jpg"));
        //products.add(new DownImg(7435,"Farinha de Trigo Ùtil Especial 1kg","https://www.novasafra.com.br/novasafra/_ImagemProdutos/miniaturas140x140/113708.jpg"));
        //products.add(new DownImg(7878,"Lasanha Dona Benta Direto ao Forno 200g","https://01b18884b6cf8a2c.cdn.gocache.net/confianca/catalog/product/cache/4/image/350x350/9df78eab33525d08d6e5fb8d27136e95/9/0/908959.jpg"));
        //products.add(new DownImg(7886,"massas com ovos dona benta","https://santahelenacenter.com.br/wp-content/uploads/fotos-produtos/58234.jpg"));
        //products.add(new DownImg(7908,"Gelatina Royal 25g/35g","https://redemachado.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/4/9/499883.jpg"));
        //products.add(new DownImg(7812,"Mini Chicken Minions Perdigão 300g","https://redeultramercado3.index.com.br/redeultramercado/fotos/g340291.jpg"));
        //products.add(new DownImg(7933,"Margarina Becel 250g","https://www.deliveryextra.com.br/img/uploads/1/885/496885.jpg"));
        //products.add(new DownImg(7939,"mortadela de frango perdigao","https://savegnago.vteximg.com.br/arquivos/ids/266672-1000-1000/figura-1frente.jpg"));
        //products.add(new DownImg(7934,"Capeletti, Pacotini, Ravioli 400g Mezzani","https://savegnago.vteximg.com.br/arquivos/ids/271972-1000-1000/figura-1frente.jpg"));
        //products.add(new DownImg(7947,"Margarina Qualy Light Sadia 500g","https://decisaoentrega.fbitsstatic.net/img/p/margarina-qualy-light-com-sal-500g-267257/434540.jpg"));
        //products.add(new DownImg(7946,"Margarina Qualy Cremosa 500g","https://cdn.awsli.com.br/600x450/672/672161/produto/290102015b9581d71f.jpg"));
        //products.add(new DownImg(7956,"blanquet de peru sadia","http://www.hippo.com.br/uploads/Produto/73047/Default/grande_49bed02a95fa5d7b01855f6661ed80f3.jpeg"));
        //products.add(new DownImg(7958,"Mortadela Defumada Seara kg","https://muffatosupermercados.vteximg.com.br/arquivos/ids/218863-400-400/Mortadela-Seara-Defumada-Fatiada-Kg-198532.jpg"));
        //products.add(new DownImg(14261,"lasanha seara 1kg","https://www.paodeacucar.com/img/uploads/1/185/522185.jpg"));
        //products.add(new DownImg(7772,"energético minotauro 2l","http://d26lpennugtm8s.cloudfront.net/stores/191/643/products/energetico-minotauro11-bfaa54da4fd0d596a715460922193747-640-0.jpg"));
        //products.add(new DownImg(7800,"","http://www.feiranovaemcasa.com.br/tmpImg/a9dae10c-d8f5-44e2-92e0-0efb0517c9d2v00.jpg"));
        //products.add(new DownImg(0,"",""));
        //products.add(new DownImg(0,"",""));
        //products.add(new DownImg(0,"",""));
        //products.add(new DownImg(0,"",""));

        /*DaoImage dao = new DaoImage();
        SendImage den ;
        for (DownImg img : products){

            den = new SendImage(img.id, img.title.replaceAll(" ","_"), ImagemUtil.downloadAndResizeImage(img.urlImg,200));

            System.out.println(den.getImageByte().length);
            boolean result = dao.updateImage(den);
            if(result == false){
                den.setImageName(den.getImageName());
               dao.getProductImage().insertImageDB(den);
            }
            System.out.println("RESULT: "+result);
        }*/


        //new DaoPagarme.Cards().getAllCard(pass);
/*
        | Bandeira   | Comeca com                                  | Máximo de número | Máximo de número cvc |
| ---------- | ------------------------------------------- | ---------------- | -------------------- |
| Visa       | 4                                           | 13,16            | 3                    |
| Mastercard | 5                                           | 16               | 3                    |
| Diners     | 301,305,36,38                               | 14,16            | 3                    |
| Elo        | 636368,438935,504175,451416,509048,509067,  |                  | 3(?)
|            | 509049,509069,509050,509074,509068,509040,
|            | 509045,509051,509046,509066,509047,509042,
|            | 509052,509043,509064,509040                 |                  |
|            | 36297, 5067,4576,4011                       | 16               | 3
                | Amex       | 34,37                                       | 15               | 4                    |
| Discover   | 6011,622,64,65                              | 16               | 4                    |
| Aura       | 50                                          | 16               | 3                    |
| jcb        | 35                                          | 16               | 3                    |
| Hipercard  | 38,60                                       | 13,16,19         | 3
*/

        /*BCard card = new BCard();
        card.setNumber("5234214945910549");
        card.setPaymentMethod(BCard.PaymentMethod.CREDIT_CARD);
        card.setHolderName("Marlon S Carvalho");
        card.setExpirationDate("0528");
        card.setCVV("394");
        card.setIdCustomer(108838771);

        String userBuyerJsonEncrypted = new Gson().toJson(Cryptograph.encrypt(new Gson().toJson("135")));

        //new DaoPagarme().getCards().createCard(userBuyerJsonEncrypted);
        Customer customer = new DaoPagarme().getCustomer().getCustomer(userBuyerJsonEncrypted);
*/

//        ArrayList<BCard> cards = new DaoPagarme().getCards().getAllCard(108838771);
//        for (BCard card : cards){
//            System.out.println("CARD: "+card.getNumber());
//        }


        /*APIPagarme api = new APIPagarme("ak_test_6EZk6wzeQ80xgWagq218CTr5rvOYDB");
        String publicKey = api.getCardManager().getPublicKeyToCardHash();

        System.out.println("PUBLIC KEY: "+publicKey);*/

         //new DaoHotAddress().getAddressFromUser(10);
        //byte[] streetNameOrig = new String(street.getName().getBytes(), "ISO-8859-1").getBytes("UTF-8");


        //System.out.println("NAME: "+new String(streetNameOrig));
        //System.out.println("NAME: "+street.getName());


        /*LAddress address = new LAddress();
        address.setCEP("21361-360");
        address.setStreet("Rua José Machado");
        address.setCity("Rio de Janeiro");
        address.setNeighborhood("Irajá");
        address.setState("Rio de Janeiro");
        address.setContry("Brasil");
        address.setNumber(105);
        address.setComplement("Casa 505");

        HotOrder order = new HotOrder();
        order.setAddress(address);
        order.setIdUser(10);
        order.setBrandCard("Master Card");
        order.setFeeDelivery(4.59);

        HotOrder orderInserted = new DaoHotOrder().insertOrder(order);
        System.out.println("INSERTED: "+orderInserted.getId());*/
    }

    public static boolean checkIfClosed(String timeActual, String openingOrClosedHour) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date present = parser.parse(timeActual);
            Date closed = parser.parse(openingOrClosedHour);
            if (present.after(closed)) {
                return true;
            }
        } catch (ParseException e) {
            // Invalid date was entered
        }
        return false;
    }

    public static synchronized void chekDirectory(File directory, int countDirectory, int countFiles) throws IOException {
        File[] files = new File(directory.getAbsolutePath()).listFiles();

        for(File file : files){
            if(file.isFile()){
                countFiles++;
                checkFile(file, countFiles);
            } else {
                countDirectory++;
                chekDirectory(file, countDirectory, countFiles);
            }

        }
    }

    public static synchronized void checkFile(File file, int countFiles) throws IOException {
        countFiles++;
        System.out.println("******************************************");
        System.out.println("FILE NAME: "+file.getName());

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        String line ;

        while((line = br.readLine()) != null){
            System.out.println(line);
            sb.append(line);
            sb.append("\n");
        }
        System.out.println("******************************************");
        System.out.println("SAVED");
        reSave(file, sb);
        System.out.println("******************************************");

    }

    public static synchronized void reSave(File file, StringBuilder sbContent) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("",0,0);
        writer.flush();
        writer.append(sbContent.toString());
        writer.flush();
        writer.close();
    }

    public static Payment saveCard() throws ExecutionException, InterruptedException {

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<Payment> future = exec.submit(new Callable<Payment>() {
            @Override
            public Payment call() throws Exception {
                return new Payment();
            }
        });
        Payment pay = future.get();
        return pay;
    }

    public static class DownImg{
        private int id;
        private String title, urlImg ;

        public DownImg(int id, String title, String urlImg) {
            this.id = id ;
            this.title = title ;
            this.urlImg = urlImg ;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrlImg() {
            return urlImg;
        }

        public void setUrlImg(String urlImg) {
            this.urlImg = urlImg;
        }
    }

    public static synchronized String requestJson(){
       
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            
            Future<String> futureResult = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    StringBuilder urlContruction = new StringBuilder();
                    urlContruction.append("https://api.mapbox.com/matching/v5/mapbox/driving/");
                    urlContruction.append("-117.17282,32.71204;-117.17288,32.71225;-117.17293,32.71244;-117.17292,32.71256;-117.17298,32.712603;-117.17314,32.71259;-117.17334,32.71254?");
                    urlContruction.append("access_token=pk.eyJ1IjoibWFybG9uY3pjIiwiYSI6ImNqcXJoMXJ2aTAxZzQ0MnBxbmQxOXh5amUifQ.QbqfUUz8WteXALxmLkhyTQ");
                    
                    URL url = new URL(urlContruction.toString());

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    if (con.getResponseCode() != 200){
                        throw new RuntimeException("HTTP ERROR CODE: "+con.getResponseCode());
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String line ;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    con.disconnect();
                    reader.close();
                    
                    return response.toString();
                }
            });
            executor.shutdown();
            return futureResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível acessar o servidor ou banco de dados\nverifique se está configurado corretamente!","Erro ao tentar acessar o serviço",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(RequestWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null ;
    }

    public static void sleeping(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class DbTranfers{

        public void transferFromTO(){
            Connection con =  ConnectionDB_APP.connect();
            PreparedStatement pst = null ;

            String sql = "SELECT * FROM images limit " ;

            try {
                pst = con.prepareStatement(sql);
                //ResultSet rs = pst.executeQuery();

                DaoProducts czc = new DaoProducts();
                DaoImage dao = new DaoImage();

                int ID = 7626 ;

                while(ID < 14254){

                    ID++;
                    System.out.println("VERIFY IMG: "+ID);
                    if(!dao.getProductImage().existImg(ID, null)){
                        System.out.println("REQUEST IMG: "+ID);
                        SendImage s = dao.getProductImage().getSendImage(ID);
                        System.out.println("REQUEST SUCCESS: "+ID);

                        System.out.println("------------------------------------------");
                        System.out.println("ID: "+s.getIdProduct()+" PATH: "+s.getSubPath()+" BYTES: "+s.getImageByte());
                        System.out.println("------------------------------------------");
                        dao.getProductImage().insertImageProduct
                                (
                                        s.getIdProduct(),
                                        s.getSubPath(),
                                        s.getImageByte()
                                );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void transfer(){

            DaoProducts dao = new DaoProducts();
            List<SendImage> images = dao.getAllProducts();

            System.out.println("IMAGES COUNT: "+images.size());

            DaoImage daoImg = new DaoImage();

            for(SendImage s : images){

                byte[] imgLocal = getImageLocal(s.getSubPath().replaceAll(".png",".jpg"));
                s.setImageByte(imgLocal);

                if(imgLocal != null){

                    boolean b = daoImg.updateImage(s);
                    if(!b){
                        s.setSubPath(s.getSubPath().replaceAll(".jpg",".png"));
                        b = daoImg.updateImage(s);

                        if(!b){
                            daoImg.getProductImage().insertImageProduct
                                    (
                                            s.getIdProduct(),
                                            s.getSubPath(),
                                            s.getImageByte()
                                    );
                        }
                    }

                }
            }
        }

        private byte[] getImageLocal(String imageName){
            try {
                return Files.readAllBytes(Paths.get("C:\\COMPRAZEM_CASA_RESOURCES\\Guanabara\\Nova pasta\\"+imageName));
            } catch (IOException e) {
                // Baixar imagem
                System.out.println("Imagem não existe");
            }
            return null ;
        }
    }
}
