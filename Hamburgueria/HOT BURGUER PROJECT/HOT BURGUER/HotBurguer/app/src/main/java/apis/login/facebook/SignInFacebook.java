package apis.login.facebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import apis.login.LoginInterface;

public class SignInFacebook implements FacebookCallback<LoginResult> {

    private Context context;
    private LoginButton btnFacebook;
    private LoginInterface loginInterface;
    private FacebookAccount account;
    private List<String> permissions;
    private static final int FACEBOOK_SIGN_IN = 1;

    private CallbackManager callbackManager;

    public SignInFacebook(Context context) {
        this.context = context;
        this.callbackManager = CallbackManager.Factory.create();
        if (permissions == null)
            this.permissions = new ArrayList<>();
    }

    public SignInFacebook(LoginInterface logInterface) {
        this.loginInterface = logInterface;
        this.context = loginInterface.getContext();
        this.callbackManager = CallbackManager.Factory.create();
        if (permissions == null)
            this.permissions = new ArrayList<>();
    }

    public void setButtonFacebook(LoginButton button) {
        this.context = button.getContext();
        this.btnFacebook = button;
        this.btnFacebook.setReadPermissions(Arrays.asList("email", "public_profile"));
        this.btnFacebook.registerCallback(callbackManager, this);
        if (permissions == null)
            this.permissions = new ArrayList<>();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //if (requestCode == FACEBOOK_SIGN_IN) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //}
    }

