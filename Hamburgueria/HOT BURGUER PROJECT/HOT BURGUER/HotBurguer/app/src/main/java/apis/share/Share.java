package apis.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class Share {


    public static void shareApp(Context context, String title, String uri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Adiciona os dados na intent
        intent.putExtra(Intent.EXTRA_TEXT,uri);
        context.startActivity(Intent.createChooser(intent,title));
    }

    public static class DynamicLinkShare{

        public static final void shareProduct(Activity activity, String link, String dynamicLinkDomain, int idProd, String title, String description, String copy){

            StringBuilder urlImage = new StringBuilder();
            urlImage.append("https://comprazencasa.com.br/AppMobile/hot-products/get-image/");
            urlImage.append(idProd);

            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(link))
                    //.setDomainUriPrefix(dynamicLinkDomain)
                    .setDynamicLinkDomain(dynamicLinkDomain)
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                            .setTitle(title)
                            .setDescription(description)
                            .setImageUrl(Uri.parse(urlImage.toString()))
                            .build())
                    .buildDynamicLink();

            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(dynamicLink.getUri())
                    .buildShortDynamicLink()
                    .addOnCompleteListener(activity, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                // Short link created
                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();

                                shortLink = Uri.parse(shortLink.toString());

                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.setType("text/plain");
                                sendIntent.putExtra(Intent.EXTRA_TEXT, copy+"\n\n"+shortLink.toString());
                                activity.startActivity(Intent.createChooser(sendIntent, null));
                            } else {
                                // Error
                                // ...
                                Toast.makeText(activity, "Erro ao compartilhar!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        public static final void getProductShared(Activity activity, OnSuccessListener<PendingDynamicLinkData> onSuccessListener){
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(activity.getIntent())
                    .addOnSuccessListener(activity,onSuccessListener);
        }
    }
}
