package com.example.myfyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivejobAdapter extends RecyclerView.Adapter<ActivejobAdapter.MyViewHolder>
{
    ArrayList<Contract> contractssFromDB;

    private ActivejobAdapter.OnContractListener monContractListener;

    public ActivejobAdapter(ArrayList<Comment> allcomments) {
    }

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public TextView txtView;


        //public Button btnAcceptContract;
        ActivejobAdapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, ActivejobAdapter.OnContractListener onContractListener){
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

    public ActivejobAdapter(ArrayList<Contract>myDataset, ActivejobAdapter.OnContractListener onContractListener)
    {
        contractssFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public ActivejobAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.card_layout,parent,false);
        ActivejobAdapter.MyViewHolder viewHolder=new ActivejobAdapter.MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    public interface OnContractListener
    {
        void onContractClick(int position);
    }


    @Override
    public void onBindViewHolder(@NonNull ActivejobAdapter.MyViewHolder holder, int position) {

        final Contract contract=contractssFromDB.get(position);
        holder.txtView.setText("Position :"+contract.getPosition()+"\n" + "\n" + "Address :"+contract.getAddress()+ "\n" + "Start Date :"+contract.getStartdate()+ "\n" + "End Date :"+contract.getEnddate());// and users name yet to add

    }
    public void add(int position, Contract contract){
        contractssFromDB.add(position, contract);
        notifyItemInserted(position);
    }
    public void remove(int position){
        contractssFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Contract contract,int position){
        contractssFromDB.set(position,contract);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Contract contract){
        //these functions are user-defined
        contractssFromDB.add(contract);
        notifyItemInserted(contractssFromDB.size());
    }




    @Override
    public int getItemCount() {
        return contractssFromDB.size();
    }

}
