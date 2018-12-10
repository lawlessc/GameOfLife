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
//import chris.lawless.moviedbviewer.R;
//import chris.lawless.moviedbviewer.datafetchers.MovieImageRequest;
import org.jetbrains.annotations.NotNull;

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

//        class MImageRequest  extends MovieImageRequest
//        {
//
//            public MImageRequest(@NotNull Context context, @NotNull String poster_url) {
//                super(context, poster_url);
//            }
//
//            @Override
//            public void responseSuccessful(@NotNull Bitmap bmp) {
//
//                if(bmp != null)
//                {
//
//                    saveviewholder.movieImage.setImageBitmap(bmp);
//
//                }
//                else{
//                    saveviewholder.movieImage.setImageResource(R.drawable.ic_launcher_foreground);
//                }
//
//            }
//        }
//
//
//
//        MovieImageRequest imageRequest = new MImageRequest(context,movieData.get(i).movieImageUrl) ;
//       // System.out.println("ABXC  Bind called");
//
//
//        saveviewholder.movieTitle.setText(movieData.get(i).movieTitle);
//        saveviewholder.movieDescription.setText(movieData.get(i).movieDescription);

    }

    public static class Saveviewholder extends  RecyclerView.ViewHolder
    {
        CardView cardView;
        ImageView saveImage;
        TextView  saveTitle;

        public Saveviewholder(@NonNull View itemView) {
            super(itemView);

           // itemView.setBackground();
            cardView =  itemView.findViewById(R.id.save_cardview);
            saveImage = itemView.findViewById(R.id.save_image);
            saveTitle = itemView.findViewById(R.id.save_image);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
