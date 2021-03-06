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
    ArrayList<ExtraSkills> contractssFromDB;

    private skillsAdapter.OnContractListener monContractListener;

    public skillsAdapter(ArrayList<Comment> allcomments) {
    }

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView,txtview2;
        public TextView activejob;

        //public Button btnAcceptContract;
        skillsAdapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, skillsAdapter.OnContractListener onContractListener){
            super(itemView);
            txtView= itemView.findViewById(R.id.textView);
            txtview2 = itemView.findViewById(R.id.textView5);


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

    public skillsAdapter(ArrayList<ExtraSkills>myDataset, skillsAdapter.OnContractListener onContractListener)
    {
        contractssFromDB=myDataset;
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

    public interface OnContractListener
    {
        void onContractClick(int position);
    }


    @Override
    public void onBindViewHolder(@NonNull skillsAdapter.MyViewHolder holder, int position) {

        final ExtraSkills contract=contractssFromDB.get(position);
        holder.txtView.setText(contract.getSkill()+"\n");
       // holder.txtview2.setText("\n"+"\n"+"Address:" + " " +contract.getAddress()+"\n" + "County:"+ " " +contract.getCounty()+ "\n" + "Start Date:" + " " +contract.getStartdate()+ "\n" + "End Date:" + " "  +contract.getEnddate());

    }
    public void add(int position, ExtraSkills contract){
        contractssFromDB.add(position, contract);
        notifyItemInserted(position);
    }
    public void remove(int position){
        contractssFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(ExtraSkills contract,int position){
        contractssFromDB.set(position,contract);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(ExtraSkills contract){
        //these functions are user-defined
        contractssFromDB.add(contract);
        notifyItemInserted(contractssFromDB.size());
    }


    @Override
    public int getItemCount() {
        return contractssFromDB.size();
    }
}
