package com.JotaSolutions.APC;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.JotaSolutions.APC.main.main.MainActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements RoomListener {


    private String channelID = "X3K94fMSG5bPTYzj";
    private String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        MobileAds.initialize(this, "ca-app-pub-4460189410667954~1374009895");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4460189410667954/8242670579");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        MemberData data = new MemberData(getRandomName(), getRandomColor());

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone conexion abierta");
                scaledrone.subscribe(roomName, MainActivity2.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.salir:
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish(roomName, message);
            editText.getText().clear();
        }
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Conectada a la sala");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    @Override
    public void onMessage(Room room, final JsonNode json, final Member member) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final MemberData data = mapper.treeToValue(member.getClientData(), MemberData.class);
            boolean belongsToCurrentUser = member.getId().equals(scaledrone.getClientID());
            final Message message = new Message(json.asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageAdapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String getRandomName() {
        String[] adjs = {"poodle", "labrador", "pastor", "salchicha", "leon", "gato", "perico", "elefante", "jirafa", "gorrion", "mariposa", "raton", "serpiente", "lagartija", "culebra", "sabueso", "rottwailer", "caniche", "pug", "yorkshire", "beagle", "weathered", "blue", "billowing", "boxer", "bulldog", "gatito", "perrito", "chowchow", "dogo", "husky", "corgi", "pastore", "abeja", "chinita", "butterfly", "mantis", "mono", "tigresa", "oso", "gorila", "perro", "patito", "paloma", "cotorra", "catita", "rinoceronte", "mammuth", "pantera", "hiena", "chimpance", "oruga", "GatoJuanito", "Calcetin", "oveja", "chinchilla", "pez", "ballena", "tiburon", "caracol", "belga", "cachorro", "mascota", "adoptar"};
        String[] nouns = {"toy", "blanco", "belga", "rojo", "azul", "amarrillo", "rosa", "rosita", "rojito", "azulito", "amarrillito", "negro", "negrito", "gris", "blanquito", "especial", "interesante", "unico", "unica", "lindo", "linda", "feliz", "triste", "verde", "verdesito", "naranja", "naranjito", "cafe", "cafecito", "burdeo", "fantastica", "siberiano", "responsable", "educado", "brillante", "opaca", "religiosa", "del oriente", "panda", "gigante", "pequeñito", "pequeño", "del cielo", "violeta", "morado", "trueno", "rayo", "calido", "gelido", "dulce", "amargo", "con Rombosman", "negra", "fuera del agua", "espada", "tigre", "orca", "aqua", "congelado", "pastor", "de amor", "sensual", "prodigiosa", "estrella"};
        return (
            adjs[(int) Math.floor(Math.random() * adjs.length)] +
            "_" +
            nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}

class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

}