    public boolean isLogged() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        setAccount(accessToken);
        return accessToken != null && !accessToken.isExpired();
    }

    public void addPermission(String permission) {
        permissions.add(permission);
    }

    public void logout() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        setAccount(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        loginInterface.onSignInloading(false);
        loginInterface.onSignInFail(new Exception("Cancelled Exception"));
    }

    @Override
    public void onError(FacebookException error) {
        loginInterface.onSignInloading(false);
        loginInterface.onSignInFail(error);
    }

    private void setAccount(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject me, GraphResponse response) {
                if (response.getError() != null) {
                    // handle error
                } else {
                    String user_lastname = me.optString("last_name");
                    String user_firstname = me.optString("first_name");
                    String user_email = response.getJSONObject().optString("email");

                    String email = me.optString("email");
                    String id = me.optString("id");
                    String profilePicUrl = "http://graph.facebook.com/"+id+"/picture?type=large";

                    account = new FacebookAccount();
                    account.setEmail(email != null ? email : user_email);
                    account.setName(user_firstname != null ? user_firstname : user_lastname);
                    account.setPhotoUrl(profilePicUrl);

                    loginInterface.onSignInloading(false);
                    loginInterface.onSignInSucess(account);
                }
            }
        });

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < permissions.size(); i++) {

            String permission = permissions.get(i);
            sb.append(permission);

            if (i < permissions.size()-1)
                sb.append(",");

        }

        Bundle paramters = new Bundle();
        //paramters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
        paramters.putString("fields", sb.toString());

        request.setParameters(paramters);
        request.executeAsync();
    }

    public FacebookAccount getAccount() {
        return account;
    }

    public class FacebookAccount {
        public String name, email, photoUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }

    public static class Permissions {

        /*Com a permissão ads_management, seu aplicativo pode ler e gerenciar a conta de anúncios que possui ou à qual teve ou que tenha recebido acesso pelo proprietário da conta de anúncios.
        Uso permitido
        Programar a criação de campanhas, o gerenciamento de anúncios e a busca por métricas.
        Criar ferramentas de gerenciamento de anúncios que forneçam soluções inovadoras e valor diferenciado para os anunciantes.*/
        public static final String ADS_MANAGEMENT = "ads_management";

        /*Com a permissão ads_read, seu aplicativo pode acessar a API de Informações de Anúncios a fim de extrair informações de relatórios de anúncios de contas de anúncios que você possui ou para as quais tenha recebido acesso pelo proprietário ou proprietários de outras contas de anúncios por meio dessa permissão. Essas permissões também concedem ao seu aplicativo acesso à API do lado do servidor para que os anunciantes possam enviar eventos da web dos seus respectivos servidores diretamente para o Facebook.
        Uso permitido
        Fornecer à API acesso aos dados de desempenho de seu anúncio para utilização em análises de dados e painéis personalizados.
        Enviar eventos da web do seu servidor diretamente para o Facebook.*/
        public static final String ADS_READ = "ads_read";

        /*Com a permissão attribution_read, seu aplicativo pode acessar a API de Atribuição para extrair dados do relatório de atribuição das linhas de negócios que você possui ou às quais tenha recebido acesso pelo proprietário ou proprietários de outras linhas de negócios.
        Uso permitido
        Permite que o seu aplicativo acesse dados de desempenho dos anúncios da Atribuição para uso em análises de dados e painéis personalizados*/
        public static final String ATTRIBUITION_READ = "attribution_read";

        /*
            Com a permissão business_management, seu aplicativo pode ler e escrever com a API do Gerenciador de Negócios.
            Uso permitido
            Gerenciar ativos comerciais, como uma conta de anúncios.
            Reivindicar contas de anúncios.
        */
        public static final String BUSINESS_MANAGEMENT = "business_management";

        /*
            Com a permissão catalog_management, seu aplicativo pode criar, ler, atualizar e excluir catálogos de produtos da empresa dos quais o usuário é administrador.
            Uso permitido
            Criar soluções relacionadas ao comércio, como plataformas de comércio eletrônico, plataformas de viagem e anúncios dinâmicos.
            Criar soluções de gerenciamento de tipo de inventário, como inventário de produtos, de hotel ou de automóveis.
        */
        public static final String CATALOG_MANAGEMENT = "catalog_management";


        /*
            A permissão email possibilita que o aplicativo leia o endereço de email principal de uma pessoa.
            Uso permitido
            Comunicar-se com as pessoas e permitir que elas entrem no seu aplicativo com o endereço de email associado ao perfil do Facebook delas.
        */
        public static final String EMAIL = "email";

        /*
            A permissão public_profile possibilita que o aplicativo leia o perfil publico.
        */
        public static final String PUBLIC_PROFILE = "public_profile";

        /*
           A permissão first_name possibilita que o aplicativo leia o primeiro nome.
        */
        public static final String FIRST_NAME = "first_name";

        /*
          A permissão first_name possibilita que o aplicativo leia o ultimo nome.
       */
        public static final String LAST_NAME = "last_name";

        /*
         A permissão first_name possibilita que o aplicativo leia o id.
      */
        public static final String ID = "id";

        /*
            Com a permissão groups_access_member_info, seu aplicativo pode ler informações de membros do grupo disponíveis publicamente. Por exemplo, nome e identificação, caso o autor da publicação tenha concedido ao seu aplicativo o acesso a essas informações.
            Uso permitido
            Permitir aos aplicativos obter conteúdo publicado no grupo com detalhes do usuário.
            Ajudar os administradores a obter informações agregadas sobre as atividades que acontecem no grupo deles.
        */
        public static final String GROUPS_ACCESS_MEMBER_INFO = "groups_access_member_info";

        /*
            Com a permissão instagram_basic, seu aplicativo pode ler conteúdo de mídia e informações do perfil de uma conta do Instagram.
            Uso permitido
            Obter metadados básicos de um perfil da conta do Instagram para Empresas, por exemplo, nome de usuário e identificação.
       */
        public static final String INSTAGRAM_BASIC = "instagram_basic";

        /*
            Com a permissão instagram_manage_comments, seu aplicativo pode criar, excluir e ocultar comentários em nome da conta do Instagram vinculada a uma página. Seu aplicativo também pode ler e responder a mídias públicas e comentários em que uma empresa tenha sido mencionada ou marcada em uma foto.
            Uso permitido
            Ler, atualizar e excluir comentários de contas do Instagram para Empresas.
            Ler objetos de mídia, como stories, de contas do Instagram para Empresas.
        */
        public static final String INSTAGRAM_MANAGE_COMMENTS = "instagram_manage_comments";

        /*
            Com a permissão instagram_manage_insights, seu aplicativo pode obter acesso a informações da conta do Instagram vinculada a uma Página do Facebook. Seu aplicativo também pode descobrir e ler as informações de perfil e a mídia de outros perfis comerciais.
            Uso permitido
            Obter metadados de uma conta do Instagram para Empresas.
            Obter informações de dados de uma conta do Instagram para Empresas.
            Obter informações de story de uma conta do Instagram para Empresas.
        */
        public static final String INSTAGRAM_MANAGE_INSIGHTS = "instagram_manage_insights";

        /*
            Com a permissão leads_retrieval, seu aplicativo pode recuperar e ler todas as informações capturadas por um formulário de anúncio de cadastro associado a um anúncio criado no Gerenciador de Anúncios ou na API de Marketing.
            Uso permitido
            Contatar as pessoas que preencheram seu formulário de anúncio de cadastro solicitando mais informações. Por exemplo, uma concessionária entra em contato com um cliente em potencial (cadastro) que respondeu ao anúncio dela com cotações para um carro.
            Para plataformas de CRM autorizadas pelos anunciantes extraírem dados de cadastros em nome dos anunciantes. Esses anunciantes podem usar as informações do cadastro para entrar em contato com o usuário.
        */
        public static final String LEADS_RETRIEVAL = "leads_retrieval";

        /*
            Com a permissão pages_manage_ads, seu aplicativo pode gerenciar anúncios associados à Página.
            Uso permitido
            Criar anúncios para a sua Página.
            Gerenciar anúncios para a sua Página.
        */
        public static final String PAGES_MANAGE_ADS = "pages_manage_ads";

        /*
            Com a permissão pages_manage_cta, seu aplicativo pode executar as funções POST e DELETE nos pontos de extremidade usados para gerenciar botões de chamada para ação (CTA) em uma Página do Facebook.
            Uso permitido
            Fornecer acesso à API para gerenciar botões de chamada para ação (CTA) nas Páginas que você gerencia.
        */
        public static final String PAGES_MANAGE_CTA = "pages_manage_cta";

        /*
            Com a permissão pages_manage_instant_articles, seu aplicativo pode gerenciar Instant Articles em nome de Páginas do Facebook administradas por pessoas que usam o seu aplicativo.
            Uso permitido
            Criar e atualizar Instant Articles para as Páginas dos usuários do aplicativo.
       */
        public static final String PAGE_MANAGE_INSTANT_ARTICLES = "pages_manage_instant_articles";

        /*
            Com a permissão pages_manage_engagement, seu aplicativo pode criar, editar e excluir comentários publicados na Página.
            Uso permitido
            Publicar um comentário em uma publicação da Página.
            Atualizar seu comentário em uma publicação da Página.
            Excluir um comentário de uma publicação da Página.
            Curtir uma publicação da Página ou remover a curtida.
        */
        public static final String PAGE_MANAGE_ENGAGEMENT = "pages_manage_engagement";

        /*
            Com a permissão pages_manage_metadata, seu aplicativo pode inscrever e receber webhooks sobre atividade na Página, bem como atualizar configurações nela.
            Uso permitido
            Inscrever-se para receber webhooks da Página.
            Atualizar configurações da Página.
        */
        public static final String PAGE_MANAGE_METADA = "pages_manage_metadata";

        /*
            Com a permissão pages_manage_posts, seu aplicativo pode criar, editar e excluir publicações na sua Página.
            Uso permitido
            Fazer uma publicação, compartilhar uma foto ou um vídeo na sua Página.
            Atualizar uma publicação, foto ou vídeo na sua Página.
            Excluir uma publicação, foto ou vídeo na sua Página.
        */
        public static final String PAGE_MANAGE_POSTS = "pages_manage_posts";

        /*
            Com a permissão pages_messaging, seu aplicativo pode gerenciar e acessar conversas da Página no Messenger.
            Uso permitido
            Criar experiências interativas iniciadas por um usuário.
            Confirmar interações do cliente, como compras, pedidos e reservas.
            Enviar mensagens de solicitação de suporte.
        */
        public static final String PAGES_MESSAGING = "pages_messaging";

        /*
            Com a permissão pages_read_engagement, seu aplicativo pode ler conteúdo (publicações, fotos, vídeos e eventos) publicados pela Página, ler dados de seguidores (incluindo nome e PSID) e foto do perfil, bem como ler metadados e outras informações sobre a Página.
            Uso permitido
            Obter conteúdo publicado por sua Página.
            Obter nomes, PSIDs e fotos de perfil dos seguidores da sua Página.
            Obter metadados sobre sua Página.
        */
        public static final String PAGES_READ_ENGAGEMENT = "pages_read_engagement";

        /*
            Com a permissão pages_read_user_content, seu aplicativo lê o conteúdo gerado pelo usuário na Página, como publicações, comentários e classificações dos usuários ou de outras Páginas, bem como exclui comentários do usuário nas publicações da Página.
            Uso permitido
            Obter conteúdo gerado por usuários na sua Página.
            Obter publicações nas quais sua Página está marcada.
            Excluir comentários publicados por usuários na sua Página.
        */
        public static final String PAGES_READ_USER_CONTENT = "pages_read_user_content";

        /*
            Com a permissão pages_show_list, seu aplicativo pode acessar a lista das Páginas que uma pessoa gerencia.
            Uso permitido
            Mostrar apenas para uma pessoa a lista das Páginas que ela gerencia, a menos que essa pessoa permita que você mostre a lista para outras pessoas.
        */
        public static final String PAGES_SHOW_LIST = "pages_show_list";

        /*
            Com a permissão pages_user_gender, seu aplicativo acessa o gênero de um usuário por meio da Página à qual o aplicativo está conectado.
            Uso permitido
            Personalizar experiências ou recomendações conforme o gênero.
            Usar idioma com conceito de gênero, como pronomes e títulos corretos.
        */
        public static final String PAGES_USER_GENDER = "pages_user_gender";

        /*
            Com a permissão pages_user_locale, seu aplicativo pode acessar o idioma de um usuário por meio da Página à qual o aplicativo está conectado.
            Uso permitido
            Personalizar experiências com base no idioma de uma pessoa mostrando conteúdo específico ao idioma.
            Enviar respostas no idioma preferido da pessoa.
            Exibir números, horas e datas corretamente no idioma da pessoa.
        */
        public static final String PAGES_USER_LOCALE = "pages_user_locale";

        /*
            Com a permissão pages_user_timezone, seu aplicativo pode acessar o fuso horário de um usuário por meio da Página à qual o aplicativo está conectado.
            Uso permitido
            Evitar que mensagens sejam enviadas em um horário inconveniente.
            Enviar conteúdo sensível ou notícias recorrentes em um horário específico.
            Fornecer conteúdo personalizado com base no horário.
            Enviar saudações apropriadas ao horário.
        */
        public static final String PAGES_USER_TIMEZONE = "pages_user_timezone";

        /*
            Com a permissão publish_to_groups, seu aplicativo pode publicar conteúdo em um Grupo em nome de uma pessoa desde que ela tenha concedido acesso a ele para tanto.
            Uso permitido
            Permitir que as pessoas publiquem conteúdo do seu aplicativo no grupo do Facebook delas.
            Ajudar as pessoas a gerenciar o conteúdo publicado no grupo delas.
       */
        public static final String PUBLISH_TO_GROUPS = "publish_to_groups";

        /*
            Com a permissão publish_video, seu aplicativo pode publicar vídeos ao vivo na linha do tempo de um usuário dele.
            Uso permitido
            Concede ao aplicativo permissão para publicar vídeos ao vivo na linha do tempo do usuário do aplicativo.
       */
        public static final String PUBLISH_VIDEO = "publish_video";

        /*
            Com a permissão read_insights, seu aplicativo poder ler os dados sobre informações de Páginas, aplicativos e domínios da web que pertencem à pessoa.
            Uso permitido
            Integrar em suas próprias ferramentas de análise as informações sobre o aplicativo, a página ou o domínio do Facebook.
       */
        public static final String READ_INSIGHTS = "read_insights";

        /*
            Com a permissão user_age_range, seu aplicativo pode acessar a faixa etária da pessoa conforme publicado no perfil dela no Facebook.
            Uso permitido
            O aplicativo deve ter classificação etária por exigência legal.
            O aplicativo inclui conteúdo inadequado para a base geral de usuários do Facebook, como conteúdo de encontros, adulto ou violento.
        */
        public static final String USER_AGE_RANGE = "user_age_range";

        /*
            Com a permissão user_brithday, seu aplicativo pode ler o aniversário de uma pessoa conforme publicado no perfil dela no Facebook.
            Uso permitido
            Fornecer às pessoas conteúdo compatível com a respectiva idade quando a faixa etária não for suficiente.
       */
        public static final String USER_BIRTHDAY = "user_birthday";

        /*
            Com a permissão user_friends, seu aplicativo pode obter uma lista dos amigos de uma pessoa usando esse aplicativo.
            Uso permitido
            Fornecer conteúdo relacionado ao Facebook para personalizar a experiência de uma pessoa.
       */
        public static final String USER_FRIENDS = "user_friends";

        /*
            Com a permissão user_gender, seu aplicativo pode ler o gênero de uma pessoa conforme publicado no perfil dela no Facebook.
            Uso permitido
            Para dar pronomes.
            Personalizar a experiência de uma pessoa conforme o gênero. Por exemplo, aplicativos de encontros, compras e moda.
       */
        public static final String USER_GENDER = "user_gender";

        /*
            Com a permissão user_hometown, seu aplicativo pode ler a cidade natal de uma pessoa conforme publicada no perfil dela no Facebook.
            Uso permitido
            Fornecer uma experiência personalizada com base no local em que uma pessoa viveu ou cresceu.
       */
        public static final String USER_HOMETOWN = "user_hometown";

        /*
            Com a permissão user_likes, seu aplicativo pode ler uma lista de todas as Páginas do Facebook que um usuário já curtiu.
            Uso permitido
            Melhore a qualidade da experiência de um usuário no seu aplicativo (deve ficar claro para o usuário como os dados dele são usados para fornecer essa experiência).
       */
        public static final String USER_LIKES = "user_likes";

        /*
            Com a permissão user_link, seu aplicativo pode acessar a URL de perfil do Facebook da pessoa que está usando o aplicativo.
            Uso permitido
            Fornecer a uma pessoa que usa seu aplicativo uma maneira de visitar o perfil de outra pessoa no Facebook.
        */
        public static final String USER_LINK = "user_link";

        /*
            Com a permissão user_location, seu aplicativo pode ler o nome da cidade, conforme exibido no campo de localização do perfil de uma pessoa no Facebook.
            Uso permitido
            Fornecer uma experiência personalizada com base no nome da cidade, conforme exibido no campo de localização do perfil de uma pessoa no Facebook.
        */
        public static final String USER_LOCATION = "user_location";

        /*
            Com a permissão user_photos, seu aplicativo pode ler as fotos de uma pessoa carregadas no Facebook.
            Uso permitido
            Criar livros ou álbuns físicos ou digitais das fotos de uma pessoa, o que inclui permitir que as pessoas exportem fotos para impressão.
            Fornecer às pessoas a capacidade de exibir fotos com outros usuários do aplicativo. Por exemplo, em aplicativos sociais ou de encontros.
            Fornecer às pessoas a capacidade de editar ou criar novo conteúdo fotográfico com base em fotos existentes.
        */
        public static final String USER_PHOTOS = "user_photos";

        /*
            Com a permissão user_posts, seu aplicativo pode acessar as publicações que um usuário fez na própria linha do tempo.
            Uso permitido
            Melhore a qualidade da experiência de um usuário no seu aplicativo (deve ficar claro para o usuário como os dados dele são usados para fornecer essa experiência).
        */
        public static final String USER_POSTS = "user_posts";

        /*
            Com a permissão user_videos, seu aplicativo pode ler uma lista de vídeos carregados por uma pessoa.
            Uso permitido
            Exibir os vídeos de uma pessoa em uma TV por meio de um set-top box ou em um porta-retrato digital.
            Fornecer às pessoas a capacidade de editar ou criar novo conteúdo de vídeo usando vídeos existentes.
            Fornecer às pessoas a capacidade de exibir vídeo com donos no aplicativo. Por exemplo, em aplicativos sociais ou de encontros.
        */
        public static final String USER_VIDEOS = "user_videos";

        /*
            CCom a permissão whatsapp_business_management, seu aplicativo pode ler e/ou gerenciar ativos de negócios do WhatsApp que você possui ou aos quais outras empresas concederam acesso a você por meio dessa permissão.
            Uso permitido
            Exibir análises de conta do WhatsApp Business no seu portal comercial.
            Gerenciar os modelos de mensagem das suas contas do WhatsApp Business.
        */
        public static final String WHATSAPP_BUSINESS_MANAGEMENT = "whatsapp_business_management";

        /*
            Obsoletas a partir da versão 7.0.
        */
        public static final String MANAGE_PAGES = "manage_pages";

        /*
            Obsoletas a partir da versão 7.0.
        */
        public static final String PUBLISH_PAGES = "publish_pages";

        /*
            Com a permissão instagram_graph_user_media, seu aplicativo lê o nó de mídia, que representa uma imagem, um vídeo ou um álbum, e ainda as bordas do nó.
            Uso permitido
            Criação de livros físicos ou digitais das fotos do usuário do aplicativo, incluindo exportação de fotos para imprimir.
            Exibição das fotos dos usuários do aplicativo para outros usuários. Por exemplo, com aplicativos sociais ou de encontros.
            Edição ou criação de novas fotos ou vídeos com base nas fotos e vídeos existentes do usuário do aplicativo (por exemplo, para aplicativos de edição de fotos ou de vídeos).
            Exibição das fotos e dos vídeos do usuário do aplicativo em um dispositivo externo, como uma TV ou um porta-retrato digital.
        */
        public static final String INSTAGRAM_GRAPH_USER_MEDIA = "instagram_graph_user_media";

        /*
            A permissão instagram_graph_user_profile possibilita que o aplicativo leia o perfil do usuário do aplicativo.
            Uso permitido
            Ler campos em um perfil do usuário no Instagram. Por exemplo, o número de identificação e o tipo de conta do usuário.
        */
        public static final String INSTAGRAM_GRAPH_USER_PROFILE = "instagram_graph_user_profile";
    }
}
