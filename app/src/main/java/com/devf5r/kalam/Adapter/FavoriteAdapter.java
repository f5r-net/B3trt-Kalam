package com.devf5r.kalam.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.devf5r.kalam.BuildConfig;
import com.devf5r.kalam.Activity.MainActivity;
import com.devf5r.kalam.Model.FavoriteList;
import com.devf5r.kalam.Model.Quote;
import com.devf5r.kalam.R;
import com.devf5r.kalam.Utils.PrefManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback{
    private List<FavoriteList> favoriteLists;
    Context mCtx;
    private int[] images;
    private int imagesIndex = 0;
    PrefManager prf;
    private int STORAGE_PERMISSION_CODE = 1;

    public FavoriteAdapter(List<FavoriteList> favoriteLists, Context mCtx) {
        this.favoriteLists = favoriteLists;
        this.mCtx = mCtx;
        prf = new PrefManager(mCtx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_quotes,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        final FavoriteList fl = favoriteLists.get(i);



        holder.txtQuote.setText(fl.getName());

        if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(fl.getId()) == 1)
            holder.favBtn.setLiked(true);
        else
            holder.favBtn.setLiked(false);

        holder.favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                FavoriteList favoriteList = new FavoriteList();
                int id = fl.getId();
                String name = fl.getName();
                favoriteList.setId(id);
                favoriteList.setName(name);

                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
                    holder.favBtn.setLiked(true);
                    MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);
                    startSound();

                } else {
                    holder.favBtn.setLiked(false);
                    MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
                    startSound();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                FavoriteList favoriteList = new FavoriteList();
                int id = fl.getId();
                String name = fl.getName();
                favoriteList.setId(id);
                favoriteList.setName(name);

                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
                    holder.favBtn.setLiked(true);
                    MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);
                    startSound();
                } else {
                    holder.favBtn.setLiked(false);
                    MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
                    startSound();
                }
            }
        });


        //Change Random Backgrounds
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int numOfImages = 31;
                images = new int[numOfImages];
                images[0] = R.drawable.img1;
                images[1] = R.drawable.img2;
                images[2] = R.drawable.img3;
                images[3] = R.drawable.img4;
                images[4] = R.drawable.img5;
                images[5] = R.drawable.img6;
                images[6] = R.drawable.img7;
                images[7] = R.drawable.img8;
                images[8] = R.drawable.img9;
                images[9] = R.drawable.img10;
                images[10] = R.drawable.img11;
                images[11] = R.drawable.img12;
                images[12] = R.drawable.img13;
                images[13] = R.drawable.img14;
                images[14] = R.drawable.img15;
                images[15] = R.drawable.img16;
                images[16] = R.drawable.img17;
                images[17] = R.drawable.img18;
                images[18] = R.drawable.img19;
                images[19] = R.drawable.img20;
                images[20] = R.drawable.img21;
                images[21] = R.drawable.img22;
                images[22] = R.drawable.img23;
                images[23] = R.drawable.img24;
                images[24] = R.drawable.img25;
                images[25] = R.drawable.img26;
                images[26] = R.drawable.img27;
                images[27] = R.drawable.img28;
                images[28] = R.drawable.img29;
                images[29] = R.drawable.img30;
                images[30] = R.drawable.img31;

                holder.relativeLayout.setBackgroundResource(images[imagesIndex]);
                ++imagesIndex;  // update index, so that next time it points to next resource
                if (imagesIndex == images.length - 1)
                    imagesIndex = 0; // if we have reached at last index of array, simply restart from beginning
                allSound();
            }
        });

        //when you press save button
        holder.ll_quote_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(mCtx,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    holder.tv_quotes_watermark.setVisibility(View.VISIBLE);
                    Bitmap bitmap = Bitmap.createBitmap(holder.relativeLayout.getWidth(), holder.relativeLayout.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    holder.relativeLayout.draw(canvas);

                    OutputStream fos;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentResolver resolver = mCtx.getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                        Toast.makeText(mCtx, "File Saved", Toast.LENGTH_SHORT).show();
                        holder.tv_save_quote.setText("Saved");
                        holder.iv_save_quote.setImageResource(R.drawable.ic_menu_check);
                        try {
                            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                            fos.flush();
                            fos.close();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        holder.tv_quotes_watermark.setVisibility(View.INVISIBLE);
                    } else {

                        FileOutputStream outputStream = null;

                        File sdCard = Environment.getExternalStorageDirectory();

                        File directory = new File(sdCard.getAbsolutePath() + "/Latest Quotes");
                        directory.mkdir();

                        String filename = String.format("%d.jpg", System.currentTimeMillis());

                        File outFile = new File(directory, filename);

                        Toast.makeText(mCtx, "Saved", Toast.LENGTH_SHORT).show();
                        holder.tv_save_quote.setText("Saved");
                        holder.iv_save_quote.setImageResource(R.drawable.ic_menu_check);



                        try {
                            outputStream = new FileOutputStream(outFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                            outputStream.flush();
                            outputStream.close();

                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            intent.setData(Uri.fromFile(outFile));
                            mCtx.sendBroadcast(intent);


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        holder.tv_quotes_watermark.setVisibility(View.INVISIBLE);

                    }

                }else{
                    //show permission popup
                    requestStoragePermission();
                }
                startSound();

            }
        });

        //copy button
        holder.ll_copy_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mCtx.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", fl.getName());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mCtx, "تم النسخ", Toast.LENGTH_SHORT).show();
                startSound();
            }
        });

        //When You Press Share Button
        holder.ll_quote_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup();
                startSound();

            }

            private void popup() {
                PopupMenu popup = new PopupMenu(mCtx, holder.ll_quote_share);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sub_text:
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, fl.getName() + "\n https://play.google.com/store/apps/details?id="+mCtx.getPackageName());
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "بعثرة كلام");
                                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mCtx.startActivity(Intent.createChooser(shareIntent, "مشاركة").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                Toast.makeText(mCtx, "شارك كـ نص", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.sub_image:
                                holder.tv_quotes_watermark.setVisibility(View.VISIBLE);
                                Bitmap bitmap = Bitmap.createBitmap(holder.relativeLayout.getWidth(), holder.relativeLayout.getHeight(),
                                        Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                holder.relativeLayout.draw(canvas);
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+mCtx.getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mCtx.startActivity(Intent.createChooser(intent, "بعثرة كلام").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                holder.tv_quotes_watermark.setVisibility(View.INVISIBLE);
                                Toast.makeText(mCtx, "شارك كـ صورة", Toast.LENGTH_SHORT).show();

                                return true;
                        }
                        return false;
                    }
                });
                popup.inflate(R.menu.menu_item);

                popup.show();
            }
        });
    }

    //Share image tool
    private Uri getLocalBitmapUri(Bitmap bitmap) {
        Uri bmpUri = null;
        try {
            File file = new File(mCtx.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "بعثرة كلام" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(mCtx, BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    //Like , Save , Copy , share - Sound Effect
    private void startSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(mCtx,R.raw.water);

        if (prf.getBoolean("SOUND")==true) {
            likeSound.start();
        }else {
            likeSound.stop();
        }

    }

    //Sound Effect
    private void allSound() {
        MediaPlayer likeSound;
        likeSound = MediaPlayer.create(mCtx,R.raw.all);

        if (prf.getBoolean("SOUND")==true) {
            likeSound.start();
        }else {
            likeSound.stop();
        }

    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mCtx,Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(mCtx)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)mCtx,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else {
            ActivityCompat.requestPermissions((Activity)mCtx,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    //Permisssion for save images
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(mCtx, "Permission ok", Toast.LENGTH_SHORT).show();

            }else

                Toast.makeText(mCtx, "Permission not allow", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return favoriteLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView   tv_save_quote;
        TextView txtQuote , likeText;
        TextView txtCategory;
        ImageView iv_save_quote,tv_quotes_watermark;
        RelativeLayout relativeLayout;
        private Quote qte;
        LinearLayout ll_quote_save, ll_copy_quote, ll_quote_share;
        ImageView imgIcon;
        LikeButton favBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuote = itemView.findViewById(R.id.txtQuote);
            relativeLayout = itemView.findViewById(R.id.llBackground);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            txtQuote = itemView.findViewById(R.id.txtQuote);
            tv_quotes_watermark = itemView.findViewById(R.id.tv_quotes_watermark);
            likeText = itemView.findViewById(R.id.tv_like_quote_text);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            ll_copy_quote = itemView.findViewById(R.id.ll_copy_quote);
            ll_quote_save = itemView.findViewById(R.id.ll_quote_save);
            ll_quote_share = itemView.findViewById(R.id.ll_quote_share);
            tv_save_quote = itemView.findViewById(R.id.tv_save_quote);
            iv_save_quote = itemView.findViewById(R.id.iv_save_quote);
            favBtn = itemView.findViewById(R.id.favBtn);


        }
    }
}
