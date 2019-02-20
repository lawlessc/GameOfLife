package c.lawless.gameoflife.StorageStuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import c.lawless.gameoflife.R;

import java.nio.IntBuffer;
import java.util.ArrayList;

public class GOFAdapter extends RecyclerView.Adapter<GOFAdapter.Saveviewholder> {
    public ArrayList<GOFSave> data;
    Context context;

  public GOFAdapter(ArrayList<GOFSave> data, Context context_)
    {
        this.data= data;
        context = context_;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public Saveviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // System.out.println("ABXC  oncreateViewholder called");
        View movieView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_layout, viewGroup, false);
        Saveviewholder saveviewholder = new Saveviewholder(movieView);
        return saveviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Saveviewholder saveviewholder, int i) {

      GOFSave  save =  data.get(i);
        // You are using RGBA that's why Config is ARGB.8888
        Bitmap bitmap = Bitmap.createBitmap(save.getWidth(), save.getHeight(), Bitmap.Config.ARGB_8888);
        // vector is your int[] of ARGB

        //Gets the save bytres, convertes them to integers and then a bitmap..
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(SaverFKt.convertBytestoIntegers(save.getSavedImage())));

        saveviewholder.saveTitle.setText(save.getSaveName());
        saveviewholder.saveImage.setImageBitmap(bitmap);
    }

    public static class Saveviewholder extends  RecyclerView.ViewHolder
    {
        CardView cardView;
        public ImageView saveImage;
        public TextView  saveTitle;

        public Saveviewholder(@NonNull View itemView) {
            super(itemView);
           // itemView.setBackground();
            cardView =  itemView.findViewById(R.id.save_cardview);
            saveImage = itemView.findViewById(R.id.save_image);
            saveTitle = itemView.findViewById(R.id.save_title);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
