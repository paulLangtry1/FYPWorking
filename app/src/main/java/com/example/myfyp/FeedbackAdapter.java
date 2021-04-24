package com.example.myfyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>
{

    ArrayList<Feedback> fbFromDB;
    private FeedbackAdapter.OnContractListener monContractListener;


    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        //public Button btnAcceptContract;
        FeedbackAdapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, FeedbackAdapter.OnContractListener onContractListener){
            super(itemView);
            txtView= itemView.findViewById(R.id.textView);

            this.onContractListener = onContractListener;

            itemView.setOnClickListener(this);


        }



        @Override
        public void onClick(View view)
        {
            onContractListener.onContractClick(getAdapterPosition());

            //int position=this.getLayoutPosition();
            //Contract selectedContract =contractssFromDB.get(position);
            //Toast.makeText(view.getContext(),"This worked", Toast.LENGTH_LONG).show();
            //Intent intent= new Intent(view.getContext(),CurrentContract.class);
            //view.getContext().startActivity(intent);

        }
    }

    public FeedbackAdapter(ArrayList<Feedback>myDataset, FeedbackAdapter.OnContractListener onContractListener)
    {
        fbFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public FeedbackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }




    public interface OnContractListener
    {
        void onContractClick(int position);
    }


    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.MyViewHolder holder, int position) {

        final Feedback fb=fbFromDB.get(position);
        holder.txtView.setText("Company Name :"+ fb.getCompanyName()+ "\n");
    }
    public void add(int position, Feedback feedback){
        fbFromDB.add(position, feedback);
        notifyItemInserted(position);
    }
    public void remove(int position){
        fbFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Feedback feedback,int position){
        fbFromDB.set(position,feedback);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Feedback feedback){
        //these functions are user-defined
        fbFromDB.add(feedback);
        notifyItemInserted(fbFromDB.size());
    }


    @Override
    public int getItemCount() {
        return fbFromDB.size();
    }
}

