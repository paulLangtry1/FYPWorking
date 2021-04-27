package com.example.myfyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class skillsAdapter extends RecyclerView.Adapter<skillsAdapter.MyViewHolder>
{
    ArrayList<ExtraSkills> skillsfromdb;
    private skillsAdapter.OnContractListener monContractListener;


    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        //public Button btnAcceptContract;
        userAdpater.OnContractListener onContractListener;

        public MyViewHolder(View itemView, skillsAdapter.OnContractListener onContractListener)
        {
            super(itemView);
            txtView= itemView.findViewById(R.id.textView);

           // this.onContractListener = onContractListener;

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

    public skillsAdapter(ArrayList<ExtraSkills>myDataset, skillsAdapter.OnContractListener onContractListener)
    {
        skillsfromdb=myDataset;
        this.monContractListener = onContractListener;

    }

    @Override
    public skillsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.skills_layout,parent,false);
        skillsAdapter.MyViewHolder viewHolder=new skillsAdapter.MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull skillsAdapter.MyViewHolder holder, int position)
    {
        final ExtraSkills user=skillsfromdb.get(position);
        holder.txtView.setText("       "+user.getSkill()+"\n");

    }


    public interface OnContractListener
    {
        void onContractClick(int position);
    }



    public void add(int position, ExtraSkills user){
        skillsfromdb.add(position, user);
        notifyItemInserted(position);
    }
    public void remove(int position){
        skillsfromdb.remove(position);
        notifyItemRemoved(position);
    }
    public void update(ExtraSkills user,int position){
        skillsfromdb.set(position,user);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(ExtraSkills user){
        //these functions are user-defined
        skillsfromdb.add(user);
        notifyItemInserted(skillsfromdb.size());
    }


    @Override
    public int getItemCount() {
        return skillsfromdb.size();
    }
}
