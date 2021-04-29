package com.example.myfyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class history_adapter extends RecyclerView.Adapter<history_adapter.MyViewHolder>
{
    ArrayList<Contract> contractssFromDB;

    private history_adapter.OnContractListener monContractListener;

    public history_adapter(ArrayList<Comment> allcomments) {
    }

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView,txtview2;
        public TextView activejob;

        //public Button btnAcceptContract;
        history_adapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, history_adapter.OnContractListener onContractListener){
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

    public history_adapter(ArrayList<Contract>myDataset, history_adapter.OnContractListener onContractListener)
    {
        contractssFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public history_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.layout_for_company_admin,parent,false);
        history_adapter.MyViewHolder viewHolder=new history_adapter.MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    public interface OnContractListener
    {
        void onContractClick(int position);
    }


    @Override
    public void onBindViewHolder(@NonNull history_adapter.MyViewHolder holder, int position) {

        final Contract contract=contractssFromDB.get(position);
        holder.txtView.setText(contract.getPosition()+"\n");
//        holder.txtview2.setText("\n"+"\n"+"Address:" + " " +contract.getAddress()+"\n" + "County:"+ " " +contract.getCounty()+ "\n" + "Start Date:" + " " +contract.getStartdate()+ "\n" + "End Date:" + " "  +contract.getEnddate());

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
