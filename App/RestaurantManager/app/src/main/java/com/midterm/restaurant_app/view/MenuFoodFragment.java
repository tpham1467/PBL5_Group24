package com.midterm.restaurant_app.view;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.databinding.LayoutDialogAddFoodForMenuBinding;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.viewmodel.adapter.itemsMenuProductAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuFoodFragment extends Fragment {
    private static final int PICK_IMAGE_REQUESR = 1;
    List<Product> lstProduct;
    private RecyclerView recyclerFoods;
    private itemsMenuProductAdapter itemsAdapter;
    private Button Add_Button;
    private Uri avatarUri;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private DatabaseReference databaseReference;
    private LayoutDialogAddFoodForMenuBinding bindingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        itemsAdapter = new itemsMenuProductAdapter(view.getContext(), this);

        lstProduct = new ArrayList<>();


        itemsAdapter.setData(lstProduct);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    lstProduct.add(product);
                }

                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3); // số 2 ở đây là số cột hiển thị
        recyclerFoods = view.findViewById(R.id.rv_list_food);
//        recyclerFoods.setLayoutManager(gridLayoutManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFoods.setLayoutManager(layoutManager);
        recyclerFoods.setAdapter(itemsAdapter);

        Add_Button = view.findViewById(R.id.but_Add);
        updateAddButton(false, "+ Add");
        recyclerFoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // RecyclerView đứng yên, hiển thị nút dạng hình chữ nhật bo tròn
                    updateAddButton(false, "+ Add");
                } else {
                    // RecyclerView đang di chuyển, hiển thị nút dạng hình tròn
                    updateAddButton(true, "+");
                }
            }
        });

        Add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(Gravity.CENTER);

            }
        });


    }
    private Dialog dialog;

    private void openDialog(int gravity) {
        dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bindingMenu = LayoutDialogAddFoodForMenuBinding.inflate(LayoutInflater.from(getContext()));

        dialog.setContentView(bindingMenu.getRoot());

        bindingMenu.progressBar.setVisibility(View.GONE);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        bindingMenu.ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        bindingMenu.btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bindingMenu.btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AddProduct();
            }
        });
        dialog.show();
    }

    public void updateAddButton(boolean isRound, String text) {
        if (isRound) {
            // Đặt định dạng hình tròn cho nút
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setSize(80, 80);
            shape.setColor(ContextCompat.getColor(getContext(), R.color.green));
            Add_Button.setBackground(shape);
            Add_Button.setTextSize(40);
            Add_Button.setGravity(Gravity.CENTER);
        } else {
            // Đặt định dạng hình chữ nhật bo tròn cho nút
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setSize(200, 80);
            shape.setCornerRadius(getResources().getDimension(R.dimen.button_corner_radius));
            shape.setColor(ContextCompat.getColor(getContext(), R.color.green));
            Add_Button.setBackground(shape);
            Add_Button.setTextSize(20);
            Add_Button.setGravity(Gravity.CENTER);

        }
        Add_Button.setText(text);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUESR);
    }

    @SuppressLint("Range")
    private String getFileExtension(Uri uri) {
        String result = null;
        bindingMenu.progressBar.setVisibility(View.VISIBLE);
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void AddProduct() {
        String idPro = getIdPush();
        if (avatarUri != null) {
            storageReference = FirebaseStorage.getInstance().getReference("imageProduct");
            StorageReference fileReference = storageReference.child(getFileExtension(avatarUri));
            storageTask = fileReference.putFile(avatarUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Upload successfull !!!", Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Product product = new Product(
                                            uri.toString(),
                                            idPro,
                                            bindingMenu.edtName.getText().toString().trim(),
                                            Double.parseDouble(bindingMenu.edtPrice.getText().toString().trim()),
                                            bindingMenu.edtIngredient.getText().toString().trim(),
                                            4);
                                    FirebaseDatabase.getInstance().getReference("Product")
                                            .child(idPro)
                                            .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    bindingMenu.progressBar.setVisibility(View.GONE);
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Upload Successfully !", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Upload Fail !", Toast.LENGTH_SHORT).show();
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });
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
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUESR && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            avatarUri = data.getData();
            Picasso.get().load(avatarUri).into(bindingMenu.imgFood);
        }
        else {
            avatarUri = data.getData();
            itemsAdapter.setUrl(avatarUri);
        }
    }

    private String getIdPush() {
        String id = "";
        int i = 1;
        boolean check = false;
        while(!check){
            check = true;
            if (i < 10) id = "Pd0" + (i+1);
            else if (i >= 10) id = "Pd" + (i+1);
            for (Product product:lstProduct) {
                if (product.getIdProduct().toString().trim().equals(id)) {
                    check = false;
                    break;
                }
            }
            i++;
        }
        return id;
    }
}