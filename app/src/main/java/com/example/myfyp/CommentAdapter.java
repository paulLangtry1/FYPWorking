package com.example.myfyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>
{
    ArrayList<Comment> commentfromdb;
    private CommentAdapter.OnContractListener monContractListener;


    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView,txtView2,txtviewcontent;

        //public Button btnAcceptContract;
        CommentAdapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, CommentAdapter.OnContractListener onContractListener){
            super(itemView);
            txtView= itemView.findViewById(R.id.textView);
            txtView2= itemView.findViewById(R.id.textView2);
            txtviewcontent= itemView.findViewById(R.id.textViewcontent);

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

    public CommentAdapter(ArrayList<Comment> myDataset, CommentAdapter.OnContractListener onContractListener)
    {
        commentfromdb=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.chat_layout,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final Comment comment=commentfromdb.get(position);
        holder.txtView.setText(" "+ comment.getName());
        holder.txtviewcontent.setText("\n" +"\n" + " "+ comment.getContent());
        holder.txtView2.setText(" "+ comment.getDatetime());



    }


    public interface OnContractListener
    {
        void onContractClick(int position);
    }



    public void add(int position, Comment comment){
        commentfromdb.add(position, comment);
        notifyItemInserted(position);
    }
    public void remove(int position){
        commentfromdb.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Comment comment,int position){
        commentfromdb.set(position,comment);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Comment comment){
        //these functions are user-defined
        commentfromdb.add(comment);
        notifyItemInserted(commentfromdb.size());
    }


    @Override
    public int getItemCount() {
        return commentfromdb.size();
    }
}
