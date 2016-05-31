package edu.ucsb.cs.cs185.azakhor.easya;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by AaronZak on 1/29/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {
    private static final String TAG = "SemesterList";
    private String[] mDataset;

    public void setmDataset(String[] mDataset) {
        this.mDataset = mDataset;
    }

    public String getDatasetAtPosition(int i){
        return mDataset[i];
    }
    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {
        // each data item is just a string in this case
        public View mView;

       /* @Override
        public void onClick(View v) {
            String trailNametoPass = ;//Todo Aaron initiate trailname using

            Log.d("Click recycler", "onClick " + getPosition() + " clicked" );
            Intent intent = new Intent(v.getContext(), TrailHistory.class);
            intent.putExtra("passTrail",trailNametoPass);
            v.getContext().startActivity(intent);

        }*/

        public ViewHolder(View v) {
            super(v);
            // v.setOnClickListener(this);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mView.setText(mDataset[positio

        // TextView test = (TextView) holder.mView.findViewById(R.id.test_text);
        // test.setText(mDataset[position]);
        //TODO Beatrix use the trail names to access the database and get the trail location, starting coordinates, and imagestring
        String trailName = mDataset[position];

       // TrailDatabaseHelper db = new TrailDatabaseHelper(holder.mView.getContext());
      //  List<Trail> trails = db.getAllTrails();
       // final Trail currentTrail = trails.get(position);



        TextView termBox = (TextView)holder.mView.findViewById(R.id.termBox);

        Log.d("Recycler", mDataset[position]);


        termBox.setText(mDataset[position]);

        //trailLocationTextView.setText(currentTrail.getLocation());
//        final File fileofpic= new File(currentTrail.getImageURL());
//        Log.d("RecyclerAdapter", currentTrail.getImageURL());
//        String.format("%.2f", currentTrail.getDistance());
//        lengthofTrailTextView.setText( String.format("%.2f", currentTrail.getDistance()/1000) + "km");
//
//
//        Picasso.with(holder.mView.getContext()).load(fileofpic).resize(512,700).memoryPolicy(MemoryPolicy.NO_CACHE).into(picroll);
//


        /*Bitmap myBitmap = BitmapFactory.decodeFile(mDataset[position]);

        picroll.setImageBitmap(myBitmap);*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}