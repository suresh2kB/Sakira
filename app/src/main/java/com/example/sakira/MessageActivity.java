package com.example.sakira;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sakira.Adapter.MessageAdapter;
import com.example.sakira.Models.Chat;
import com.example.sakira.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParametersSpi;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageActivity extends AppCompatActivity {

    static String key;

//************** AES ****************//

    private byte[] encryptionKeyAes = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher enCipherAes,deCipherAes;
    private SecretKeySpec secretKeySpecAes;

    //******************    *****************//


    //************** DES ****************//
//    KeyGenerator kg = KeyGenerator.getInstance("DES");
//    SecretKey myDESKey = kg.generateKey();

    private Cipher enCipherDes,deCipherDes;
    private SecretKeySpec secretKeySpecDes;

    //******************    *****************//

    String data;

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;


    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;



    public MessageActivity() throws NoSuchAlgorithmException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        data = MainActivity.getActivityInstance().getData();
        Toast.makeText(this, "Selected Algorithm is "+data, Toast.LENGTH_SHORT).show();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        text_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //*************AES**************//
        try {
            enCipherAes = Cipher.getInstance("AES");
            deCipherAes = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        secretKeySpecAes = new SecretKeySpec(encryptionKeyAes,"AES");

        //******************      *****************//


        //*************DES**************//
//        try {
//            enCipherDes = Cipher.getInstance("AES");
//            deCipherDes = Cipher.getInstance("AES");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
//        secretKeySpecDes = new SecretKeySpec(encryptionKeyAes,"AES");
//
//        //******************      *****************//


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if(!msg.equals(""))
                {
                    try {
                        sendMessage(firebaseUser.getUid(),userid,msg);
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(MessageActivity.this, "Please Enter some message", Toast.LENGTH_SHORT).show();
                }

                text_send.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance("https://sakira2-16914-default-rtdb.firebaseio.com/").getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }

                readMessage(firebaseUser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String sender,String receiver,String message) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://sakira2-16914-default-rtdb.firebaseio.com/").getReference();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        if(data.equals("AES"))
        {
            hashMap.put("message",AesEnc(message));
            hashMap.put("algo","AES");
        }
        else if(data.equals("DES"))
        {
            hashMap.put("message",DesEnc(message));
            hashMap.put("algo","DES");
        }
        else{
            hashMap.put("message",(message));
            hashMap.put("algo","NO");
        }

//        AesActivity.DesEnc1()


        reference.child("Chats").push().setValue(hashMap);

    }


    private void readMessage(final String myid, final String userid, final String imageurl)
    {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://sakira2-16914-default-rtdb.firebaseio.com/").getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                        if(chat.getAlgo().equals("AES"))
                        {
                            String s = null;
                            try {
                                s = AesDec(chat.getMessage());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            chat.setMessage(s);
                            mChat.add(chat);
                        }
                        else if(chat.getAlgo().equals("DES"))
                        {
                            String s = "Resolving some error";
                            try {
                                s = DesDec(chat.getMessage());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            chat.setMessage(s);
                            mChat.add(chat);
                        }
                        else{
                            mChat.add(chat);
                        }


                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // ****************AES FUNCTIONS ****************//

    private String AesEnc(String string){

        byte[] stringBytes = string.getBytes();
        byte[] EncryptedBytes = new byte[stringBytes.length];

        try {
            enCipherAes.init(Cipher.ENCRYPT_MODE, secretKeySpecAes);
            EncryptedBytes = enCipherAes.doFinal(stringBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String FinalString = null;

        try {
            FinalString = new String(EncryptedBytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return FinalString;
    }

    public String AesDec(String string) throws UnsupportedEncodingException {

        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = null;

        byte[] decryption;

        try {
            deCipherAes.init(Cipher.DECRYPT_MODE, secretKeySpecAes);
            decryption = deCipherAes.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }

    //***************         ******************//


    //************** DES *****************//

    public String DesEnc(String decrypted) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        String res="";
        String desKey = "0123456789abcdef"; // user value (24 bytes)
        byte[] keyBytes = desKey.getBytes();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey key = factory.generateSecret(new DESKeySpec(keyBytes));

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] text = decrypted.getBytes();
        byte[] textEnc = cipher.doFinal(text);

        res = new String(textEnc);

        return res;
    }

    //**************  *****************//

    public String DesDec(String decrypted) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        String res="";
        String desKey = "0123456789abcdef"; // user value (24 bytes)
        byte[] keyBytes = desKey.getBytes();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
        SecretKey key = factory.generateSecret(new DESKeySpec(keyBytes));

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] text = decrypted.getBytes();
        byte[] textEnc = cipher.doFinal(text);

        res = new String(textEnc);

        return res;
    }

    //***************    ******************








}