package br.com.comprazencasa.products;

import apis.encrypt.Cryptograph;
import apis.pagarme.APIPagarme;
import apis.pagarme.userbuyer.BCard;
import apis.pagarme.userbuyer.BDocument;
import apis.pagarme.userbuyer.BProduct;
import apis.pagarme.userbuyer.UserBuyer;
import br.com.comprazencasa.database.DaoPagarme;
import br.com.comprazencasa.util.ImagemUtil;
import br.com.comprazencasa.util.UtilPrices;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.datastructures.payment.Payer;
import me.pagar.model.Customer;
import me.pagar.model.PagarMe;
import me.pagar.model.PagarMeException;
import me.pagar.model.Transaction;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Tester {

    public static void main(String[] args) throws Exception {


        new Thread() {
            @Override
            public void run() {
                /*LEGUMES
                createImage("cebola_branca","https://img.sitemercado.com.br/produtos/25441423b3f9e5fad21bf742f837a6fb317ac0ba49981f7cf76fdee8b15ac198_full.jpg");
                createImage("tomate_comum","https://img.sitemercado.com.br/produtos/bb12ef09fa0e1eddb8c29b0075d252582a2dc6e48ec52a2a4c6095b059d1ee9a_full.jpg");
                createImage("cebola","https://img.sitemercado.com.br/produtos/abba3789b6231665b8152f47e3881d2473e44be7fcff7a59cfabdfa3f081fe72_full.jpg");
                createImage("abobora_italia","https://img.sitemercado.com.br/produtos/f5a2611b5da3abfa3505554d59893e3b43cd9f26d84b4c23374a1dac2e5c49b3_full.jpg");
                createImage("abobrinha_italia","https://img.sitemercado.com.br/produtos/0d8106d2dcb449aa367c9f2227c3afdfc40205365499e92f0bf851bdb7e03fcc_full.jpg");
                createImage("batata_doce_rosada","https://img.sitemercado.com.br/produtos/593a58615b9293037008cb2814238de03bf6016ea0cc391a00c29a31586f90c7_full.jpg");
                createImage("abobora_menina_parreira","https://img.sitemercado.com.br/produtos/660cb9354855e9224ec179add0f25bf571c7c423528ebd97c7fd88a67d9f96dc_full.jpg");
                createImage("abobrinha_italiana","https://img.sitemercado.com.br/produtos/b5be670b69623c8ac52127897e1bac54b643ef01748d693cc2f1d674b99cd8da_full.jpg");
                createImage("batata_lavada","https://img.sitemercado.com.br/produtos/b7dd831baa3b79bed5da2655f116e1c6dc621262cf5468e9bf76f370cb585a76_full.jpg");
                createImage("abobora_brasil","https://img.sitemercado.com.br/produtos/12a4dfb7f4b8e9425720b731fda77d86fb8d2fd5a700bfe2fff5286c9108ad47_full.jpg");
                createImage("gengibre","https://img.sitemercado.com.br/produtos/1219400ea90d67442c3e09dac359fe27e8c3951b1cc0ad120c0a832f6e9b60fb_full.jpg");
                createImage("pepino_comum","https://img.sitemercado.com.br/produtos/79470ff3f58e9b2371280b3155757643a3ea2ce4afe9f0dc92bfe6b0186ac57c_full.jpg");
                createImage("pepino_caipira","https://img.sitemercado.com.br/produtos/7d55a60e520a7e72be027961c30a8f7a33744d2552db052c5a1c83a536fc8318_full.jpg");
                createImage("alho_roxo","https://img.sitemercado.com.br/produtos/40d49b2e796e8193f163c89bfa2a45d42534228201e4bb7c6a97e5fb0589827a_full.jpg");
                createImage("inhame","https://img.sitemercado.com.br/produtos/24bb450169c847d6bb04bd369c3eeff3f348333976674a9db2c551d833c6585c_full.jpg");
                createImage("abobrinha","https://img.sitemercado.com.br/produtos/c8aa6c074764a4eb4e13120de0f689ef4f9112ea161c0968d4d0e83f703a80bb_full.jpg");
                createImage("batata_bolinha","https://img.sitemercado.com.br/produtos/6505440baaf20105b9759025829ec42917e67d12a938ac1a3d30925d7c8f2c9a_full.jpg");
                createImage("pimentao_vermelho","https://img.sitemercado.com.br/produtos/96ca6915941e0f9fd63faffa5cbb63fcd8f0a0229427a393aeeff3b21efeb463_full.jpg");
                createImage("pinhao","https://img.sitemercado.com.br/produtos/d630800bb4182283947e8660c94600c95f22a98f0e722278c9c5fe0b0b0031f3_full.jpg");
                createImage("pimentao_amarelo","https://img.sitemercado.com.br/produtos/c988874fbe4e1019698b80a48756272e459957b3c06395b6753d330b73183d8a_full.jpg");
                createImage("cebola_roxa","https://img.sitemercado.com.br/produtos/514ca66fe51c2daba711d815d13891d792bfed0e7ba6ebadb49e7a690688bbe4_full.jpg");
                createImage("jilo","https://img.sitemercado.com.br/produtos/13ddedaddd40bb2de728fbb398a888ba77ce696f4b21c250dad89b1ef04d8923_full.jpg");
                createImage("abobrinha_brasileira","https://img.sitemercado.com.br/produtos/fc07a18ca171f2a261b5747e3317b426b6d046ec3fae4adc13be15afd8d00111_full.jpg");
                createImage("abobrinha_paulista_verde","https://img.sitemercado.com.br/produtos/9b02d68ae8d90cdd37d728acac2b5122944b4e6993ddaec31ff4ec45aaf0947d_full.jpg");
                createImage("cebola_roxa_red","https://img.sitemercado.com.br/produtos/a716004cdb38cbd0f706eb2a6b227cd8dd346871bc984f2943946868879919b3_full.jpg");
                createImage("abobora_paulista","https://img.sitemercado.com.br/produtos/7d08b9218a74df09f2ed10ef5c545ba091e8004a9642fbfab95f7bebf37fd474_full.jpg");
                createImage("cebola_conserva","https://img.sitemercado.com.br/produtos/84e3b470da922e5b33218ba25eddd84abdd03fce8d015137a6b2d31cf477cedf_full.jpg");
                createImage("batata_pirulito","https://img.sitemercado.com.br/produtos/79c5062b59fc1c4fc76104cdab5f3a0840bc6584586ef4d7466770d83d6623de_full.jpg");
                createImage("cenoura_baby_grimmway_embalagem_250g","https://img.sitemercado.com.br/produtos/7bbf8f68ccf683a0863980799ad84a94be2bdb929f99e32f6ac37865eca57462_full.jpg");
                createImage("ervilha_torta","https://img.sitemercado.com.br/produtos/135340955f66d6f51113e7e83dfe37b19687114aa7f6abebc336568e40fb441f_full.jpg");
                createImage("abobora_paulista_madura","https://img.sitemercado.com.br/produtos/3b13cf2e76204e3775732202c22c06192fd9ec22909ef1ebf79515182d50d36d_full.jpg");
                createImage("batata_asterix_vermelha","https://img.sitemercado.com.br/produtos/665d122c1a45a3a4cfbbb82fe2a69352be7d188dd37f14d08bfda21416865b4f_full.jpg");
                createImage("pimenta_vermelha","https://img.sitemercado.com.br/produtos/82c94dffa1af7792920559376293feee5f0fe9fa04712c3c259c99c4eee890cc_full.jpg");
                createImage("batata_doce_branca","https://img.sitemercado.com.br/produtos/474ef3ac65ba545f17071f75d14c7e1934f92e00fc5d506bae134846e3a529d7_full.jpg");
                createImage("abobora_seca","https://img.sitemercado.com.br/produtos/c8ce43c705789092d0dc901529dee01ac4cbc20b05da3c5552d9fd8af3621652_full.jpg");
                createImage("rabanete_a_granel","https://img.sitemercado.com.br/produtos/2f51577f4cb9901e6b88472aa0927783d8e7cbd19dd917e9b00c113346b70ab7_full.jpg");
                createImage("cara","https://img.sitemercado.com.br/produtos/3da1af8c38ae2baac1e935d2636e6f35626e8ba8ce191901110d671e6eb07cd6_full.jpg");*/

                /* TEMPEROS FRESCOS
                createImage("alho","https://img.sitemercado.com.br/produtos/6168eb76ce4f7c7384607786738285f1aee45a74d21b615946fef3574bfe517a_full.jpg");
                createImage("pimenta_dedo_de_moca_vermelha","https://img.sitemercado.com.br/produtos/69f5ac5685384ee3e7bc000226d972b0252b72d085c985673fcf01778ed2c40d_full.jpg");
                createImage("pimenta_ardida","https://img.sitemercado.com.br/produtos/0c4e70f985f0ee30871f65aa4139fb05d1ed1f6c308c0db4986138397f057d97_full.jpg");
                createImage("pimenta_cambuci","https://img.sitemercado.com.br/produtos/42aba409afd2dfcdcf102a460fa6065662508fa16046ca21e0cac5c3ac23899d_full.jpg");
                createImage("pimenta_americana","https://img.sitemercado.com.br/produtos/a4e45651223e595e1e2475e38b03db91f644f117967910e1321a0f489413b334_full.jpg");*/

                /* FRUTAS SECAS
                createImage("remix_de_castanhas_mae_terra_embalagem_25g","https://img.sitemercado.com.br/produtos/745623fab8c5f918b96da6dd263f0f85a74218054025be50c6400dfc1370f3e1_full.jpg");
                createImage("tamara_sem_caroco","https://img.sitemercado.com.br/produtos/9004401ba8069cf024e8b6cde13a478feb905c926ecd341e99fdf03599351446_full.jpg");
                createImage("mix_frutas_secas_brasil_frutt_pote_150g","https://img.sitemercado.com.br/produtos/ba33824243ee51cb56dd31cf57af2eace3062b5651d724f43995917fb5f064d2_full.jpg");
                createImage("uva_passa_seca_sem_semente_a_granel","https://img.sitemercado.com.br/produtos/4486a5d24a081035647702966ca2cbe6c83cb777a8761a2a39f3539b34118bd2_full.jpg");
                createImage("castanha_de_caju_torrada_e_salgada","https://img.sitemercado.com.br/produtos/2e8f5f9d358665d74f10ffc999d8e79d78147bdbe01bd496fed644b063821fcd_full.jpg");
                createImage("castanha_de_caju","https://img.sitemercado.com.br/produtos/9614b0396a7a0119fd0dc22bb74dbf40ff597f39a7c0488fa7b30eed5f5ed913_full.jpg");
                createImage("damasco_turco_a_granel","https://img.sitemercado.com.br/produtos/dc7f49e4817d9dc96d4facd371044832dcdbb57776cd3de6f2d0915974f2e94e_full.jpg");
                createImage("castanha_do_para_sem_casaca_media_a_granel","https://img.sitemercado.com.br/produtos/e781ec6b7d15bc577b14e4336709ccf2320f17486cd08ae460081ca2e88cf602_full.jpg");
                createImage("uva_passa_escura_la_violetera_sem_semente_sache_100G","https://img.sitemercado.com.br/produtos/77ff75d4c5fba715be9645de7932802fe6cddbdc7e3729036ae66cfd493b12f2_full.jpg");
                createImage("castanha_de_caju_sl_w1_a_granel","https://img.sitemercado.com.br/produtos/adeebeef52d49bfd42a37baf0d37426534643308d4af9893206c4db3379cd5e0_full.jpg");
                createImage("amendoa_torrada_salgada_a_granel","https://img.sitemercado.com.br/produtos/af6e6c874ee30682383986e7f122ea8bc9711ac2a27a8fd37d9219f3156a8eaf_full.jpg");
                createImage("ameixa_preta_com_caroco_a_granel","https://img.sitemercado.com.br/produtos/3c686dfdde20791cf9517de34329439739260c066706871e163f5f7e579bddd2_full.jpg");
                createImage("uva_passa_branca_sem_semente","https://img.sitemercado.com.br/produtos/4902745001fdcefee9cf8a1a6252ba49e116e22d2ca286fa5455637f5936deba_full.jpg");
                createImage("mix_sementes_jasmine_nuts_pacote_40g","https://img.sitemercado.com.br/produtos/f1d7656e7fcc94453e9beef13ebba08110bdde70141ae125222cd28ba3dbf515_full.jpg");
                createImage("banana_passa_a_granel","https://img.sitemercado.com.br/produtos/af02f373cbafff0ce33759cd0e5eedd5b55992cacad4cbab04484ff7067539de_full.jpg");
                createImage("castanha_de_caju_picada_x-3t","https://img.sitemercado.com.br/produtos/336b7f20c32713b926cdaa06f72fa96adea2637d828d97be126c9b574773338e_full.jpg");
                createImage("amendoa_sem_casca_a_granel","https://img.sitemercado.com.br/produtos/f9663ba0a20786614faf8587f72d982e95f41b5bb6793248b907a792d9eb3d1a_full.jpg");
                createImage("uva_passa_branca_sem_semente_a_granel","https://img.sitemercado.com.br/produtos/536427193ae7caa386ddebea4a9cc9257c79df347b034a64c700f05bc5c4244d_full.jpg");
                createImage("ameixa_seca_ole_lata_180g","https://img.sitemercado.com.br/produtos/7e046394ab4c28c48aa242b67036cd13e92297b474eda43030d256384a96e9d4_full.jpg");
                createImage("ameixa_seca_predilecta_lata_150g","https://img.sitemercado.com.br/produtos/92e9e85393264c4ff5468f6c76312cd06e21a58471493c5710c1fd53cba12aa0_full.jpg");
                createImage("avela_sem_casca_a_granel","https://img.sitemercado.com.br/produtos/51340ecc3a379f877a4bbaf825238a240fe26d59c4e253e6ec8c67e2fc48b7d7_full.jpg");
                createImage("frutas_cristalizadas_la_violetera_cubos_pacote_150G","https://img.sitemercado.com.br/produtos/2c9f9a111905ad83c77e79a76a2ffb34a3695ec5a7e60438c35eb89df1e0513f_full.jpg");
                createImage("frutas_cristalizadas_a_granel","https://img.sitemercado.com.br/produtos/686084e14e9c3c7e92b574b04bd1a82c6feaecdef8132d72cca26fead94a51e6_full.jpg");
                createImage("uva_passa_branca","https://img.sitemercado.com.br/produtos/4f514240801dddf0ed5fb52e7353a0d7df01c848bb687f7743dd155db355ab7e_full.jpg");
                createImage("nozes_com_casca","https://img.sitemercado.com.br/produtos/8a8297972e98b4d8781910f215cbf75c63eea4dfa8d63286d41dc8a4fa77ed8e_full.jpg");*/

                /* VERDURAS
                createImage("repolho_roxo","https://img.sitemercado.com.br/produtos/39249d14d9b00e35fd9ecfeec9cbd537ee6c45160e80b391c5c9b6481b4e14ef_full.jpg");
                createImage("couve_folha_maco","https://img.sitemercado.com.br/produtos/809029b70f275b9c56f7f2586636c5703e955a6da47d3d5aa10c38b3ec9dc9ea_full.jpg");*/

                /* FRUTAS
                createImage("pera_argentina","https://img.sitemercado.com.br/produtos/88b0de4e8cb8630ffc37ef4434febd872b408328a5c75cd799b0ecc105f23364_full.jpg");
                createImage("morango_bandeja_unidade","https://img.sitemercado.com.br/produtos/71892b29609d830f3508a4ba1180a23914c716c9e4854b55afdfb7f08d0a2710_full.jpg");
                createImage("kiwi","https://img.sitemercado.com.br/produtos/a2c0fd99602cd9e0b2e7011e17b86edc4157f20853f89468f875f5c994caa19d_full.jpg");
                createImage("tangerina","https://img.sitemercado.com.br/produtos/8443172d33df687e1ec7ef215809b619e2491b47a7ac59d67ca2da4724fd7014_full.jpg");
                createImage("banana_da_terra","https://img.sitemercado.com.br/produtos/21ec9af33a2b65e1bcf3f168a9f04b2c001f3a72122a504104af634f688d373f_full.jpg");
                createImage("melao_amarelo_rei","https://img.sitemercado.com.br/produtos/8c7258ca68b6dc6638f140a9be793025821537c9983fdae2c21865c1ae5d3f41_full.jpg");
                createImage("abacate_avocado","https://img.sitemercado.com.br/produtos/db21feb4b7bb7249e6ce736eaed0a4f3c4b6a6e3e96278d9756167f94b88c26b_full.jpg");
                createImage("maca_nacional","https://img.sitemercado.com.br/produtos/fc6df9a51e09a6f6dafe6ae1951d31d7ed9c9489e091b91817a77ed1bb6990a4_full.jpg");
                createImage("mexerica_murgote","https://img.sitemercado.com.br/produtos/5da27b0dc1113de52f82b9a7524ee7d3fe54f88b15720e9d86608922c7b4162a_full.jpg");
                createImage("banana_nanica","https://img.sitemercado.com.br/produtos/03b76c31339f8591006ec5911bd27af17f36d7fdfe8857cc73865bdd89eb4228_full.jpg");
                createImage("limao","https://img.sitemercado.com.br/produtos/db6a01490f846a64501001a2a7b0728555299d093d48286559b4763509fd4bba_full.jpg");
                createImage("mexerica_ponkan","https://img.sitemercado.com.br/produtos/ef1380be5afff8a796bed66e3566a98d0585fb60db116e659ef61d4078ac3ebb_full.jpg");
                createImage("abacate","https://img.sitemercado.com.br/produtos/d2ad37ad174cfe2e925413334412b61dce4b711771d2df52a2bfc3223d784249_full.jpg");
                createImage("pitaya_vermelha","https://img.sitemercado.com.br/produtos/5f1c42b3e57a552888d95a0cc2ff1fdfca51206d1b0261a3f4c71681422c0122_full.jpg");
                createImage("melancia_unidade","https://img.sitemercado.com.br/produtos/6d5c65dcdf678fc597e4c0a8b106671b0fb8279ecfb9c9cdb25cc8f62c43a3f8_full.jpg");
                createImage("pera_importada","https://img.sitemercado.com.br/produtos/9e71da5db45bf0694fbbbb9330b60a88867af6ae297a793edc607191cdb4f033_full.jpg");
                createImage("coco_seco","https://img.sitemercado.com.br/produtos/54b47635afac4efb5e438d76e331bf3624d65a2882245a2e730dbfbb7378bd34_full.jpg");
                createImage("maracuja","https://img.sitemercado.com.br/produtos/31065f132d1c8daa50a99db8c67c66033444cc47bd8d5237b2c96fe1f53ecacf_full.jpg");
                createImage("uva_brasil_nacional","https://img.sitemercado.com.br/produtos/623762af090c51f31d5ec86e7ef20a88054b36f7749092dcca5ff63a372b084e_full.jpg");
                createImage("abacaxi_unidade","https://img.sitemercado.com.br/produtos/18c59fdaf881469fecd1fc1379f2df6eab43c4020b12578b77e7ade2b173371e_full.jpg");
                createImage("maracuja_azedo","https://img.sitemercado.com.br/produtos/c13da2337db438f93aeaa711938a9ed7725a3420918266cfcf3e1c1e8147a81c_full.jpg");
                createImage("graviola","https://img.sitemercado.com.br/produtos/9a59004a0787f4342f9b7f2cdedb84c8d14c32b7a68645c7505d5859f4803a64_full.jpg");
                createImage("caqui","https://img.sitemercado.com.br/produtos/1684e686abcb9f84441fd1d77d5fbcad7791e71644688976135bb04568f82ef7_full.jpg");
                createImage("pera_portuguesa","https://img.sitemercado.com.br/produtos/485a225f0f6f69704619d8a7bff9c3a2feaafc226a73fe3e89f3d829a99b6e80_full.jpg");
                createImage("kiwi_importado","https://img.sitemercado.com.br/produtos/1b0aadaf56803c9575003ddb7081276165ca17befc65f920f61fd33bb37423d1_full.jpg");
                createImage("uva_crimson","https://img.sitemercado.com.br/produtos/2ffc47411171dc624d0f08077f2aac459543f0e24035c8b5c4a8e02f0fd68b2e_full.jpg");
                createImage("goiaba_vermelha","https://img.sitemercado.com.br/produtos/99d3f96b5627f76e6733d5fd7abc710bf2ae83556ee34121d409631744e1872b_full.jpg");
                createImage("ameixa_importada","https://img.sitemercado.com.br/produtos/74eb876426ac9d2709747d1de84018f791a899b9293694e1739b787d6d6195bd_full.jpg");
                createImage("melancia_redonda","https://img.sitemercado.com.br/produtos/299212a73ff3b7627bcbd8b18a2d1be989897a23118c6dd1bf3b30816416cb43_full.jpg");
                createImage("ameixa_nacional","https://img.sitemercado.com.br/produtos/29cbb36caa60f1bebd92e741781bc8c9149a4e6ce6574107e0cc3aa9cc8d69a3_full.jpg");
                createImage("maca_argentina","https://img.sitemercado.com.br/produtos/b8f84b51890a7fe976deaf9ca529d041cf66847b0bd2bca6748f6cf5bcef2a7e_full.jpg");
                createImage("laranja_bahia_nacional","https://img.sitemercado.com.br/produtos/afd5d0e7a7412aee918f37afaf7550d3cb5d9099e8c7c76ee34719c1630bc29c_full.jpg");
                createImage("caqui_chocolate","https://img.sitemercado.com.br/produtos/8b652ac5dee8a8597d384a0609482892e5bd5da1a02bac7a3e8e82f960751747_full.jpg");
                createImage("caqui_rama_forte","https://img.sitemercado.com.br/produtos/2314dd120e219ec6689a9cdab46ea6c1522ba7710fd15842e45c6bb073858f61_full.jpg");
                createImage("laranja_bahia_importada","https://img.sitemercado.com.br/produtos/e4879beaf8a97a1906cb83950019811b4eb4e91949290cdc0d8e5ba4a23d802d_full.jpg");
                createImage("ameixa","https://img.sitemercado.com.br/produtos/4c7d5e9368bbdf80c06b634c8e829db9e708ce9d7f353160154371d665989192_full.jpg");
                createImage("lima_da_persia","https://img.sitemercado.com.br/produtos/91ebf9d242bd8638c90b6ac9c886a27fa38b94b4cc44fc933bf9018121084fb5_full.jpg");
                createImage("pessego_importado","https://img.sitemercado.com.br/produtos/e6da8333f1e8a5c3aa1589928f67c98d1fb9a0c62ac0a41c256ce54c01dec7f2_full.jpg");
                createImage("pessego_nacional","https://img.sitemercado.com.br/produtos/40fc55d380d784302854d85708510c02599706392b1ce9e2b4f6f0b469b3d5a2_full.jpg");
                createImage("mexerica","https://img.sitemercado.com.br/produtos/4bd2c6cc52427758969689c3a7b608dea9b4dc6c3076bc3d598bf532058d9301_full.jpg");
                createImage("limao_siciliano","https://img.sitemercado.com.br/produtos/bb3f410fa2819c71ab98007e8f53500d50f0c736a0b4ada8f43dad4fd998190c_full.jpg");
                createImage("tangerina_ponkan","https://img.sitemercado.com.br/produtos/23a033934b532a10ef283b468beb7ee426e42c45bba1f60bbe86db4edb3b4534_full.jpg");
                createImage("carambola","https://img.sitemercado.com.br/produtos/f69e9a31eb0679b0ea93b45471129416249559f196913bb1230b313d785baf96_full.jpg");
                createImage("laranja_ponkan","https://img.sitemercado.com.br/produtos/c0839daa547a05763493f59ca9c0b0a3914f76bf3ba2a66a20fd2527d56c0a3f_full.jpg");
                createImage("limap_tahiti","https://img.sitemercado.com.br/produtos/e7b614958a3425a0572d8a24034f94a2e2c77c14af97ec3511bb3465277e3082_full.jpg");
                createImage("laranja_bahia","https://img.sitemercado.com.br/produtos/ca8fe30b6cafb6a045d0554aec6f17e5d2e953538245d307815d603a634f34ac_full.jpg");
                createImage("manga_rosa","https://img.sitemercado.com.br/produtos/cddd6b78efa43e4c514b2ac918c0da2214fa6cfbdc968b00b23dd6df2d2e3c36_full.jpg");
                createImage("maracuja_doce","https://img.sitemercado.com.br/produtos/70b04ba3c2feff4f562ef54c0551d14dcee985d25358d4ed308024b845faaa40_full.jpg");
                createImage("manga_haden","https://img.sitemercado.com.br/produtos/55ba1013eb2caf84e6ec3897ade50d891553eaeaa81191ffa7025375e5f0e348_full.jpg");
                createImage("maca_verde","https://img.sitemercado.com.br/produtos/716a38b019574477b8ede8a8025e09617892c2f1e90c7e0f0eb7273ae6668247_full.jpg");
                createImage("pera_williams","https://img.sitemercado.com.br/produtos/42e455b08d4d353ac57681a39d7e86bc8378971e84ef72cf83f4ab0ff2c2ff98_full.jpg");
                createImage("nectarina_nacional","https://img.sitemercado.com.br/produtos/d47fafbae0220a47567c0a95d001ca2d73f87552bd80da6cccdf9f8f17af41ac_full.jpg");
                createImage("uva_red_globe","https://img.sitemercado.com.br/produtos/955d92d8358f83185ff6ce9a515d0309a3e9e03ef29ed3d96a5bf49f190bce5f_full.jpg");
                createImage("melao_amarelo_redinha","https://img.sitemercado.com.br/produtos/895803400d68c63867c42ce3759a941c28bc3391d3afa8a6805c89a752cb8cb3_full.jpg");
                createImage("manga_espada","https://img.sitemercado.com.br/produtos/a91d754bd20bf438570c153946b77e55606042c0cae7a77f0ed8f5ebf5b4196f_full.jpg");
                createImage("maca_granny_smith","https://img.sitemercado.com.br/produtos/c6b28f07453bd7b3127b0c028e1c5052144e1fd852341d67f88651b1c81ef6d5_full.jpg");
                createImage("uva_rubi","https://img.sitemercado.com.br/produtos/b43b4a5c4fc243f5e01ee9e43dca5e7beb2db94a4d3433ef6c29a1f1bcdcf82a_full.jpg");
                createImage("manga_palmer","https://img.sitemercado.com.br/produtos/b14293de73d02b172f1af13fbd28893b29222f0e36b91695505d5f0ca73e5387_full.jpg");
                createImage("banana_marmelo","https://img.sitemercado.com.br/produtos/e8ecce67ff39f91aa06d9c08d4ca8efb7bb13a3d814adda89967134f6b877b2e_full.jpg");
                createImage("melao_orange","https://img.sitemercado.com.br/produtos/6d25c1942c510773d922ce0210e7bc567346e83a2eb1e5f31a374d9416d56d0d_full.jpg");
                createImage("banana_ouro","https://img.sitemercado.com.br/produtos/5aff73f630c570913776d4201b8ee620c390085808833cdb8b597269a6bbe799_full.jpg");
                createImage("coco_verde","https://img.sitemercado.com.br/produtos/188027331fcae8bd2012738abec2be56bd245df96ede1fa8698f42da4727e76b_full.jpg");
                createImage("melancia_baby","https://img.sitemercado.com.br/produtos/ea24c55486c95c9f8b5197103855ec69ff2f437fa9f0af4c83c008e15b65ca1a_full.jpg");
                createImage("maca_red","https://img.sitemercado.com.br/produtos/ea13d938c971b3381c3c5eda24419d98f676863d40758af16648752f6d149762_full.jpg");
                createImage("melao_pele_de_sapo","https://img.sitemercado.com.br/produtos/7b2e1b03021b6c49648cde3229ea1a22390cc0a286743b27d3c16101ee9b0ea1_full.jpg");
                createImage("nectarina_importada","https://img.sitemercado.com.br/produtos/fa1b9918150345ba14eddcdb2286ecc491f24e2b4c5de8681c7572649e01a705_full.jpg");
                createImage("melao_sapo","https://img.sitemercado.com.br/produtos/4e2030ddc9137c71ddb58cbcafed20d1666f1af84f9563ef47a4077410fd49a2_full.jpg");
                createImage("manga_bourbon","https://img.sitemercado.com.br/produtos/5c43bc527f37d65c3ce3b68cc9b190110df316edee51d51127fd4566fa272da1_full.jpg");
                createImage("pera_asiatica","https://img.sitemercado.com.br/produtos/933325e6e912126d271b021d2eba76498938829ff60773942f97d872c11fb0ae_full.jpg");*/


                //byte[] image = ImagemUtil.downloadAndResizeImage("",250);

                //ProductsManager pm = new ProductsManager("C:\\Users\\Marlon\\Desktop\\NOVOS_PRODUTOS\\Morning\\");
                //pm.loadingAndSaveProductsCarrefour(LoadingProducts.AllMarketLinks.Carrefour.Morning.ROASTED_BUNS_URL,"Morning");
                /*DaoCzc dao = new DaoCzc();
                DaoImage daoImg = new DaoImage();

                Fi
                        ITProduct product = new ITProduct();le[] files = new File("C:\\Users\\Marlon\\Desktop\\NOVOS_PRODUTOS\\Hortifruti\\").listFiles();
                for (File path : files){
                    System.out.println(path.getAbsolutePath());

                    File[] filesDirectory = new File(path.getAbsolutePath()).listFiles();
                    for (File file : filesDirectory){
                        System.out.println(file.getName());

                        product.setTitle(formatName(file.getName()));
                        product.setCategorie("Hortifruti");
                        product.setSubCategorie(path.getName());
                        product.setWeight(true);
                        product.setPathImg(file.getName());
                        product.setStatus("inactive");

                        try {
                            dao.insertProduct(product, Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }*/
            }
        }.start();

        /*new Thread(){
            @Override
            public void run() {
                ProductsManager pm = new ProductsManager("C:\\Users\\Marlon\\Desktop\\NOVOS_PRODUTOS\\Morning\\");
                pm.loadingAndSaveProductsCarrefour(LoadingProducts.AllMarketLinks.Carrefour.Healthy.INTEGRALS_URL,"Morning");
            }
        }.start();*/

        /*JSONObject request = new JSONObject();

        JSONObject payer = new JSONObject();
        payer.put("email","marlonsctms@gmail.com");

        JSONObject payment = new JSONObject();
        payment.put("payment_method_id","visa");
        payment.put("payment_type_id","credit_card");
        payment.put("token","jhuyt786r5yhfcgjvhkuoy7t86r75erdfhcgv");
        payment.put("transaction_amount",50);
        payment.put("installments",1);
        payment.put("processing_mode","aggregator");

        JSONObject disbursement = new JSONObject();
        disbursement.put("amount",50);
        disbursement.put("external_reference","2781555906094938");
        disbursement.put("collector_id","3916357421168084");
        disbursement.put("application_fee",20);
        disbursement.put("money_release_days",30);

        JSONArray payments = new JSONArray();
        payments.put(payment);

        JSONArray disbursements = new JSONArray();
        disbursements.put(disbursement);

        request.put("payer",payer);
        request.put("payments",payments);
        request.put("disbursements",disbursements);
        request.put("external_reference","2781555906094938");

        curl("https://api.mercadopago.com/v1/advanced_payments?access_token=APP_USR-2781555906094938-041818-fbd997be5916bd490ac7cba431351807-548936550",
                request.toString());*/

        //Customer c = new APIPagarme("ak_live_ZtHfMPxyiUh4uFW1HIAcUPPt1c2ZM6")
                //.getCustomerManager().getCustomer(108838771);

        /*UserBuyer buyer = new UserBuyer();
        buyer.setId(135);
        buyer.setName("Marlon Carvalho");
        buyer.setBirthday("1994-09-12");

        buyer.getDocument().setCPF("15856957776");
        buyer.getDocument().setType(BDocument.Type.CPF);

        buyer.getAddressDefault().setCity("Rio de Janeiro");
        buyer.getAddressDefault().setCountry("br");
        buyer.getAddressDefault().setNeighborhood("Irajá");
        buyer.getAddressDefault().setStreet("Rua José Machado");
        buyer.getAddressDefault().setState("rj");
        buyer.getAddressDefault().setZipCode("21361360");
        buyer.getAddressDefault().setStreetNumber(String.valueOf(105));
        buyer.getAddressDefault().setShippingAddress(0);
        buyer.getAddressDefault().setFeeDelivery(449);
        //buyer.getAddressDefault().setComplement(getOrder().getEndereco().getComplemento());

        buyer.getContact().setEmail("marlonsctms@gmail.com");
        buyer.getContact().getPhone().setDdi(55);
        buyer.getContact().getPhone().setDDD(21);
        buyer.getContact().getPhone().setNumber("992219815");

        BProduct product = new BProduct();
        product.setId(2253);
        product.setName("Monster Hinds");
        product.setCategory("Brinquedos");
        product.setPrice(250);
        product.setQuantity(1);
        product.setTangible(true);
        buyer.getProducts().add(product);

        BCard card = new BCard();
        buyer.setCard(card);*/

        //Transaction.Status status = new DaoPagarme().getTransactions().createTransaction(Cryptograph.encryptToJson(buyer));
        //System.out.println("PAYMENT: "+status.name());
        PagarMe.init("ak_test_6EZk6wzeQ80xgWagq218CTr5rvOYDB");
        Transaction tx = new Transaction().find(10192414);

        tx.capture(499);
    }

    public static void curl(String curl, String json) throws IOException {
        URL obj = new URL(curl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.flush();


        int responseCode = con.getResponseCode();

        BufferedReader iny = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = iny.readLine()) != null) {
            response.append(output);
        }
        iny.close();
        os.close();
        //printing result from response
        System.out.println(response.toString());
    }

    public static synchronized String formatName(String fileName) {
        String[] palavras = fileName.split("_");

        StringBuilder nameConstruct = new StringBuilder();
        for (String p : palavras) {
            nameConstruct.append(p.substring(0, 1).toUpperCase());
            nameConstruct.append(p.substring(1));
            nameConstruct.append(" ");
        }

        return nameConstruct.toString();
    }

    public synchronized static void createImage(String produto, String url) {
        byte[] image = ImagemUtil.downloadAndResizeImage(url, 250);

        //ImagemUtil.saveImage("C:\\Users\\Marlon\\Desktop\\NOVOS_PRODUTOS\\","Batat lisa","https://www.meumercadoemcasa.com.br/produto/"+produto);
        saveImageFromPASTE("C:\\Users\\Marlon\\Desktop\\NOVOS_PRODUTOS\\Hortifruti\\Frutas\\" + produto + ".jpg", image);
    }

    public synchronized static boolean saveImageFromPASTE(String urlPasta, byte[] image) {
        try {
            Files.write(Paths.get(urlPasta), image);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getCountDownDate(String dateNow, String otherDate, String formatIn) {
        try {
            DateFormat df = new SimpleDateFormat(formatIn);
            df.setLenient(false);
            Date d1 = null;
            d1 = df.parse(dateNow);
            Date d2 = df.parse(otherDate);
            long dt = (d2.getTime() - d1.getTime()) + 3600000; // 1 hora para compensar horário de verão
            return (int) (dt / 86400000L); // passaram-se 67111 dias;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Long countDownDate(String date, String format) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern(format);
        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime otherDate = LocalDateTime.parse(date, formater);
        return Long.valueOf(dateNow.until(otherDate, ChronoUnit.DAYS));
    }

    public static String formatDate(String date, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        Date dateFo = null;
        try {
            dateFo = fm.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fm.format(dateFo);
    }

    public static String formataData(String data, String formato) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatoSaidaDtCompleta = new SimpleDateFormat("dd/MM/yyyy 'à(s)' HH:mm:ss");
        SimpleDateFormat formatoSaidaDiaMesAno = new SimpleDateFormat("dd/MM/yyyy");
        Date dataTypeDate = null;
        String resultado = null;
        try {
            dataTypeDate = formatoEntrada.parse(data);
            if ("dataCompleta".equals(formato)) {
                resultado = formatoSaidaDtCompleta.format(dataTypeDate);
            } else if ("diaMesAno".equals(formato)) {
                resultado = formatoSaidaDiaMesAno.format(dataTypeDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static class ITAvailableHour implements Comparable<ITAvailableHour> {

        private int id, idUser, position;
        private String hour, idOrder;
        private boolean selected;

        public ITAvailableHour() {
        }

        public ITAvailableHour(int position, String hour) {
            this.id = position;
            this.hour = hour;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdUser() {
            return idUser;
        }

        public void setIdUser(int idUser) {
            this.idUser = idUser;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public String getIdOrder() {
            return idOrder;
        }

        public void setIdOrder(String idOrder) {
            this.idOrder = idOrder;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public int compareTo(ITAvailableHour o) {
            if (hour.equals(o.getHour())) {
                return 1;
            } else if (!hour.equals(o.getHour())) {
                return 0;
            }
            return 0;
        }

        public static boolean containsHour(final List<ITAvailableHour> list, final String hour) {
            return list.stream().map(ITAvailableHour::getHour).filter(hour::equals).findFirst().isPresent();
        }
    }
}
