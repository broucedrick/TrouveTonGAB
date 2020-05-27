package com.example.trouvetongab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "login" ;

    SignInButton button_connection_ggle;
    LoginButton button_connection_fb;

    CardView btnfb, btngoogle;

    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 0 ;
    private static final int SIGN_FB = 0 ;

    String userName = "";
    String userEmail ="";
    String userFname ="";
    String userDname = "";
    String info = "";
    private String nom;
    private String nom_fb;
    private String email_ggle;
    private String email;
    String name;
    LoadingDialog loadingDialog;
    CheckBox checkBox;
    TextView condition;
    GoogleSignInClient mGoogleSignInClient;

    private CallbackManager callbackManager;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if (ContextCompat.checkSelfPermission(login.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

// Permission is not granted
// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(login.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(login.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
// Permission has already been granted
        }

        setContentView(R.layout.activity_login);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        condition = (TextView) findViewById(R.id.condition);
        condition.setText("Conditions d'utilisation relative à l'application");
        condition.setClickable(true);
        condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, InfosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("i_title", "Conditions Générales d'Utilisation\n(CGU)");
                bundle.putString("i_info", "ARTICLE 1 – DEFINITIONS \n" +
                        "\n" +
                        "Aux fins des présentes, on entend par :\n" +
                        "\n" +
                        "Application TTG : l’application mobile de Digitale Finances dénommée TROUVE TON GAB (TTG) \n" +
                        "\n" +
                        "Conditions Générales d’Utilisation ou CGU : les présentes conditions générales d’utilisation de l’Application Mobile Banking.\n" +
                        "\n" +
                        "Contrat : les présentes CGU ainsi que leurs annexes et/ou Conditions Particulières, conclues entre Digitale Finances  et l’Utilisateur. \n" +
                        "\n" +
                        "Données d’Identification : les données permettant à l’Utilisateur d’accéder à son Espace Personnel, composées selon le cas d’un numéro de téléphone, adresse mail.\n" +
                        "\n" +
                        "Données Personnelles : toutes les informations à caractère personnel concernant un Utilisateur.\n" +
                        "\n" +
                        "Espace Personnel : l’espace dédié de l’Utilisateur dans l’Application TTG.\n" +
                        "\n" +
                        "Services : les services actuels et futurs mis à la disposition de l’Utilisateur à travers l’Application TTG\n" +
                        "\n" +
                        "Utilisateur : la personne physique majeure et capable qui est détenteur d’un moyen de payement électronique (cartes bancaires, wallet to bank ,etc) , client ou non d’une banque de la place et qui utilise tout ou partie des Services.\n" +
                        "\n" +
                        "ARTICLE 2 – OBJET \n" +
                        "\n" +
                        "Les présentes Conditions Générales d’Utilisation régissent les relations contractuelles entre Digitale Finances et l’Utilisateur dans le cadre de l’Application TTG  et définissent les conditions et modalités du service. \n" +
                        "\n" +
                        "Les CGU constituent, avec, le cas échéant, les annexes et d’éventuelles Conditions Particulières, les documents contractuels qui s’imposent à l’Utilisateur. Si une ou plusieurs clauses de ces documents devaient être déclarées nulles, invalides ou sans effet, et pour quelque cause que ce soit, les autres clauses garderont toutefois leur plein effet.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 3 – DESCRIPTION DES SERVICES OFFERTS PAR L’APPLICATION MOBILE BANKING\n" +
                        "\n" +
                        "TROUVE TON GAB est un service innovant, performant et intuitif qui vous permet d’informer les clients  (de toutes les banques ) de la disponibilité de leur GAB/DAB  en live en plus de leur géolocalisation, et ce , depuis votre smartphone ou tablette. \n" +
                        "TTG permet également la géolocalisation des agences bancaires de toutes les banques locales.\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 4 – SOUSCRIPTION - ABONNEMENT\n" +
                        "\n" +
                        "\uF0A7\tLa souscription à l’Application se fait sur le site institutionnel de Digitale Finances (www.digitalefinances.com/trouvetongab  ou depuis les plateformes de téléchargement d’application en ligne que sont l’App Store ou Google Play et l’installer sur son appareil mobile.\n" +
                        "\n" +
                        " \n" +
                        "La souscription peut également se faire à distance en suivant les étapes suivantes :\n" +
                        "\n" +
                        "\uF0A7\tTélécharger l’Application TTG depuis les plateformes de téléchargement d’application en ligne que sont l’App Store ou Google Play et l’installer sur son appareil mobile.\n" +
                        "\uF0A7\tAccepter les présentes CGU en cliquant sur la case « j’accepte les CGU envoyée sous forme de cookies ;\n" +
                        "\uF0A7\tChoisir sa banque pour voir les GAB/DAB ou agences bancaires disponibles avec leurs géolocalisation et les horaires d’ouverture et fermeture ;\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 5 – SAISIE DES INFORMATIONS D’IDENTIFICATION\n" +
                        "\n" +
                        "La saisie par l’Utilisateur et la validation des informations d’identification, par Digitale Finances, emporte automatiquement la synchronisation, sur le profil créé, de toutes les informations liées aux comptes de l’Utilisateur qu’il a entendu associer à l’Application.\n" +
                        "\n" +
                        "Les informations relatives aux comptes de l’Utilisateur sont actualisées de manière automatique.\n" +
                        "\n" +
                        "ARTICLE 6 – ACCES AUX SERVICES\n" +
                        "\n" +
                        "Pour avoir accès aux Services, l’Utilisateur se connecte à l’Application en renseignant ses Données d’Identification.\n" +
                        "\n" +
                        "Lorsque l’Utilisateur est connecté, s’il ne se déconnecte pas manuellement, il reste connecté, même s’il n’est plus dans l’Application.\n" +
                        "\n" +
                        "Si l’Utilisateur se déconnecte manuellement de l’Application, il devra à nouveau renseigner ses Données d’Identification pour accéder à nouveau à son profil.\n" +
                        "\n" +
                        "ARTICLE 7 – SECURITE\n" +
                        " \n" +
                        "Les Données d’Identification sont définies par l’Utilisateur au moment de sa souscription.\n" +
                        "\n" +
                        "L’Utilisateur s’oblige à prendre toutes les mesures et précautions propres à assurer la sécurité de son appareil mobile, de ses Données d’Identification et de ses informations relatives à ses opérations sur son profil. Il s’oblige à les tenir absolument secrètes et à ne les divulguer à personne sous aucun prétexte.\n" +
                        "\n" +
                        "Tout accès au profil de l’Utilisateur au moyen de ses Données d’Identification, est présumé de son fait. \n" +
                        "\n" +
                        "Digitale Finances  s’oblige à faire tout son possible en vue d’assurer la sécurité du profil et des Données d’Identification de l’Utilisateur.\n" +
                        "\n" +
                        "Compte tenu toutefois de la complexité de l’environnement internet et des systèmes d’information, Digitale Finances n’est tenue, au titre de son engagement de sécurisation du profil et des Données d’Identification de l’Utilisateur, qu’à une obligation de moyen.\n" +
                        "\n" +
                        "Tout accès frauduleux au profil et aux Données d’Identification de l’Utilisateur, toute altération de son profil, sont interdits et feront l’objet de poursuites judiciaires.\n" +
                        "\n" +
                        "L’ensemble des informations, incluant les informations des opérations exécutées par l’Utilisateur, est conservé sous la responsabilité de Digitale Finances qui en garantit l’intégrité. \n" +
                        "\n" +
                        "L’Utilisateur s’engage à ne commettre aucun acte qui pourrait mettre en cause la sécurité des systèmes d’information de Digitale Finances.\n" +
                        "\n" +
                        "Il est interdit à l’Utilisateur d’accéder aux Services de l’Application TTG  sur un appareil mobile dont il n’est pas propriétaire ou dont l’usage est partagé avec une tierce personne.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 8 – DISPONIBILITE DE L’APPLICATION TTG\n" +
                        "\n" +
                        "L’Application et les Services sont en principe disponible 24 heures/24 et 7 jours sur 7. \n" +
                        "\n" +
                        "En cas de panne, de maintenance ou de mise à jour des systèmes, l’accès à l’Application est suspendu temporairement. L’Utilisateur en sera notifié sur son appareil mobile.\n" +
                        "\n" +
                        "ARTICLE 9 – PROPRIETE INTELLECTUELLE \n" +
                        "\n" +
                        "L’Application Mobile Banking, l’ensemble des Services et chacun des éléments qui lui sont associés pour sa fonctionnalité (les programmes et fichiers informatiques, les logiciels, progiciels interfaces, base de données, savoir-faire, données, textes, articles, lettres d’informations, communiqués, présentations, brochures, illustrations, photographies, animations, et toutes autres informations, sans que cette liste soit limitative) sont la propriété intellectuelle exclusive de Digitale Finances.\n" +
                        "L’utilisation de l’Application, objet des présentes conditions générales, ne confère nullement à l’Utilisateur, un quelconque droit de propriété, de quelque nature que ce soit et la mise à disposition des services dédiés, ne saurait s’analyser en un transfert quelconque de propriété au profit de l’Utilisateur.\n" +
                        "\n" +
                        "L’Utilisateur s’interdit donc de vendre, prêter, nantir, licencier, reproduire, diffuser, communiquer au public, commercialiser, louer, adapter, modifier, transformer, décompiler tout élément de l’Application, dans sa forme originale ou dans une forme modifiée. \n" +
                        "\n" +
                        "L'Utilisateur s'engage expressément à ce que l'utilisation de l'Application ne porte en aucun cas atteinte aux droits de Digitale Finances, et notamment à ce que cette utilisation ne constitue pas un acte de contrefaçon, de concurrence déloyale ou parasitaire de l’Application ou de son contenu.\n" +
                        "\n" +
                        "ARTICLE 10 – PROTECTION DES DONNEES A CARACTERE PERSONNEL\n" +
                        "\n" +
                        "L’Utilisateur reconnait que Digitale Finances est amenée à collecter des données à caractère personnel le concernant à l’occasion de la conclusion et de l’exécution des présentes. \n" +
                        " \n" +
                        "Ces données feront l’objet de traitement afin de permettre la gestion de la relation Client et des Services offerts par l’Application, la gestion des risques, la prévention de la fraude, la mise en place d’actions commerciales et le respect des obligations légales et règlementaires.\n" +
                        "\n" +
                        "L’Utilisateur autorise, de convention expresse, Digitale Finances à collecter, traiter et communiquer les données à caractère personnel pour son compte ou celui des prestataires et sous-traitants qui interviennent pour son compte.\n" +
                        " \n" +
                        "L’Utilisateur peut s’opposer, pour des motifs légitimes, à ce que les données à caractère personnel fassent l’objet de tels traitements.\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 11 - LUTTE CONTRE LA FRAUDE, LE BLANCHIMENT DE CAPITAUX ET LE FINANCEMENT DU TERRORISME\n" +
                        "\n" +
                        "Digitale Finances se réserve le droit de ne pas exécuter toute opération qu’elle jugerait non conforme.\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 12 - RESPONSABILITE DE DIGITALE FINANCES\n" +
                        "\n" +
                        "Digitale Finances est responsable de la fourniture des Services offerts par l’Application Mobile Banking, laquelle est limitée aux seuls dommages directs subis par l’Utilisateur à raison de l’utilisation de l’Application.\n" +
                        "\n" +
                        "Digitale Finances ne peut être tenue responsable de tous dommages indirects causés à l’Utilisateur, comme la perte de clientèle, la perte de profit, le manque à gagner ou l’atteinte à son image.\n" +
                        " Digitale Finances ne pourra être tenue responsable en cas de dommage subi par l’Utilisateur et résultant de :\n" +
                        "- Une faute de l’Utilisateur ;\n" +
                        "- Un non-respect par l’Utilisateur des conditions générales d’utilisation ;\n" +
                        "- L'accès par un tiers à ses informations d’identification sur autorisation de l’Utilisateur ;\n" +
                        "- L’usage frauduleux ou abusif de l’Application Mobile Banking par l’Utilisateur ;\n" +
                        "- L’interruption ou la défaillance de l’Application, suite à des dysfonctionnements du réseau Internet, du réseau des télécommunications ou du réseau informatique ;\n" +
                        "- L’impossibilité de fournir les Services offerts par l’Application, notamment, en cas d’indisponibilité de la connexion ou pour des raisons indépendantes de la volonté de Digitale Finances;\n" +
                        "\n" +
                        "ARTICLE 13 - RESPONSABILITE DE L’UTILISATEUR\n" +
                        "\n" +
                        "L’Utilisateur s’oblige à utiliser l’Application et les Services offerts dans le respect des stipulations des conditions générales d’utilisation ainsi que de la règlementation en vigueur.\n" +
                        "\n" +
                        "L’Utilisateur est responsable de la bonne exécution des obligations qui lui incombent en vertu des présentes conditions générales d’utilisation.\n" +
                        "\n" +
                        "Il est responsable de tous dommages et intérêts qui pourraient être réclamés par tout tiers à Digitale Finances , ainsi que de toute plainte, actions, poursuite ou condamnation de Digitale Finances, en cas de non-respect par lui, d’une ou plusieurs obligations découlant des conditions générales d’utilisation et s’oblige à indemniser Digitale Finances.\n" +
                        "\n" +
                        "ARTICLE 14 – DURÉE  \n" +
                        "\n" +
                        "Les présentes conditions générales d’utilisation sont édictées pour une durée indéterminée à compter de leur acceptation par l'Utilisateur.\n" +
                        "\n" +
                        "ARTICLE 15 - RESILIATION \n" +
                        "\n" +
                        "15.1\tL’Utilisateur peut résilier, à tout moment, sans frais, son abonnement en notifiant sa décision à Digitale Finances, par tout moyen laissant trace écrite.\n" +
                        "\n" +
                        "15.2\t Digitale Finances  peut résilier le contrat en respectant un préavis d'au moins un (01) mois. Elle informe l’utilisateur par tout moyen laissant trace écrite et notamment par courrier électronique.\n" +
                        "\n" +
                        "Toutefois, Digitale Finances se réserve le droit de procéder à la résiliation, sans préavis, en cas de liquidation des biens de l’Utilisateur, de fraude, d’usage abusif de l’Application, à des fins notamment de blanchiment de capitaux, de financement du terrorisme et en cas de violation par l’Utilisateur de ses obligations contenu dans les présentes conditions générales d’utilisation.\n" +
                        " \n" +
                        "ARTICLE 16 – CONDITIONS FINANCIÈRES\n" +
                        "\n" +
                        "L’Application Mobile Banking est fournie gratuitement à l’Utilisateur dès son lancement mais pourrait à terme connaître une facturation selon l’ évolution des différents services associés.\n" +
                        " \n" +
                        "L’accès à l’ensemble des Services tel que mentionné à l’article 3 ci-dessus est également gratuit pour l’Utilisateur.\n" +
                        "\n" +
                        "\n" +
                        "ARTICLE 17 – MODIFICATIONS DES CONDITIONS GENERALES D’UTILISATION DE L’APPLICATION\n" +
                        "\n" +
                        "Digitale Finances  se réserve le droit d’apporter, à tout moment, des modifications, à tout ou partie des présentes conditions générales, en vue de les adapter aux évolutions de l’Application ou de l’offre de services.\n" +
                        "\n" +
                        "Digitale Finances notifiera le projet des modifications à l’Utilisateur, par tout moyen laissant trace écrite et notamment par courrier électronique, en même temps que la date prévue pour leur entrée en vigueur et au plus tard un (01) mois avant. \n" +
                        "\n" +
                        "L’Utilisateur dispose d’un délai de quinze (15) jours à compter de la notification des modifications pour refuser celles-ci.\n" +
                        "\n" +
                        "L’Utilisateur est réputé avoir accepté les modifications s'il n’a pas notifié son refus à Digitale Finances avant la date d'entrée en vigueur proposée de ces modifications.\n" +
                        "\n" +
                        "Si l’Utilisateur refuse les modifications, il peut, si bon lui semble, résilier sans frais le contrat, avant la date d'entrée en vigueur proposée des modifications.\n" +
                        "\n" +
                        "Si l’Utilisateur ne signifie pas son refus des modifications, et continue à utiliser l’Application et les Services offerts après la date de leur entrée en vigueur, Digitale Finances ne pourra en aucun cas, être tenue pour responsable d’un quelconque préjudice, résultant de cette utilisation.\n" +
                        "\n" +
                        "ARTICLE 18 - SECRET PROFESSIONNEL\n" +
                        "\n" +
                        "Digitale Finances est tenue au secret professionnel. Toutefois, ce secret peut être levé, conformément à la législation en vigueur, en vertu d’une obligation légale, règlementaire et prudentielle, notamment à la demande régulière des autorités de tutelle, de l’administration fiscale ou douanière, ainsi qu’à celle de toute autorité judiciaire.\n" +
                        "\n" +
                        "L’Utilisateur autorise Digitale Finances à communiquer les informations le concernant entreprises extérieures pour l’exécution des travaux qu’elle peut sous-traiter. Bien entendu, toutes mesures sont prises pour assurer la confidentialité des informations transmises.\n" +
                        "\n" +
                        "ARTICLE 19 – DROIT APPLICABLE - REGLEMENT DES DIFFERENTS\n" +
                        "\n" +
                        "Les présentes Conditions Générales d’Utilisation de l’Application sont régies par le droit ivoirien.\n" +
                        "\n" +
                        "Tout litige découlant de l’interprétation, de l’exécution ou de la rupture de celles-ci, devra faire l’objet d’un règlement amiable par voie de négociation directe entre les Parties. \n" +
                        "\n" +
                        "La Partie faisant état de l’existence dudit litige, devra dès qu’elle en a connaissance, le notifier à l’autre Partie par écrit en précisant sa nature et en fournissant toute autre information pertinente qu’elle jugera nécessaire à sa compréhension. \n" +
                        "\n" +
                        "A défaut d’accord dans un délai de trente (30) jours à compter de la date de réception, de la notification, le litige sera soumis par la partie la plus diligente, au Tribunal de Commerce d’Abidjan.\n");
                i.putExtras(bundle);
                startActivity(i, bundle);
            }
        });

        button_connection_fb = findViewById(R.id.login_button);

        button_connection_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //loadingDialog.dismissDialog();

                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    if(checkBox.isChecked()){

                                    JSONObject data = response.getJSONObject();
                                    Intent i = new Intent(login.this, Home.class);

                                    name = me.optString("name");
                                    String email = me.optString("email");
                                    String id = me.optString("id");
                                    //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();


                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", id);
                                        bundle.putString("name",name);
                                        bundle.putString("fb", "fb");
                                        i.putExtras(bundle);
                                        startActivity(i, bundle);

                                        String fb = "facebook";

                                        //connection(login.this,name,email);
                                        storeUserData(name,email);
                                        if(email ==""){
                                            email = "email inconnu" + " "+id;
                                            connection(login.this,name,email,fb);

                                        }else{
                                            connection(login.this,name,email,fb);
                                        }
                                    }else{
                                        logout_fb();
                                        //finish();
                                        //startActivity(new Intent(getApplicationContext(), login.class));
                                        Toast.makeText(getApplicationContext(), "veillez cochez la case svp ", Toast.LENGTH_LONG).show();

                                    }


                      //  Toast.makeText(getApplicationContext(),data.toString(),Toast.LENGTH_LONG).show();

                                }
                            }

                        }).executeAsync();
            }
            @Override
            public void onCancel() {
            //    Toast.makeText(getApplicationContext(), "connection a facebook echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               loadingDialog.dismissDialog();
                loadingDialog.startWarningDialog_log();
            //    setContentView(R.layout.activity_login);
            }

            @Override
            public void onError(FacebookException error) {
               // Toast.makeText(getApplicationContext(), "connection a facebook echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               loadingDialog.dismissDialog();
                loadingDialog.startWarningDialog_log();
            //    setContentView(R.layout.activity_login);
              //  startActivity(new Intent(getApplicationContext(), login.class));

            }
        });

        //////////////////connnection google/////////////////
        //button_connection_ggle = findViewById(R.id.sign_in_button);
        btngoogle = (CardView) findViewById(R.id.btngoogle);
        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    google();
                }else{
                    //finish();
                    startActivity(new Intent(getApplicationContext(), login.class));
                    Toast.makeText(getApplicationContext(), "veuillez  cochez la case svp ", Toast.LENGTH_LONG).show();

                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

    }



    public void logout_fb() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, login.class));
            // Toast.makeText(getApplicationContext(),"facebook deconnecter",Toast.LENGTH_LONG).show();
        }
    }

    public void google(){
        loadingDialog = new LoadingDialog(login.this);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        loadingDialog.startLoadingDialog();
        startActivityForResult(intent,SIGN_IN);
    }
    private void clearPrefData(){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear().apply();
    }

    private void    storeUserData(String nom,String email){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("nom", nom);
        mEditor.putString("email", email);
        mEditor.apply();
    }




    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data ){
       super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           if(result.isSuccess()){
               loadingDialog.startLoadingDialog();
               handleSignInResult(result);
              // startActivity(new Intent(getApplicationContext(), MainActivity.class));
              // finish();
           }else{
               //Toast.makeText(getApplicationContext(), "connection a Google echoué verifier la connection internet ", Toast.LENGTH_LONG).show();
               loadingDialog.dismissDialog();
               loadingDialog.startWarningDialog_log();
               // startActivity(new Intent(getApplicationContext(), login.class));
           }
       }else{
            //Toast.makeText(getApplicationContext(), "facebook on result", Toast.LENGTH_LONG).show();

            if(checkBox.isChecked()){
                loadingDialog = new LoadingDialog(login.this);
                loadingDialog.startLoadingDialog();
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }else{
                //logout_fb();
                startActivity(new Intent(getApplicationContext(), login.class));
                Toast.makeText(getApplicationContext(), "veuillez cochez la case svp ", Toast.LENGTH_LONG).show();

            }
        }
    }
    protected void handleSignInResult(GoogleSignInResult result){
        String google = "google";
        if(result.isSuccess()){
            loadingDialog.dismissDialog();
            Intent i = new Intent(login.this, Home.class);
            GoogleSignInAccount account=result.getSignInAccount();
            userName = (account.getDisplayName());
            userEmail = (account.getEmail());
            userFname= (account.getFamilyName());

            Bundle bundle = new Bundle();
            bundle.putString("ggle",google);
            i.putExtras(bundle);

                connection(login.this,userName,userEmail,google);
               // Toast.makeText(getApplicationContext(),userName + userEmail ,Toast.LENGTH_LONG).show();
                storeUserData(userName,userEmail);
                startActivity(i, bundle);
                finish();





        }else{
            Toast.makeText(getApplicationContext(),"recuperation impossible ",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, login.class));

        }

    }

    public void logout_gl() {

        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(login.this, "reessayer ... ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  boolean connection(final Context context, final String username, final String usermail, final String plateforme){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"https://digitalfinances.innovstech.com/visiteur.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
             //  Toast.makeText(context,"connection bd login effectuer "+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"verifier la connection internet ",Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();
                loadingDialog.startWarningDialog_log();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("plateforme",plateforme);
                params.put("nom",username);
                params.put("email",usermail);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("plateforme",plateforme);
                params.put("nom",username);
                params.put("email",usermail);
                return params;
            }
        };
     //   Toast.makeText(context,usermail,Toast.LENGTH_LONG).show();

        queue.add(sr);
        return true;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.finishAffinity();
    }
}




