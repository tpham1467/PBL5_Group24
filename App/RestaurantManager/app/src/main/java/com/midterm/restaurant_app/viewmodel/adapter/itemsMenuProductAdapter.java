package com.midterm.restaurant_app.viewmodel.adapter;


import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.midterm.restaurant_app.databinding.ItemMenuProductsBinding;
import com.midterm.restaurant_app.databinding.LayoutDialogAddFoodForMenuBinding;
import com.midterm.restaurant_app.model.Product;
import com.midterm.restaurant_app.view.MenuFoodFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class itemsMenuProductAdapter extends RecyclerView.Adapter<itemsMenuProductAdapter.ViewHolder> {

    public static final int PICK_IMAGE_REQUESR_Product = 2;
    public Uri uri_img_food;
    public Product product;
    public LayoutDialogAddFoodForMenuBinding bindingDialog;
    private Context context;
    private List<Product> productItems;
    private ItemMenuProductsBinding binding;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference productRef;
    private MenuFoodFragment menuFoodFragment;
    private StorageTask storageTask;

    private PreferenceManager.OnActivityResultListener listener;


    public itemsMenuProductAdapter(Context context, MenuFoodFragment menuFoodFragment) {
        this.menuFoodFragment = menuFoodFragment;
        this.context = context;
        setupFirebaseListener();
    }

    public void setData(List<Product> items) {
        this.productItems = items;
        notifyDataSetChanged();
    }

    private void setupFirebaseListener() {
        database = FirebaseDatabase.getInstance();
        productRef = database.getReference("Product");

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    products.add(product);
                }
                setData(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMenuProductsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product productItem = productItems.get(position);
        if (productItem.getUrlProduct() != "") {
            Glide.with(this.context)
                    .load(productItem.getUrlProduct())
                    .centerCrop()
                    .placeholder(R.drawable.initialimage)
                    .into(holder.binding.ivMenuFoodimage);
        }
        holder.binding.setProduct(productItems.get(position));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference("Product");

        holder.binding.cardFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFeedbackDialog(Gravity.CENTER, productItems.get(position).getIdProduct());
            }

            private void openFeedbackDialog(int gravity, String productId) {
                final Dialog dialog = new Dialog(context);

                bindingDialog = LayoutDialogAddFoodForMenuBinding.inflate(LayoutInflater.from(context));

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(bindingDialog.getRoot());

                bindingDialog.progressBar.setVisibility(View.GONE);

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


                // Load data from Realtime Database
                productRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            product = snapshot.getValue(Product.class);
                            if (product != null) {
                                bindingDialog.setProduct(product);
                                Glide.with(context)
                                        .load(productItems.get(position).getUrlProduct())
                                        .centerCrop()
                                        .placeholder(R.drawable.hampogar)
                                        .into(bindingDialog.imgFood);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });

                bindingDialog.btnCancel.setText("Delete");
                bindingDialog.btnAdd.setText("Update");
                Picasso.get().load(uri_img_food).into(bindingDialog.imgFood);
                dialog.show();

                bindingDialog.imbClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                bindingDialog.ivUpload.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        menuFoodFragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUESR_Product);
                    }
                });
                bindingDialog.btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Delete product
                        productRef.child(productId).removeValue();
                        dialog.dismiss();
                    }
                });


                bindingDialog.btnAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Product prod = bindingDialog.getProduct();
                        if (uri_img_food != null) {
                            storageReference = FirebaseStorage.getInstance().getReference("imageProduct");
                            StorageReference fileReference = storageReference.child(getFileExtension(uri_img_food));
                            storageTask = fileReference.putFile(uri_img_food)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(getContext(), "Upload successfull !!!", Toast.LENGTH_SHORT).show();
                                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Product product = new Product(
                                                            uri.toString(),
                                                            prod.getIdProduct(),
                                                            bindingDialog.edtName.getText().toString().trim(),
                                                            Double.parseDouble(bindingDialog.edtPrice.getText().toString().trim()),
                                                            bindingDialog.edtIngredient.getText().toString().trim(),
                                                            4);
                                                    FirebaseDatabase.getInstance().getReference("Product")
                                                            .child(prod.getIdProduct())
                                                            .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    bindingDialog.progressBar.setVisibility(View.GONE);
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
                        else{
                            String nameUpdate = bindingDialog.edtName.getText().toString();
                            Double priceUpdate = Double.parseDouble(bindingDialog.edtPrice.getText().toString().trim());
                            String ingredientUpdate = bindingDialog.edtIngredient.getText().toString();
                            if(!nameUpdate.equals(""))
                            {
                                FirebaseDatabase.getInstance().getReference("Product").child(prod.getIdProduct()).child("nameProduct").setValue(nameUpdate);
                            }
                            if(!priceUpdate.equals("")){
                                FirebaseDatabase.getInstance().getReference("Product").child(prod.getIdProduct()).child("pricesProduct").setValue(priceUpdate);
                            }
                            if(!ingredientUpdate.equals("")){
                                FirebaseDatabase.getInstance().getReference("Product").child(prod.getIdProduct()).child("detailProduct").setValue(ingredientUpdate);
                            }
                        }
                        dialog.dismiss();

                    }
                });
            }
        });
    }

    public void setUrl(Uri url){
        this.uri_img_food = url;
        Picasso.get().load(url).into(bindingDialog.imgFood);
    }



    @Override
    public int getItemCount() {
        if (productItems != null) {
            return productItems.size();
        }
        return 0;
    }


    @SuppressLint("Range")
    private String getFileExtension(Uri uri) {
        String result = null;
        bindingDialog.progressBar.setVisibility(View.VISIBLE);
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


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemMenuProductsBinding binding;

        public ViewHolder(ItemMenuProductsBinding itembinding) {
            super(itembinding.getRoot());
            this.binding = itembinding;
        }

    }


}
