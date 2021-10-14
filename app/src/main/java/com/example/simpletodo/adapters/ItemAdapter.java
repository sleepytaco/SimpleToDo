package com.example.simpletodo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// item adapter is responsible for taking data from a particular position and putting it into the
// view holder
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    // an interface in ItemsAdapter that the MainActivity will implement
    // this helps us pass info from ItemsAdapter to MainActivity
    // in our case, we want to let MainActivity know when a particular item in the RV was long clicked
    public interface OnLongClickListener {
        void onItemLongClicked(int position);  // the class thats implementing this interface (i.e MainActivity) will
        // know the item's position which was long clicked. The MainActivity will then notify the adapter that that's the
        // positon we want to remove from RV
    }

    List<String> items;
    OnLongClickListener longClickListener;

    public ItemAdapter(List<String> items, OnLongClickListener longClickListener) {
        // this adapter class acts as a mediator b/w our java code and the recycler view on the screen
        // anything we want to do with the RV, we ask it to our mediator and mediator helps us do it
        // Now, in order to initialize and fill out the adapter, we need to pass in some information to
        // this adapter from our java code, hence we create this ItemsAdapter constructor to help us do that
        // some info we need from java class: model data, which rv to use this adapter with
        this.items = items;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // responsible for creating each view in the RV
        // here we create a new view (i.e a row in the recycler view) and wrap it inside of a ViewHolder

        // 1. Use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // 2. Wrap it inside a ViewHolder and return it
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // responsible for taking data from a model at a particular position and putting it into a view holder
        // responsible for binding (ie filling in) data to a particular ViewHolder

        // 1. Grab the item at the specified position
        String item = items.get(position);

        // 2. Bind the item into the specified ViewHolder
        holder.bind(item);
    }

    @Override
    public int getItemCount() {  // literally just the num of items available in the data
        // this method lets the RV know how many items are there
        return items.size();
    }

    // container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        // get a reference to all the views inside a given view wrapped inside the ViewHolder
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String item) {
            // a method we created to help us update (ie fill in) the view inside of the ViewHolder
            // with the data of String item
            tvItem.setText(item);

            // we also want to set an onLongClick listenner on the view itself, so that when the item is long clicked
            // we can delete it from the model
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Remove the item from the recycler view
                    // (problem with using OnLongClickListener like this inside the ViewHolder is that we now dont
                    // have the ability to talk to the adapter behind the RV at this point)
                    // (we need to communicate that this particular item from the RV was clicked, we need to communicate
                    // this back to the MainActivity) -> (ie we need to somehow pass info from MainActivity to ItemsAdapter)
                    // -> (we do this by defining an interface in ItemsAdapter that the MainActivity will implement)

                    // getAdapterPosition() will get the position of the item in the RV that is clicked
                    longClickListener.onItemLongClicked(getAdapterPosition());

                    return true;
                }
            });
        }
    }
}
