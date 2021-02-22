package com.example.myfyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class userAdpater extends RecyclerView.Adapter<userAdpater.MyViewHolder>
{
    ArrayList<User> userFromDB;
    private userAdpater.OnContractListener monContractListener;


    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;

        //public Button btnAcceptContract;
        userAdpater.OnContractListener onContractListener;

        public MyViewHolder(View itemView, userAdpater.OnContractListener onContractListener){
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

    public userAdpater(ArrayList<User>myDataset, userAdpater.OnContractListener onContractListener)
    {
        userFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public userAdpater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.row_layout,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull userAdpater.MyViewHolder holder, int position)
    {
        final User user=userFromDB.get(position);
        holder.txtView.setText("Name :"+user.getUsername()+"\n" + "\n" + "Email :"+ user.getEmail()+ "\n" );

    }


    public interface OnContractListener
    {
        void onContractClick(int position);
    }



    public void add(int position, User user){
        userFromDB.add(position, user);
        notifyItemInserted(position);
    }
    public void remove(int position){
        userFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(User user,int position){
        userFromDB.set(position,user);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(User user){
        //these functions are user-defined
        userFromDB.add(user);
        notifyItemInserted(userFromDB.size());
    }


    @Override
    public int getItemCount() {
        return userFromDB.size();
    }
}
