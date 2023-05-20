package com.midterm.restaurant_app.view;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.FragmentAccountBinding;
import com.midterm.restaurant_app.model.Account;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    private static final int PICK_IMAGE_REQUESR = 1;
    private ImageView ivUpload;
    private CircleImageView ivAvatar;
    private Uri avatarUri;
    private Button btnSave;
    private FragmentAccountBinding binding;


    private StorageReference storageReference;
    private StorageTask storageTask;
    private DatabaseReference databaseReference;

    private Account account;
    private EditText edtNameAcc;
    private EditText edtAddress;
    private EditText edtPhone;
    private EditText edtEmail;
    private EditText edtPass;


    public AccountFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Account");
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivUpload = view.findViewById(R.id.iv_upload);
        btnSave = view.findViewById(R.id.btn_save);


        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    updateAccount();
            }
        });

        ImageView imvNameUser = binding.penEditName;
        edtNameAcc = binding.editNameAcc;
        ImageView imvAddress = binding.penEditDateBirth;
        edtAddress = binding.editAddress;
        ImageView imvPhone = binding.penEditPhoneNumber;
        edtPhone = binding.editPhone;
        ImageView imvEmail = binding.penEditEmail;
        edtEmail = binding.editMail;
        ImageView imvPass = binding.penEditIDCard;
        edtPass= binding.editPass;


        imvNameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Nếu EditText không được kích hoạt, kích hoạt nó và thay đổi văn bản của Button
                if (!edtNameAcc.isEnabled()) {
                    edtNameAcc.setEnabled(true);
                    edtNameAcc.setBackgroundColor(Color.parseColor("#f09e98"));

                    //button.setText("Lưu");
                }
                // Ngược lại, vô hiệu hóa EditText và đặt lại văn bản của Button
                else {
                    edtNameAcc.setEnabled(false);
                    //button.setText("Chỉnh sửa");
                    edtNameAcc.setBackgroundColor(Color.parseColor("#f5c3c0"));
                }
            }
        });
        imvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtAddress.isEnabled()) {
                    edtAddress.setEnabled(true);
                    edtAddress.setBackgroundColor(Color.parseColor("#f09e98"));
                }
                else {
                    edtAddress.setEnabled(false);
                    edtAddress.setBackgroundColor(Color.parseColor("#f5c3c0"));
                }

            }
        });
        imvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edtPhone.isEnabled()) {
                    edtPhone.setEnabled(true);
                    edtPhone.setBackgroundColor(Color.parseColor("#f09e98"));
                }
                else {
                    edtPhone.setEnabled(false);
                    edtPhone.setBackgroundColor(Color.parseColor("#f5c3c0"));
                }

            }
        });
        imvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Nếu EditText không được kích hoạt, kích hoạt nó và thay đổi văn bản của Button
                if (!edtEmail.isEnabled()) {
                    edtEmail.setEnabled(true);
                    edtAddress.setBackgroundColor(Color.parseColor("#f09e98"));
                }
                // Ngược lại, vô hiệu hóa EditText và đặt lại văn bản của Button
                else {
                    edtEmail.setEnabled(false);
                    edtAddress.setBackgroundColor(Color.parseColor("#f5c3c0"));
                }

            }
        });
        imvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Nếu EditText không được kích hoạt, kích hoạt nó và thay đổi văn bản của Button
                if (!edtPass.isEnabled()) {
                    edtPass.setEnabled(true);
                    edtPass.setBackgroundColor(Color.parseColor("#f09e98"));
                }
                // Ngược lại, vô hiệu hóa EditText và đặt lại văn bản của Button
                else {
                    edtPass.setEnabled(false);
                    //button.setText("Chỉnh sửa");
                    edtPass.setBackgroundColor(Color.parseColor("#f5c3c0"));
                }

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        ivAvatar = binding.cirvAvatar;
        MainActivity mainActivity = new MainActivity();
        account = mainActivity.accountSignIn;
        binding.setAccount(account);
        if(account.getUrlAvatar()!= null){
            Glide.with(getContext())
                    .load(account.getUrlAvatar())
                    .centerCrop()
                    .placeholder(R.drawable.initialimage)
                    .into(ivAvatar);
        }
        return binding.getRoot();
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUESR);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = this.getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updatePass(String newPass){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        } else {
                            Log.d(TAG, "User password update failed.");
                        }
                    }
                });
    }

    private void updateAccount(){
        databaseReference.child(account.getIdAcc()).child("nameAcc").setValue(edtNameAcc.getText().toString());
        databaseReference.child(account.getIdAcc()).child("address").setValue(edtAddress.getText().toString());
        databaseReference.child(account.getIdAcc()).child("phoneNumber").setValue(edtPhone.getText().toString());
        databaseReference.child(account.getIdAcc()).child("email").setValue(edtEmail.getText().toString());
        databaseReference.child(account.getIdAcc()).child("password").setValue(edtPass.getText().toString());
        updatePass(edtPass.getText().toString());

        if(avatarUri != null){
            storageReference = FirebaseStorage.getInstance().getReference("avatarAcc");
            StorageReference fileReference = storageReference.child(account.getNameAcc()+"."+getFileExtension(avatarUri));
            storageTask = fileReference.putFile(avatarUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(), "Upload successfull !!!",Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    databaseReference.child(account.getIdAcc()).child("urlAvatar").setValue(imageUrl);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUESR && resultCode == RESULT_OK
                        && data != null &&data.getData() !=null){
            avatarUri = data.getData();
            Picasso.get().load(avatarUri).into(ivAvatar);
        }
    }
}